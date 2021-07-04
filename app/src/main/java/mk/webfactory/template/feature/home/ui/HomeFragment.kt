package mk.webfactory.template.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import mk.webfactory.template.databinding.FragmentHomeBinding
import javax.inject.Inject

class HomeFragment
@Inject constructor() : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel by viewModels()

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

//        viewModel.handleEvent(HomeEvent.OnStart)
    }

    override fun onStop() {
//        viewModel.handleEvent(HomeEvent.OnStop)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoadingIndicator(active: Boolean) {
        binding.circularProgressView.visibility = if (active) View.VISIBLE else View.GONE
    }

    fun observe() {
        // This is an example how to use МVVM pattern and observe the changes from the modelView
        viewModel.progressBarState.observe(
            viewLifecycleOwner,
            Observer {
                showLoadingIndicator(it)
            }
        )
    }

    fun setUpClickListeners() {
    }
}
