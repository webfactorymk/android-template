package mk.webfactory.template.feature.home.ui

import mk.webfactory.template.feature.home.HomeRepository
import javax.inject.Inject

class HomePresenter @Inject constructor(private val homeRepository: HomeRepository) :
    HomeContract.Presenter() {
    override fun onTakeView(v: HomeContract.View?) {
        if (hasView()) {
            view!!.showLoadingIndicator(true)
        }
    }

    override fun onDropView() {}

}