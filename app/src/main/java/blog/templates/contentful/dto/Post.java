package blog.templates.contentful.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import java.util.List;

/** Post model. */
@AutoValue
public abstract class Post implements Parcelable {
  public static Post create(@Nullable String title, @Nullable String slug,
      @Nullable List<Author> authors, @Nullable String body, @Nullable List<Category> categories,
      @Nullable List<String> tags, @Nullable String featuredImage, @Nullable String date,
      @Nullable Boolean comments, @Nullable String remoteId) {
    return new AutoValue_Post(
        title, slug, authors, body, categories, tags, featuredImage, date, comments, remoteId);
  }

  @Nullable public abstract String title();

  @Nullable public abstract String slug();

  @Nullable public abstract List<Author> authors();

  @Nullable public abstract String body();

  @Nullable public abstract List<Category> categories();

  @Nullable public abstract List<String> tags();

  @Nullable public abstract String featuredImage();

  @Nullable public abstract String date();

  @Nullable public abstract Boolean comments();

  @Nullable public abstract String remoteId();

  // Parcelable :(
  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(title());
    dest.writeValue(slug());
    dest.writeValue(authors());
    dest.writeValue(body());
    dest.writeValue(categories());
    dest.writeValue(tags());
    dest.writeValue(featuredImage());
    dest.writeValue(date());
    dest.writeValue(comments());
    dest.writeValue(remoteId());
  }

  public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
    @SuppressWarnings("unchecked")
    public Post createFromParcel(Parcel in) {
      ClassLoader classLoader = getClass().getClassLoader();

      String title = (String) in.readValue(classLoader);
      String slug = (String) in.readValue(classLoader);
      List authors = (List) in.readValue(classLoader);
      String body = (String) in.readValue(classLoader);
      List categories = (List) in.readValue(classLoader);
      List tags = (List) in.readValue(classLoader);
      String featuredImage = (String) in.readValue(classLoader);
      String date = (String) in.readValue(classLoader);
      Boolean comments = (Boolean) in.readValue(classLoader);
      String remoteId = (String) in.readValue(classLoader);

      return Post.create(title, slug, authors, body, categories, tags, featuredImage, date,
          comments, remoteId);
    }

    public Post[] newArray(int size) {
      return new Post[size];
    }
  };
}
