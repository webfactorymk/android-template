package mk.webfactory.template.feature.home.ui.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_home.*
import mk.webfactory.template.R
import mk.webfactory.template.feature.common.ui.BaseFragment
import javax.inject.Inject

class HomeView
@Inject constructor() : BaseFragment(), IHomeContract.View {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe()
        setUpClickListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()

        //TODO implement with DI
        viewModel = HomeViewModel()

        viewModel.handleEvent(HomeEvent.OnStart)
    }

    override fun onStop() {
        viewModel.handleEvent(HomeEvent.OnStop)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun showLoadingIndicator(active: Boolean) {
        circular_progress_view!!.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun observe() {
        // This is an example how to use mvvm pattern and observe the changes from the modelView
        viewModel.progressBarState.observe(
            viewLifecycleOwner,
            Observer {
                showLoadingIndicator(it)
            }
        )
    }

    override fun setUpClickListeners() {
        //TODO implement this method by passing the corresponding event to the viewModel with viewModel.handleEvent(HomeEvent) method
    }

}

