package mk.webfactory.template.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
class HomeFragment @Inject constructor(): Fragment() {

    private var _binding: FragmentHomeBinding? = null

    val binding get() = _binding!!

    val viewModel by viewModels<HomeViewModel>()

    lateinit var itemViewAdapter: ShowItemViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupTabLayout()
        getPopularMovies() //todo save tab bar state and get saved tab content
        return binding.root
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

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    MOVIES_TAB_INDEX -> { getPopularMovies() }
                    TV_SHOWS_TAB_INDEX -> { getPopularTvShows() }
                    TRENDING_TAB_INDEX -> { getTrending() }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun getPopularMovies() {
        lifecycleScope.launch {
            viewModel.popularMovies.distinctUntilChanged().collectLatest {
                itemViewAdapter.submitData(it)
            }
        }
    }

    private fun getPopularTvShows() {
        lifecycleScope.launch {
            viewModel.popularTvShows.distinctUntilChanged().collectLatest {
                itemViewAdapter.submitData(it)
            }
        }
    }

    private fun getTrending() {
        lifecycleScope.launch {
            itemViewAdapter.submitData(lifecycle, PagingData.empty())
            viewModel.trendingShows.distinctUntilChanged().collectLatest {
                itemViewAdapter.submitData(it)
            }
        }
    }
}
