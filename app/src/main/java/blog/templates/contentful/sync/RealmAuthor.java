package blog.templates.contentful.sync;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/** RealmAuthor. */
public class RealmAuthor extends RealmObject {
  @PrimaryKey
  private String remoteId;

  private String name;
  private String website;
  private String profilePhoto;
  private String bio;

  public String getRemoteId() {
    return remoteId;
  }

  public void setRemoteId(String remoteId) {
    this.remoteId = remoteId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getProfilePhoto() {
    return profilePhoto;
  }

  public void setProfilePhoto(String profilePhoto) {
    this.profilePhoto = profilePhoto;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }
}
