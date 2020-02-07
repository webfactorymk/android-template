package mk.webfactory.template.feature.common.ui

/**
 * Base presenter for the MVP pattern.
 */
abstract class BasePresenter<View> {

    protected var view: View? = null
        private set

    fun takeView(view: View) {
        if (this.view != null) throw IllegalStateException("takeView before previous view is dropped.")
        this.view = view
        onTakeView(view)
    }

    fun dropView(view: View) {
        if (this.view !== view) {
            return
        }
        onDropView()
        this.view = null
    }

    protected abstract fun onTakeView(v: View)

    protected abstract fun onDropView()

    fun hasView(): Boolean {
        return view != null
    }
}