package mk.webfactory.template.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mk.webfactory.template.R
import mk.webfactory.template.feature.common.ui.BaseFragment
import javax.inject.Inject

class HomeFragment
@Inject constructor() : BaseFragment(), HomeContract.View {
    //Fixme: kotlin view binding  @BindView(R.id.circular_progress_view)

    //  private Unbinder unbinder;
    var circularProgressView: View? = null
    @set:Inject
    var presenter: HomeContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //    unbinder = ButterKnife.bind(this, view);
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        presenter!!.takeView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter!!.dropView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //    unbinder.unbind();
    }

    override fun showLoadingIndicator(active: Boolean) {
        circularProgressView!!.visibility = if (active) View.VISIBLE else View.GONE
    }
}