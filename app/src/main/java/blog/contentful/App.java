package blog.contentful;

import android.app.Application;
import blog.contentful.lib.ClientProvider;
import blog.contentful.vault.BlogSpace;
import com.contentful.vault.SyncConfig;
import com.contentful.vault.Vault;

public class App extends Application {
  private static App instance;

  @Override public void onCreate() {
    super.onCreate();
    instance = this;
    requestSync();
  }

  public static App get() {
    return instance;
  }

  public static void requestSync() {
    requestSync(false);
  }

  public static void requestSync(boolean invalidate) {
    Vault.with(get(), BlogSpace.class).requestSync(
        SyncConfig.builder()
            .setClient(ClientProvider.get())
            .setInvalidate(invalidate)
            .build());
  }
}
