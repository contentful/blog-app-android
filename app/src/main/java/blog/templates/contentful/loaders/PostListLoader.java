package blog.templates.contentful.loaders;

import android.support.annotation.Nullable;
import blog.templates.contentful.vault.Author;
import blog.templates.contentful.vault.BlogSpace;
import blog.templates.contentful.vault.Post;
import com.contentful.vault.Vault;
import java.util.ArrayList;
import java.util.List;

public class PostListLoader extends AbsAsyncLoader<PostListLoader.Result> {
  private final String authorRemoteId;

  public PostListLoader(@Nullable String authorRemoteId) {
    super();
    this.authorRemoteId = authorRemoteId;
  }

  @Override protected Result performLoad() {
    Vault vault = Vault.with(getContext(), BlogSpace.class);
    List<Post> posts = vault.fetch(Post.class).all();
    if (authorRemoteId == null) {
      return new Result(posts, null);
    }

    Author author = vault.fetch(Author.class)
        .where("remote_id = ?", authorRemoteId)
        .first();

    List<Post> filtered = new ArrayList<>();
    if (author != null) {
      for (Post post : posts) {
        if (postAuthorMatches(post, authorRemoteId)) {
          filtered.add(post);
        }
      }
    }

    return new Result(filtered, author);
  }

  private boolean postAuthorMatches(Post post, String remoteId) {
    for (Author author : post.authors()) {
      if (remoteId.equals(author.remoteId())) {
        return true;
      }
    }
    return false;
  }

  public static class Result {
    private final List<Post> posts;
    private final Author author;

    public Result(List<Post> posts, Author author) {
      this.posts = posts;
      this.author = author;
    }

    public List<Post> posts() {
      return posts;
    }

    public Author author() {
      return author;
    }
  }
}
