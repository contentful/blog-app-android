package blog.templates.contentful.loaders;

import android.support.annotation.Nullable;
import blog.templates.contentful.dto.Author;
import blog.templates.contentful.dto.Post;
import blog.templates.contentful.lib.RealmConverter;
import blog.templates.contentful.sync.RealmAuthor;
import blog.templates.contentful.sync.RealmPost;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

/** Loader for a list of posts. */
public class PostListLoader extends AbsLoader<PostListLoader.Result> {
  private final String authorRemoteId;

  private PostListLoader(@Nullable String authorRemoteId) {
    super();
    this.authorRemoteId = authorRemoteId;
  }

  public static PostListLoader newInstance() {
    return new PostListLoader(null);
  }

  public static PostListLoader forAuthor(String remoteId) {
    return new PostListLoader(remoteId);
  }

  @Override protected Result performLoad() {
    Result result;
    Realm realm = Realm.getInstance(getContext());

    try {
      List<Post> posts = getPostList(realm, authorRemoteId);

      Author author = null;
      if (authorRemoteId != null) {
        author = getAuthor(realm, authorRemoteId);
      }

      result = new Result(posts, author);
    } finally {
      realm.close();
    }

    return result;
  }

  private Author getAuthor(Realm realm, String authorRemoteId) {
    RealmAuthor realmAuthor =
        realm.where(RealmAuthor.class).equalTo("remoteId", authorRemoteId).findFirst();

    if (realmAuthor != null) {
      return RealmConverter.author(realmAuthor);
    }
    
    return null;
  }

  private List<Post> getPostList(Realm realm, String authorRemoteId) {
    RealmResults<RealmPost> posts;

    if (authorRemoteId == null) {
      posts = realm.allObjects(RealmPost.class);
    } else {
      posts = realm.where(RealmPost.class).equalTo("authors.remoteId", authorRemoteId).findAll();
    }

    return convert(posts);
  }

  private List<Post> convert(RealmResults<RealmPost> posts) {
    List<Post> result = new ArrayList<>();

    for (RealmPost post : posts) {
      result.add(RealmConverter.post(post));
    }

    return result;
  }

  public static class Result {
    private final List<Post> posts;
    private final Author author;

    public Result(List<Post> posts, Author author) {
      this.posts = posts;
      this.author = author;
    }

    public List<Post> getPosts() {
      return posts;
    }

    public Author getAuthor() {
      return author;
    }
  }
}
