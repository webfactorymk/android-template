package mk.webfactory.template.log;

import androidx.annotation.Nullable;

import java.util.List;

public interface CrashReportLogger {

  void setLoggedInUser(@Nullable String userIdentifier);

  void setCurrentPage(String page);

  List<String> getLastUsedPages();
}
