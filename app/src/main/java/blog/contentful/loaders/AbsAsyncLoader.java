package blog.contentful.loaders;

import android.support.v4.content.AsyncTaskLoader;
import blog.contentful.App;

/**
 * Base loader implementation.
 *
 * @param <T> result type
 */
public abstract class AbsAsyncLoader<T> extends AsyncTaskLoader<T> {
  protected T result;

  AbsAsyncLoader() {
    super(App.get());
  }

  @Override public T loadInBackground() {
    return result = performLoad();
  }

  @Override protected void onStartLoading() {
    if (result != null) {
      deliverResult(result);
    }

    if (takeContentChanged() || result == null) {
      forceLoad();
    }
  }

  @Override protected void onReset() {
    super.onReset();

    result = null;
  }

  protected abstract T performLoad();
}