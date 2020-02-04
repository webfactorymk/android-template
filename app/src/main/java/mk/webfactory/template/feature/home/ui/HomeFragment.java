package mk.webfactory.template.feature.home.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import javax.inject.Inject;
import mk.webfactory.template.R;
import mk.webfactory.template.feature.common.ui.BaseFragment;

public class HomeFragment extends BaseFragment implements HomeContract.View {

  @BindView(R.id.circular_progress_view)
  View circularProgressView;

  @Inject HomeContract.Presenter presenter;
  private Unbinder unbinder;

  @Inject public HomeFragment() {}

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onStart() {
    super.onStart();
    presenter.takeView(this);
  }

  @Override public void onStop() {
    super.onStop();
    presenter.dropView(this);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override public void showLoadingIndicator(boolean active) {
    circularProgressView.setVisibility(active ? View.VISIBLE : View.GONE);
  }
}
