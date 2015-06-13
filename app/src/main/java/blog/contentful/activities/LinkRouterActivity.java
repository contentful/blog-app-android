package blog.contentful.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import blog.contentful.App;
import blog.contentful.Intents;
import blog.contentful.lib.ClientProvider;
import blog.contentful.lib.LinkGenerator;
import blog.contentful.lib.Preferences;

import static blog.contentful.lib.LinkGenerator.CMD_OPEN;
import static blog.contentful.lib.LinkGenerator.PATH_AUTHOR;
import static blog.contentful.lib.LinkGenerator.PATH_SPACE;

/** Interceptor for {@link Intent#ACTION_VIEW} intents with a pre-defined schema. */
public class LinkRouterActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Uri data = getIntent().getData();
    if (data != null) {
      if (CMD_OPEN.equals(data.getHost())) {
        String firstPathSegment = data.getPathSegments().get(0);

        switch (firstPathSegment) {
          case PATH_AUTHOR:
            openAuthor(data);
            break;

          case PATH_SPACE:
            openSpace(data);
            break;
        }
      }
    }

    finish();
  }

  private void openSpace(Uri data) {
    String spaceId = data.getLastPathSegment();
    String token = data.getQueryParameter("access_token");

    if (spaceId != null && token != null) {
      // Save credentials to Shared Preferences.
      Preferences.get().edit().putString(Preferences.KEY_SPACE_ID, spaceId)
          .putString(Preferences.KEY_ACCESS_TOKEN, token).commit();

      // Reset ClientProvider singleton.
      ClientProvider.reset();

      // Request sync, invalidating any existing data.
      App.requestSync(true);

      startActivity(new Intent(this, PostListActivity.class)
          .setAction(Intents.ACTION_CHANGE_SPACE)
          .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
  }

  private void openAuthor(Uri data) {
    String remoteId = data.getQueryParameter("remote_id");

    if (remoteId != null) {
      remoteId = LinkGenerator.decode(remoteId);
      startActivity(new Intent(this, PostListActivity.class)
          .putExtra(Intents.EXTRA_REMOTE_ID, remoteId));
    }
  }
}
