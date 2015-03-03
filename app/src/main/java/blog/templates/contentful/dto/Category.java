package blog.templates.contentful.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

/** Category model. */
@AutoValue
public abstract class Category implements Parcelable {
  public static Category create(@Nullable String title, @Nullable String description,
      @Nullable String icon, @Nullable String remoteId) {
    return new AutoValue_Category(title, description, icon, remoteId);
  }

  @Nullable public abstract String title();

  @Nullable public abstract String description();

  @Nullable public abstract String icon();

  @Nullable public abstract String remoteId();

  // Parcelable :(
  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(title());
    dest.writeValue(description());
    dest.writeValue(icon());
    dest.writeValue(remoteId());
  }

  public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
    @SuppressWarnings("unchecked")
    public Category createFromParcel(Parcel in) {
      ClassLoader classLoader = getClass().getClassLoader();

      String title = (String) in.readValue(classLoader);
      String description = (String) in.readValue(classLoader);
      String icon = (String) in.readValue(classLoader);
      String remoteId = (String) in.readValue(classLoader);

      return Category.create(title, description, icon, remoteId);
    }

    public Category[] newArray(int size) {
      return new Category[size];
    }
  };
}
