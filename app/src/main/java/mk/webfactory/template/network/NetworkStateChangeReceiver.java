package mk.webfactory.template.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import mk.webfactory.template.event.NetworkConnectionEvent;
import mk.webfactory.template.util.NetworkUtils;
import org.greenrobot.eventbus.EventBus;

/**
 * Listens for changes in the network state, broadcasts a
 * {@link NetworkConnectionEvent NetworkConnection} event with the current network state
 */
public class NetworkStateChangeReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
      final boolean isOnline = NetworkUtils.isOnline(context);
      EventBus.getDefault().post(new NetworkConnectionEvent(isOnline));
    }
  }
}
