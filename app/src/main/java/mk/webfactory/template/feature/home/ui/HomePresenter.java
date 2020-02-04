package mk.webfactory.template.feature.home.ui;

import javax.inject.Inject;
import mk.webfactory.template.feature.home.HomeRepository;

final class HomePresenter extends HomeContract.Presenter {

  private final HomeRepository homeRepository;

  @Inject public HomePresenter(HomeRepository homeRepository) {
    this.homeRepository = homeRepository;
  }

  @Override protected void onTakeView(HomeContract.View v) {
    if (hasView()) {
      getView().showLoadingIndicator(true);
    }
  }

  @Override protected void onDropView() {
  }
}
