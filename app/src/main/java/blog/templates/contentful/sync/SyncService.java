package blog.templates.contentful.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import blog.templates.contentful.App;
import blog.templates.contentful.Intents;
import blog.templates.contentful.activities.PostListActivity;
import blog.templates.contentful.lib.ClientProvider;
import blog.templates.contentful.lib.Const;
import blog.templates.contentful.lib.Preferences;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.Constants;
import com.contentful.java.cda.model.CDAAsset;
import com.contentful.java.cda.model.CDAEntry;
import com.contentful.java.cda.model.CDAResource;
import com.contentful.java.cda.model.CDASyncedSpace;
import io.realm.Realm;
import io.realm.RealmObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import retrofit.RetrofitError;

/** Synchronize data from Contentful to a local realm db via the Contentful Sync API. */
public class SyncService extends IntentService {
  public SyncService() {
    super(SyncService.class.getName());
  }

  public static void sync() {
    Context context = App.get();
    context.startService(new Intent(context, SyncService.class).setAction(Intents.ACTION_SYNC));
  }

  public static void changeSpace(String spaceId, String accessToken) {
    Context context = App.get();
    context.startService(new Intent(context, SyncService.class)
        .setAction(Intents.ACTION_CHANGE_SPACE)
        .putExtra(Intents.EXTRA_SPACE_ID, spaceId)
        .putExtra(Intents.EXTRA_ACCESS_TOKEN, accessToken));
  }

  @Override protected void onHandleIntent(Intent intent) {
    String action = intent.getAction();

    if (Intents.ACTION_SYNC.equals(action)) {
      actionSync();
    } else if (Intents.ACTION_CHANGE_SPACE.equals(action)) {
      actionChangeSpace(intent);
    }
  }

