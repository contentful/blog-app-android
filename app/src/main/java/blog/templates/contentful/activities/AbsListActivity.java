package blog.templates.contentful.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import blog.templates.contentful.Intents;
import blog.templates.contentful.R;
import blog.templates.contentful.adapters.AbsListAdapter;
import blog.templates.contentful.sync.SyncService;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import java.util.List;

public abstract class AbsListActivity<A, L> extends AbsActivity
    implements SwipeRefreshLayout.OnRefreshListener,
    LoaderManager.LoaderCallbacks<L> {

  protected abstract int getLoaderId();
  protected abstract AbsListAdapter<A, ?> createAdapter();
  protected abstract List<A> getResultList(L data);

  // Views
  @InjectView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
  @InjectView(R.id.list) ListView listView;

  // Data
  protected final int LOADER_ID = getLoaderId();
  protected AbsListAdapter<A, ?> adapter;

  // Receivers
  protected BroadcastReceiver reloadReceiver;
  protected BroadcastReceiver errorReceiver;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    ButterKnife.inject(this);

    // Adapter
    adapter = createAdapter();

    // Receivers
    createReceivers();
    registerReceiver(reloadReceiver, new IntentFilter(Intents.ACTION_RELOAD));

    // Loaders
    initList();
    initSwipeRefresh();
    initLoader();
  }

  @Override protected void onResume() {
    super.onResume();
    registerReceiver(errorReceiver, new IntentFilter(Intents.ACTION_SHOW_ERROR));
  }

  @Override protected void onPause() {
    unregisterReceiver(errorReceiver);
    super.onPause();
  }

  @Override public void onDestroy() {
    unregisterReceiver(reloadReceiver);
    super.onDestroy();
  }

  protected void createReceivers() {
    reloadReceiver = new BroadcastReceiver() {
      @Override public void onReceive(Context context, Intent intent) {
        restartLoader();
      }
    };

    errorReceiver = new BroadcastReceiver() {
      @Override public void onReceive(Context context, Intent intent) {
        showErrorDialog(intent.getIntExtra(Intents.EXTRA_STATUS_CODE, -1));
      }
    };
  }

  protected void showErrorDialog(int httpStatusCode) {
    int title = R.string.dialog_general_error_title;
    int message = R.string.dialog_general_error_message;

    if (httpStatusCode == 401) {
      message = R.string.dialog_unauthorized_error_message;
    }

    new AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, null)
        .show();
  }

  protected void initSwipeRefresh() {
    swipeRefreshLayout.setOnRefreshListener(this);
  }

  protected void initList() {
    listView.setAdapter(adapter);
  }

  protected void initLoader() {
    getSupportLoaderManager().initLoader(LOADER_ID, null, this);
  }

  protected void restartLoader() {
    getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
  }

  @Override public void onLoadFinished(Loader<L> loader, L data) {
    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
      swipeRefreshLayout.setRefreshing(false);
    }

    if (data != null) {
      adapter.setData(getResultList(data));
      adapter.notifyDataSetChanged();
    }
  }

  @Override public void onLoaderReset(Loader<L> loader) {

  }

  @Override public void onRefresh() {
    SyncService.sync();
  }

  @OnItemClick(R.id.list)
  void onItemClick(View v, int position) {
  }
}
