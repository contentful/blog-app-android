package blog.contentful.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import blog.contentful.Intents;
import blog.contentful.R;
import blog.contentful.lib.LoaderId;
import blog.contentful.loaders.PostLoader;
import blog.contentful.vault.Post;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.parceler.Parcels;

public class PostActivity extends AbsActivity {
  @InjectView(R.id.web_view) WebView webView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post);
    ButterKnife.inject(this);

    webView.setWebViewClient(new WebViewClient(){
      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(getString(R.string.url_intercept_schema))) {
          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
          return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
      }
    });

    initLoader();
  }

  private void initLoader() {
    getSupportLoaderManager().initLoader(LoaderId.forClass(PostActivity.class),
        getIntent().getExtras(), new LoaderManager.LoaderCallbacks<String>() {
          @Override public Loader<String> onCreateLoader(int id, Bundle args) {
            return new PostLoader((Post) Parcels.unwrap(args.getParcelable(Intents.EXTRA_POST)));
          }

          @Override public void onLoadFinished(Loader<String> loader, String data) {
            webView.loadData(data, "text/html", "utf-8");
          }

          @Override public void onLoaderReset(Loader<String> loader) {

          }
        });
  }
}
