package blog.templates.contentful.sync;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/** RealmPost. */
public class RealmPost extends RealmObject {
  @PrimaryKey
  private String remoteId;

  private String title;
  private String slug;
  private String body;
  private String featuredImage;
  private String date;
  private int comments;
  private RealmList<RealmAuthor> authors;
  private RealmList<RealmCategory> categories;
  private String tags;

  public String getRemoteId() {
    return remoteId;
  }

  public void setRemoteId(String remoteId) {
    this.remoteId = remoteId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getFeaturedImage() {
    return featuredImage;
  }

  public void setFeaturedImage(String featuredImage) {
    this.featuredImage = featuredImage;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getComments() {
    return comments;
  }

  public void setComments(int comments) {
    this.comments = comments;
  }

  public RealmList<RealmAuthor> getAuthors() {
    return authors;
  }

  public void setAuthors(RealmList<RealmAuthor> authors) {
    this.authors = authors;
  }

  public RealmList<RealmCategory> getCategories() {
    return categories;
  }

  public void setCategories(RealmList<RealmCategory> categories) {
    this.categories = categories;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }
}
