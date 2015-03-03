package blog.templates.contentful.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import blog.templates.contentful.Intents;
import blog.templates.contentful.lib.LinkGenerator;
import blog.templates.contentful.sync.SyncService;

import static blog.templates.contentful.lib.LinkGenerator.CMD_OPEN;
import static blog.templates.contentful.lib.LinkGenerator.PATH_AUTHOR;
import static blog.templates.contentful.lib.LinkGenerator.PATH_SPACE;

/**
 * Responsible for intercepting {@link Intent#ACTION_VIEW} intents for a pre-defined URL schema.
 */
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

  /**
   * Change space credentials.
   *
   * @param data data uri
   */
  private void openSpace(Uri data) {
    String spaceId = data.getLastPathSegment();
    String token = data.getQueryParameter("access_token");

    if (spaceId != null && token != null) {
      SyncService.changeSpace(spaceId, token);
    }
  }

  /**
   * Show list of posts by a specific author.
   *
   * @param data data uri
   */
  private void openAuthor(Uri data) {
    String remoteId = data.getQueryParameter("remote_id");

    if (remoteId != null) {
      remoteId = LinkGenerator.decode(remoteId);
      startActivity(new Intent(this, PostListActivity.class)
          .putExtra(Intents.EXTRA_REMOTE_ID, remoteId));
    }
  }
}
