package mk.webfactory.template.feature.home.ui;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import javax.inject.Inject;
import mk.webfactory.template.R;
import mk.webfactory.template.feature.common.ui.BaseActivity;

public class HomeActivity extends BaseActivity {

  @Inject HomeFragment injectedFragment;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_container);

    HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.root_container);
    if (homeFragment == null) {
      homeFragment = injectedFragment;
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.root_container, homeFragment);
      transaction.commit();
    }
  }
}