  private void actionChangeSpace(Intent intent) {
    String spaceId = intent.getStringExtra(Intents.EXTRA_SPACE_ID);
    String token = intent.getStringExtra(Intents.EXTRA_ACCESS_TOKEN);

    // Clear database records.
    clearDb();

    // Persist credentials to Shared Preferences.
    setCredentials(spaceId, token);

    // Launch / refresh MainActivity.
    startActivity(new Intent(getApplicationContext(), PostListActivity.class)
        .setAction(intent.getAction())
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

    // Trigger sync.
    SyncService.sync();
  }

  private void clearDb() {
    Realm realm = Realm.getInstance(this);

    try {
      realm.beginTransaction();
      realm.allObjects(RealmAuthor.class).clear();
      realm.allObjects(RealmCategory.class).clear();
      realm.allObjects(RealmPost.class).clear();
      realm.commitTransaction();
    } finally {
      realm.close();
    }
  }

  private void setCredentials(String spaceId, String token) {
    Preferences.get().edit()
        .remove(Preferences.KEY_SYNC_TOKEN)
        .putString(Preferences.KEY_SPACE_ID, spaceId)
        .putString(Preferences.KEY_ACCESS_TOKEN, token)
        .commit();

    // Reset ClientProvider singleton.
    ClientProvider.reset();
  }

  /**
   * Handles an {@link Intents#ACTION_SYNC} intent.
   */
  private void actionSync() {
    try {
      performSync();
    } catch (RetrofitError e) {
      e.printStackTrace();
      sendErrorBroadcast(e);
    } finally {
      sendReloadBroadcast();
    }
  }

  /**
   * Sends an error broadcast to any subscribers.
   *
   * @param e original error that triggered this
   */
  private static void sendErrorBroadcast(RetrofitError e) {
    Context context = App.get();
    Integer statusCode = null;

    if (RetrofitError.Kind.HTTP.equals(e.getKind())) {
      statusCode = e.getResponse().getStatus();
    }

    context.sendBroadcast(
        new Intent(Intents.ACTION_SHOW_ERROR).putExtra(Intents.EXTRA_STATUS_CODE, statusCode));
  }

  /** Sends a reload request broadcast to any subscribers. */
  private static void sendReloadBroadcast() {
    Context context = App.get();
    context.sendOrderedBroadcast(new Intent(Intents.ACTION_RELOAD), null);
  }

  /** Synchronizes the local realm database with the remote space data. */
  private static void performSync() {
    CDAClient client = ClientProvider.get();
    String syncToken = Preferences.getSyncToken();
    boolean initial = syncToken == null;
    CDASyncedSpace space;

    if (initial) {
      space = client.synchronization().performInitial();
    } else {
      space = client.synchronization().performWithToken(syncToken);
    }

    ArrayList<CDAResource> items = space.getItems();
    if (items.size() > 0) {
      Realm realm = Realm.getInstance(App.get());

      try {
        realm.beginTransaction();

        for (CDAResource resource : items) {
          if (isOfType(resource, Constants.CDAResourceType.Entry)) {
            CDAEntry entry = (CDAEntry) resource;

            if (!initial && isDeleted(resource)) {
              delete(realm, entry);
            } else {
              save(realm, entry);
            }
          }
        }

        realm.commitTransaction();
      } finally {
        realm.close();
      }
    }

    saveSyncToken(space.getSyncToken());
  }
  
  private static void save(Realm realm, CDAEntry entry) {
    String contentTypeId = extractContentTypeId(entry);

    if (Const.CONTENT_TYPE_AUTHOR.equals(contentTypeId)) {
      saveAuthor(realm, entry);
    } else if (Const.CONTENT_TYPE_CATEGORY.equals(contentTypeId)) {
      saveCategory(realm, entry);
    } else if (Const.CONTENT_TYPE_POST.equals(contentTypeId)) {
      savePost(realm, entry);
    }
  }

  private static RealmAuthor saveAuthor(Realm realm, CDAEntry entry) {
    Map fields = entry.getFields();

    RealmAuthor author = realm.where(RealmAuthor.class)
        .equalTo("remoteId", (String) entry.getSys().get("id"))
        .findFirst();

    if (author == null) {
      author = realm.createObject(RealmAuthor.class);
    }

    author.setRemoteId((String) entry.getSys().get("id"));
    author.setName(ifNull((String) fields.get("name")));
    author.setWebsite(ifNull((String) fields.get("website")));
    author.setBio(ifNull((String) fields.get("biography")));


    // Profile Photo
    CDAAsset profilePhoto = (CDAAsset) fields.get("profilePhoto");
    if (profilePhoto != null) {
      author.setProfilePhoto(profilePhoto.getUrl());
    }

    return author;
  }

  private static RealmCategory saveCategory(Realm realm, CDAEntry entry) {
    Map fields = entry.getFields();

    RealmCategory category = realm.where(RealmCategory.class)
        .equalTo("remoteId", (String) entry.getSys().get("id"))
        .findFirst();

    if (category == null) {
      category = realm.createObject(RealmCategory.class);
    }

    category.setRemoteId((String) entry.getSys().get("id"));
    category.setTitle(ifNull((String) fields.get("title")));
    category.setDescription(ifNull((String) fields.get("shortDescription")));

    // Icon
    CDAAsset icon = (CDAAsset) fields.get("icon");
    if (icon != null) {
      category.setIcon(icon.getUrl());
    }

    return category;
  }

  private static RealmPost savePost(Realm realm, CDAEntry entry) {
    Map fields = entry.getFields();
    RealmPost post = realm.where(RealmPost.class).equalTo("remoteId",
        (String) entry.getSys().get("id")).findFirst();

    if (post == null) {
      post = realm.createObject(RealmPost.class);
    }

    post.setRemoteId((String) entry.getSys().get("id"));
    post.setTitle(ifNull((String) fields.get("title")));
    post.setSlug(ifNull((String) fields.get("slug")));
    post.setBody(ifNull((String) fields.get("body")));
    post.setDate(ifNull((String) fields.get("date")));

    // Featured Image
    CDAAsset featuredImage = (CDAAsset) fields.get("featuredImage");
    if (featuredImage != null) {
      post.setFeaturedImage(featuredImage.getUrl());
    }

    // Comments
    Boolean comments = (Boolean) fields.get("comments");
    post.setComments(Boolean.TRUE.equals(comments) ? 1 : 0);

    // Authors
    List authorsList = (List) fields.get("author");
    if (authorsList != null) {
      post.getAuthors().clear();

      for (Object res : authorsList) {
        post.getAuthors().add(saveAuthor(realm, (CDAEntry) res));
      }
    }

    // Categories
    List categoriesList = (List) fields.get("category");
    if (categoriesList != null) {
      post.getCategories().clear();

      for (Object res : categoriesList) {
        post.getCategories().add(saveCategory(realm, (CDAEntry) res));
      }
    }

    // Tags
    List tagsList = (List) fields.get("tags");
    if (tagsList != null) {
      post.setTags(TextUtils.join(",", tagsList));
    }

    return post;
  }

  private static void saveSyncToken(String syncToken) {
    Preferences.get().edit().putString(Preferences.KEY_SYNC_TOKEN, syncToken).commit();
  }

  private static void delete(Realm realm, CDAEntry entry) {
    String contentTypeId = extractContentTypeId(entry);
    Class<? extends RealmObject> clazz = classForContentType(contentTypeId);
    String remoteId = (String) entry.getSys().get("id");
    realm.where(clazz).equalTo("remoteId", remoteId).findAll().clear();
  }

  private static boolean isDeleted(CDAResource resource) {
    String type = (String) resource.getSys().get("type");
    return Constants.CDAResourceType.DeletedAsset.toString().equals(type) ||
        Constants.CDAResourceType.DeletedEntry.toString().equals(type);
  }

  private static boolean isOfType(CDAResource resource, Constants.CDAResourceType type) {
    return type.equals(Constants.CDAResourceType.valueOf((String) resource.getSys().get("type")));
  }

  public static String extractContentTypeId(CDAEntry entry) {
    Map map = (Map) entry.getSys().get("contentType");
    map = (Map) map.get("sys");
    return (String) map.get("id");
  }

  public static Class<? extends RealmObject> classForContentType(String contentTypeId) {
    if (Const.CONTENT_TYPE_AUTHOR.equals(contentTypeId)) {
      return RealmAuthor.class;
    } else if (Const.CONTENT_TYPE_CATEGORY.equals(contentTypeId)) {
      return RealmCategory.class;
    } else if (Const.CONTENT_TYPE_POST.equals(contentTypeId)) {
      return RealmPost.class;
    }

    throw new IllegalArgumentException("Invalid content type id " + contentTypeId);
  }

  public static String ifNull(String str) {
    if (str == null) {
      return "";
    }

    return str;
  }
}
