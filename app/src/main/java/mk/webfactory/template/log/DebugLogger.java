package mk.webfactory.template.log;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class DebugLogger extends Timber.DebugTree implements CrashReportLogger {

  private static final String TAG = "CrashReportLogger";

  @Override public void setCurrentPage(String page) {
    log(Log.INFO, TAG, page, null);
  }

  @Override public List<String> getLastUsedPages() {
    return new ArrayList<>();
  }
}
