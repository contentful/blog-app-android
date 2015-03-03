package blog.templates.contentful.lib;

import blog.templates.contentful.App;
import blog.templates.contentful.R;

/** Const. */
public class Const {
  private Const() {
    throw new AssertionError();
  }

  // Content Type IDs
  public static final String CONTENT_TYPE_AUTHOR =
      App.get().getString(R.string.content_type_author);

  public static final String CONTENT_TYPE_CATEGORY =
      App.get().getString(R.string.content_type_category);

  public static final String CONTENT_TYPE_POST =
      App.get().getString(R.string.content_type_post);
}
