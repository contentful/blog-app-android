package blog.templates.contentful.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

/** Author model. */
@AutoValue
public abstract class Author implements Parcelable {
  public static Author create(
      @Nullable String name, @Nullable String website, @Nullable String profilePhoto,
      @Nullable String bio, @Nullable String remoteId) {
    return new AutoValue_Author(name, website, profilePhoto, bio, remoteId);
  }

  @Nullable public abstract String name();

  @Nullable public abstract String website();

  @Nullable public abstract String profilePhoto();

  @Nullable public abstract String bio();

  @Nullable public abstract String remoteId();

  // Parcelable :(
  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(name());
    dest.writeValue(website());
    dest.writeValue(profilePhoto());
    dest.writeValue(bio());
    dest.writeValue(remoteId());
  }

  public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author>() {
    @SuppressWarnings("unchecked")
    public Author createFromParcel(Parcel in) {
      ClassLoader classLoader = getClass().getClassLoader();

      String name = (String) in.readValue(classLoader);
      String website = (String) in.readValue(classLoader);
      String profilePhoto = (String) in.readValue(classLoader);
      String bio = (String) in.readValue(classLoader);
      String remoteId = (String) in.readValue(classLoader);

      return Author.create(name, website, profilePhoto, bio, remoteId);
    }

    public Author[] newArray(int size) {
      return new Author[size];
    }
  };
}
