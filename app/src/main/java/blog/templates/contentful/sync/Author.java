package blog.templates.contentful.sync;

import blog.templates.contentful.lib.Const;
import com.contentful.vault.Asset;
import com.contentful.vault.ContentType;
import com.contentful.vault.Field;
import java.util.List;
import org.parceler.Parcel;

@ContentType(Const.CONTENT_TYPE_AUTHOR)
@Parcel
public class Author {
  @Field String name;

  @Field String website;

  @Field Asset profilePhoto;

  @Field String biography;

  @Field("createdEntries")
  List<Post> posts;

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

  public List<Post> posts() {
    return posts;
  }
}
