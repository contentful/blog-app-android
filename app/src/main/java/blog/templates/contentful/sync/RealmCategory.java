package blog.templates.contentful.sync;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/** RealmCategory. */
public class RealmCategory extends RealmObject {
  @PrimaryKey
  private String remoteId;

  private String title;
  private String description;
  private String icon;

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }
}
