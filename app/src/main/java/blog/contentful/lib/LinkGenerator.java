package blog.contentful.lib;

import blog.contentful.App;
import blog.contentful.R;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class LinkGenerator {
  private LinkGenerator() {
    throw new AssertionError();
  }

  public static final String SCHEMA = App.get().getString(R.string.url_intercept_schema);
  public static final String CMD_OPEN = "open";
  public static final String PATH_AUTHOR = "author";
  public static final String PATH_SPACE = "space";

  private static final String ENCODING = "utf-8";

  /**
   * Generate link to show list of posts for a specific author.
   *
   * @param remoteId author remote id
   * @return url
   */
  public static String author(String remoteId) {
    return String.format("%s/%s?remote_id=%s", open(), PATH_AUTHOR, encode(remoteId));
  }

  /**
   * Generate base link with {@link #CMD_OPEN} command.
   *
   * @return url
   */
  private static String open() {
    return String.format("%s://%s", SCHEMA, CMD_OPEN);
  }

  /**
   * URL encode the given {@code str}.
   *
   * @param str string to encode
   * @return encoded string result
   */
  public static String encode(String str) {
    try {
      return URLEncoder.encode(str, ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * URL decode the given {@code str}.
   *
   * @param str string to decode
   * @return decoded string result
   */
  public static String decode(String str) {
    try {
      return URLDecoder.decode(str, ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
