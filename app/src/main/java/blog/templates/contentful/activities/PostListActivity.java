package blog.templates.contentful.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.view.View;
import blog.templates.contentful.Intents;
import blog.templates.contentful.adapters.AbsListAdapter;
import blog.templates.contentful.adapters.PostListAdapter;
import blog.templates.contentful.dto.Author;
import blog.templates.contentful.dto.Post;
import blog.templates.contentful.lib.LoaderId;
import blog.templates.contentful.loaders.PostListLoader;
import blog.templates.contentful.views.AuthorView;
import java.util.List;

/**
 * Displays a list of posts.
 *
 * Given an author's remote ID was provided as an extra of the intent will attach additional
 * header view to the list containing the author's details and filter the posts accordingly,
 * otherwise displays all available posts.
 */
public class PostListActivity extends AbsListActivity<Post, PostListLoader.Result> {
  private AuthorView authorView;

  @Override protected int getLoaderId() {
    return LoaderId.forClass(PostListActivity.class);
  }

  @Override protected AbsListAdapter<Post, ?> createAdapter() {
    return new PostListAdapter();
  }

  @Override public Loader<PostListLoader.Result> onCreateLoader(int id, Bundle args) {
    String authorRemoteId = getAuthorRemoteId();
    if (authorRemoteId == null) {
      return PostListLoader.newInstance();
    }

    return PostListLoader.forAuthor(authorRemoteId);
  }

  @Override protected List<Post> getResultList(PostListLoader.Result data) {
    return data.getPosts();
  }

  @Override public void onLoadFinished(Loader<PostListLoader.Result> loader,
      PostListLoader.Result data) {
    super.onLoadFinished(loader, data);

    if (data != null) {
      Author author = data.getAuthor();
      if (author != null) {
        authorView.populate(author);
      }
    }
  }

  @Override void onItemClick(View v, int position) {
    int headerViewsCount = listView.getHeaderViewsCount();
    if (headerViewsCount > 0 && position < headerViewsCount) {
      return;
    }

    Post post = adapter.getItem(position - headerViewsCount);
    startActivity(new Intent(this, PostActivity.class)
        .putExtra(Intents.EXTRA_POST, post));
  }

  @Override protected void initList() {
    String authorRemoteId = getAuthorRemoteId();
    if (authorRemoteId != null) {
      authorView = new AuthorView(this);
      listView.addHeaderView(authorView);
    }

    super.initList();
  }

  @Nullable
  private String getAuthorRemoteId() {
    return getIntent().getStringExtra(Intents.EXTRA_REMOTE_ID);
  }
}
