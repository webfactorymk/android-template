package mk.webfactory.template.feature.home.ui.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import mk.webfactory.template.databinding.FragmentHomeBinding
import mk.webfactory.template.feature.common.ui.BaseFragment
import javax.inject.Inject

class HomeView
@Inject constructor() : BaseFragment(), IHomeContract.View {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
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
        _binding = null
    }

    private fun showLoadingIndicator(active: Boolean) {
        binding.circularProgressView.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun observe() {
        // This is an example how to use МVVM pattern and observe the changes from the modelView
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
