package blog.templates.contentful;

import android.app.Application;
import blog.templates.contentful.sync.SyncService;

/** Main Application class. */
public class App extends Application {
  private static App sInstance;

  @Override public void onCreate() {
    super.onCreate();
    sInstance = this;

    SyncService.sync();
  }

  public static App get() {
    return sInstance;
  }
}
