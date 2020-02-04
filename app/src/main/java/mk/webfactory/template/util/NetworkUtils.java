package mk.webfactory.template.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import timber.log.Timber;

public class NetworkUtils {

  private final Context applicationContext;

  public NetworkUtils(Context context) {
    this.applicationContext = context.getApplicationContext();
  }

  public boolean isOnline() {
    return isOnline(applicationContext);
  }

  /**
   * Checks if there is an active internet connection on this device
   *
   * @return <code>true</code> if the device is connected to the internet;
   * <code>false</code> otherwise
   */
  public static boolean isOnline(Context context) {
    try {
      ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo netInfo = cm.getActiveNetworkInfo();
      return netInfo != null
          && netInfo.getState() == NetworkInfo.State.CONNECTED
          && (netInfo.getType() == ConnectivityManager.TYPE_WIFI
          || netInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    } catch (Exception e) {
      Timber.e(e);
      return false;
    }
  }
}
