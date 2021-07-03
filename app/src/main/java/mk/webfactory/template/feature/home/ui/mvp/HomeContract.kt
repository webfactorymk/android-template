package mk.webfactory.template.feature.home.ui.mvp

import mk.webfactory.template.feature.common.ui.BasePresenter
import mk.webfactory.template.feature.common.ui.BaseView

interface HomeContract {
    interface View : BaseView<Presenter?> {
        fun showLoadingIndicator(active: Boolean)
    }

    abstract class Presenter :
        BasePresenter<View?>()
}