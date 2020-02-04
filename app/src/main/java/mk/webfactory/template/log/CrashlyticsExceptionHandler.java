package mk.webfactory.template.log;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StatFs;
import android.os.SystemClock;
import com.crashlytics.android.core.CrashlyticsCore;
import java.util.List;
import mk.webfactory.template.App;
import timber.log.Timber;

import static java.util.concurrent.TimeUnit.SECONDS;

class CrashlyticsExceptionHandler implements Thread.UncaughtExceptionHandler {

  private static final long SLEEP_BEFORE_FORWARD = SECONDS.toMillis(1);

  private final CrashlyticsCore crashlyticsCore;
  private final Context context;
  private final Thread.UncaughtExceptionHandler handler;

  public CrashlyticsExceptionHandler(CrashlyticsCore crashlyticsCore, Context context) {
    this.crashlyticsCore = crashlyticsCore;
    this.context = context.getApplicationContext();
    this.handler = Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  @Override public void uncaughtException(Thread thread, Throwable ex) {
    logCurrentDeviceState();
    logLastUsedPages();
    SystemClock.sleep(SLEEP_BEFORE_FORWARD);
    handler.uncaughtException(thread, ex);
  }

  private void logLastUsedPages() {
    String output = "Last used pages:\n";
    List<String> items = App.CRASH_REPORT.getLastUsedPages();
    for (int i = items.size() - 1; i >= 0; i--) {
      output += items.get(i) + "\n";
    }
    Timber.e(output);
  }

  private void logCurrentDeviceState() {
    final NetworkInfo network = ((ConnectivityManager) context.getSystemService
        (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    crashlyticsCore.setString("networkAccess",
        network != null && network.isConnected() ? network.getTypeName() : "NA");
    crashlyticsCore.setString("heap", toMbString(Runtime.getRuntime().totalMemory()));
    crashlyticsCore.setString("freeMemory", toMbString(Runtime.getRuntime().freeMemory()));
    crashlyticsCore.setString("maxHeap", toMbString(Runtime.getRuntime().maxMemory()));
    crashlyticsCore.setString("freeDisk", toMbString(getFreeDiskMemory()));
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  private long getFreeDiskMemory() {
    final StatFs diskStats = new StatFs(context.getFilesDir().getPath());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return diskStats.getAvailableBytes();
    }
    //noinspection deprecation
    return diskStats.getAvailableBlocks() * diskStats.getBlockSize();
  }

  private String toMbString(long bytes) {
    return String.format("%.2fMB", (float) bytes / 1024 / 1024);
  }
}
