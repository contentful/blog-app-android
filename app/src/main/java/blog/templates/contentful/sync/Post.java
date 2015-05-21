package blog.templates.contentful.sync;

import blog.templates.contentful.lib.Const;
import com.contentful.vault.Asset;
import com.contentful.vault.ContentType;
import com.contentful.vault.Field;
import java.util.List;
import org.parceler.Parcel;

@ContentType(Const.CONTENT_TYPE_POST)
@Parcel
public class Post {
  @Field String title;

  @Field String slug;

  @Field("author")
  List<Author> authors;

  @Field String body;

  @Field("category")
  List<Category> categories;

  @Field List<String> tags;

  @Field Asset featuredImage;

  @Field String date;

  @Field boolean comments;

  public String title() {
    return title;
  }

  public String slug() {
    return slug;
  }

  public List<Author> authors() {
    return authors;
  }

  public String body() {
    return body;
  }

  public List<Category> categories() {
    return categories;
  }

  public List<String> tags() {
    return tags;
  }

  public Asset featuredImage() {
    return featuredImage;
  }

  public String date() {
    return date;
  }

  public boolean isComments() {
    return comments;
  }
}
