package mk.webfactory.template.log;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public final class CrashlyticsLogger extends Timber.Tree implements CrashReportLogger {

  private static final int PAGES_LIST_SIZE = 5;

  private final Context context;
  private final CrashlyticsCore crashlytics;
  private final CrashlyticsExceptionHandler exceptionHandler;
  private final ArrayList<String> lastUsedPages;

  public CrashlyticsLogger(Context context, Crashlytics crashlytics) {
    this.crashlytics = crashlytics.core;
    this.context = context;
    this.lastUsedPages = new ArrayList<>(PAGES_LIST_SIZE);
    this.exceptionHandler = new CrashlyticsExceptionHandler(crashlytics.core, context);
    logDeviceInfo();
  }

  private void logDeviceInfo() {
    final DisplayMetrics dm = context.getResources().getDisplayMetrics();
    crashlytics.setString("screenResolution", dm.heightPixels + "x" + dm.widthPixels);
    crashlytics.setFloat("screenDensity", dm.density);
    crashlytics.setInt("apiLevel", Build.VERSION.SDK_INT);
    crashlytics.setString("manufacturer", Build.MANUFACTURER);
    crashlytics.setString("model", Build.MODEL);
  }

  @Override public void setCurrentPage(String page) {
    crashlytics.setString("currentPage", page);
    lastUsedPages.add(page);
    if (lastUsedPages.size() > PAGES_LIST_SIZE) {
      lastUsedPages.remove(0);
    }
  }

  @Override public List<String> getLastUsedPages() {
    return lastUsedPages;
  }

  @Override protected void log(int priority, String tag, String message, Throwable t) {
    if (priority == Log.VERBOSE || priority == Log.DEBUG) {
      return;
    }
    crashlytics.log(priority, tag, message);
    if (t != null) {
      crashlytics.logException(t);
    }
  }
}
