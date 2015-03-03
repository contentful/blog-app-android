package blog.templates.contentful.lib;

import blog.templates.contentful.dto.Author;
import blog.templates.contentful.dto.Category;
import blog.templates.contentful.dto.Post;
import blog.templates.contentful.sync.RealmAuthor;
import blog.templates.contentful.sync.RealmCategory;
import blog.templates.contentful.sync.RealmPost;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Converts realm objects to POJOs. */
public class RealmConverter {
  private RealmConverter() {
    throw new AssertionError();
  }

  public static Author author(RealmAuthor author) {
    return Author.create(author.getName(), author.getWebsite(), author.getProfilePhoto(),
        author.getBio(), author.getRemoteId());
  }

  public static Category category(RealmCategory category) {
    return Category.create(category.getTitle(), category.getDescription(), category.getIcon(),
        category.getRemoteId());
  }

  public static Post post(RealmPost post) {
    // Authors
    List<Author> authors = new ArrayList<>();
    for (RealmAuthor realmAuthor : post.getAuthors()) {
      authors.add(author(realmAuthor));
    }

    // Categories
    List<Category> categories = new ArrayList<>();
    for (RealmCategory realmCategory : post.getCategories()) {
      categories.add(category(realmCategory));
    }

    // Tags
    List<String> tags = Arrays.asList(post.getTags().split("\\s*,\\s*"));

    return Post.create(post.getTitle(), post.getSlug(), authors, post.getBody(), categories,
        tags, post.getFeaturedImage(), post.getDate(),
        Integer.valueOf(1).equals(post.getComments()),
        post.getRemoteId());
  }
}
