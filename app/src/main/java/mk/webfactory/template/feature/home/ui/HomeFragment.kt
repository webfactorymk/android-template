package mk.webfactory.template.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import mk.webfactory.template.databinding.FragmentHomeBinding
import mk.webfactory.template.feature.home.ui.adapter.ShowItemViewAdapter
import mk.webfactory.template.model.movie.Show
import javax.inject.Inject

const val MOVIES_TAB_INDEX: Int = 0
const val TV_SHOWS_TAB_INDEX: Int = 1
const val TRENDING_TAB_INDEX: Int = 2

@AndroidEntryPoint
class HomeFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    val binding get() = _binding!!

    val viewModel by viewModels<HomeViewModel>()

    lateinit var itemViewAdapter: ShowItemViewAdapter

    private var selectedTabIndex:Int = MOVIES_TAB_INDEX

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        (savedInstanceState?.getInt("selected_tab") ?: MOVIES_TAB_INDEX).apply {
            selectedTabIndex = this
            setupTabLayout(this)
            getContentForTab(this)
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selected_tab", selectedTabIndex)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerViewMovies
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ShowItemViewAdapter { onItemClicked(it) }.also { itemViewAdapter = it }
        recyclerView.setHasFixedSize(true)
    }

    private fun onItemClicked(show: Show) {
        //todo go to details page
    }

    private fun setupTabLayout(selectedTabIndex: Int) {
        binding.tabLayout.setScrollPosition(selectedTabIndex, 0f, true)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                this@HomeFragment.selectedTabIndex = tab.position
                getContentForTab(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        itemViewAdapter.addLoadStateListener { loadState ->
            binding.circularProgressView.isVisible = loadState.refresh is LoadState.Loading
            binding.txtError.isVisible = loadState.refresh is LoadState.Error
        }
    }

    private fun getContentForTab(selectedTabIndex: Int) {
        when (selectedTabIndex) {
            MOVIES_TAB_INDEX -> getPopularMovies()
            TV_SHOWS_TAB_INDEX -> getPopularTvShows()
            TRENDING_TAB_INDEX -> getTrending()
        }
    }

    private fun getPopularMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularMovies.distinctUntilChanged().collectLatest {
                itemViewAdapter.submitData(it)
            }
        }
    }

    private fun getPopularTvShows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularTvShows.distinctUntilChanged().collectLatest {
                itemViewAdapter.submitData(it)
            }
        }
    }

    private fun getTrending() {
        viewLifecycleOwner.lifecycleScope.launch {
            itemViewAdapter.submitData(lifecycle, PagingData.empty())
            viewModel.trendingShows.distinctUntilChanged().collectLatest {
                itemViewAdapter.submitData(it)
            }
        }
    }
}
