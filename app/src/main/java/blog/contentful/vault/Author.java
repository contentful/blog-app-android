package blog.contentful.vault;

import blog.contentful.lib.Const;
import com.contentful.vault.Asset;
import com.contentful.vault.ContentType;
import com.contentful.vault.Field;
import com.contentful.vault.Resource;
import org.parceler.Parcel;

@ContentType(Const.CONTENT_TYPE_AUTHOR)
@Parcel
public class Author extends Resource {
  @Field String name;

  @Field String website;

  @Field Asset profilePhoto;

  @Field String biography;

  public String name() {
    return name;
  }

  public String website() {
    return website;
  }

  public Asset profilePhoto() {
    return profilePhoto;
  }

  public String biography() {
    return biography;
  }
}

