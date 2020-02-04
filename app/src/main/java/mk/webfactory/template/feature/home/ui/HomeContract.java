package mk.webfactory.template.feature.home.ui;

import mk.webfactory.template.feature.common.ui.BasePresenter;
import mk.webfactory.template.feature.common.ui.BaseView;

interface HomeContract {

  interface View extends BaseView<Presenter> {

    void showLoadingIndicator(boolean active);
  }

  abstract class Presenter extends BasePresenter<View> {

  }
}
