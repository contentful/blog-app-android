package blog.templates.contentful.loaders;

import blog.templates.contentful.adapters.PostListAdapter;
import blog.templates.contentful.lib.LinkGenerator;
import blog.templates.contentful.vault.Author;
import blog.templates.contentful.vault.Post;
import com.commonsware.cwac.anddown.AndDown;
import com.contentful.vault.Asset;
import java.util.List;
import org.joda.time.format.ISODateTimeFormat;

public class PostLoader extends AbsAsyncLoader<String> {
  final String postTitle;

  final String postBody;

  final String postDate;

  final String postImage;

  final Author author;

  public PostLoader(Post post) {
    postTitle = post.title();
    postBody = post.body();
    postDate = post.date();

    Asset image = post.featuredImage();
    postImage = image == null ? null : image.url();

    List<Author> authors = post.authors();
    if (authors.size() == 0) {
      author = null;
    } else {
      author = authors.get(0);
    }
  }

  @SuppressWarnings("StringBufferReplaceableByString")
  @Override protected String performLoad() {
    return new StringBuilder().append("<html>")
        .append("<head>")
        .append("<style type=\"text/css\">")
        .append("a:link, a:visited, a:active { color: #4A90E2; font-weight: normal; }")
        .append("h3.top { font-weight: normal; }")
        .append("h4.bottom { font-size: 14px; }")
        .append(".bottom .date { color: #b7c2cc; font-weight: normal; }")
        .append(".thumbnail { width: 200px; display: block; margin: 0 auto; }")
        .append("</style>")
        .append("</head>")
        .append("<body>")
        .append(getThumbnailHtml())
        .append("<h3 class=\"top\">").append(postTitle).append("</h3>")
        .append("<h4 class=\"bottom\">")
        .append(getDateHtml())
        .append(getAuthorHtml())
        .append("</h4>")
        .append(new AndDown().markdownToHtml(postBody))
        .append("</body></html>")
        .toString();
  }

  private String getThumbnailHtml() {
    String result = "";

    if (postImage != null) {
      result = String.format("<img class=\"thumbnail\" src=\"%s\" />", postImage);
    }

    return result;
  }

  private String getAuthorHtml() {
    String result = "";

    if (author != null && author.name() != null) {
      result = String.format("<a style=\"margin-left: 4px;\" href=\"%s\">%s</a>",
          LinkGenerator.author(author.remoteId()), author.name());
    }

    return result;
  }

  private String getDateHtml() {
    String result = "";

    if (postDate != null) {
      result = String.format("<span class=\"date\">%s</span>",
          ISODateTimeFormat.dateOptionalTimeParser()
              .parseDateTime(postDate)
              .toString(PostListAdapter.DATE_FORMATTER)
              .toUpperCase());
    }

    return result;
  }
}
