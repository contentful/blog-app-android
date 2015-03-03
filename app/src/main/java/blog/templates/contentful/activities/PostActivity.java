package blog.templates.contentful.activities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.webkit.WebView;
import blog.templates.contentful.Intents;
import blog.templates.contentful.R;
import blog.templates.contentful.dto.Post;
import blog.templates.contentful.lib.LoaderId;
import blog.templates.contentful.loaders.PostLoader;
import butterknife.ButterKnife;
import butterknife.InjectView;

/** Displays a single post. */
public class PostActivity extends AbsActivity {
  @InjectView(R.id.web_view) WebView webView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post);
    ButterKnife.inject(this);

    initLoader();
  }

  private void initLoader() {
    getSupportLoaderManager().initLoader(LoaderId.forClass(PostActivity.class),
        getIntent().getExtras(), new LoaderManager.LoaderCallbacks<String>() {
          @Override public Loader<String> onCreateLoader(int id, Bundle args) {
            return new PostLoader((Post) args.getParcelable(Intents.EXTRA_POST));
          }

          @Override public void onLoadFinished(Loader<String> loader, String data) {
            webView.loadData(data, "text/html", "utf-8");
          }

          @Override public void onLoaderReset(Loader<String> loader) {

          }
        });
  }
}
