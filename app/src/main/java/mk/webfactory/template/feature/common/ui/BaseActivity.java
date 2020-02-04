package mk.webfactory.template.feature.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import androidx.core.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;
import mk.webfactory.template.App;

public abstract class BaseActivity extends AppCompatActivity implements HasSupportFragmentInjector {

  @Inject DispatchingAndroidInjector<Fragment> supportFragmentInjector;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
  }

  @Override protected void onResume() {
    super.onResume();
    App.CRASH_REPORT.setCurrentPage(getClass().getCanonicalName());
  }

  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
    return supportFragmentInjector;
  }
}
