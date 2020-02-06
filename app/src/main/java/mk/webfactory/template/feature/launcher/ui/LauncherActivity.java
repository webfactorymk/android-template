package mk.webfactory.template.feature.launcher.ui;

import android.content.Intent;
import android.os.Bundle;
import javax.inject.Inject;
import mk.webfactory.template.R;
import mk.webfactory.template.feature.common.ui.BaseActivity;
import mk.webfactory.template.feature.home.ui.HomeActivity;
import mk.webfactory.template.user.UserManager;

public class LauncherActivity extends BaseActivity {

  @Inject
  UserManager userManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_container);

    startActivity(new Intent(LauncherActivity.this, HomeActivity.class));
    if (userManager.isLoggedIn()) {
      //startActivity(new Intent(LauncherActivity.this, HomeActivity.class));
      //finish();
    }
  }
}
