package com.binkypv.presentation.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.binkypv.presentation.adapter.ArtistAdapter
import com.binkypv.presentation.databinding.FragmentSearchBinding
import com.binkypv.presentation.utils.configurePaging
import com.binkypv.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val START_SEARCH_VIEW_FLIPPER_CHILD = 0
private const val NO_RESULTS_VIEW_FLIPPER_CHILD = 1
private const val RESULTS_VIEW_FLIPPER_CHILD = 2

private const val TEXT_SEARCH_DELAY = 1000L

class SearchFragment : BaseFragment() {
    private lateinit var binding: FragmentSearchBinding
    private val adapter: ArtistAdapter by lazy {
        ArtistAdapter { id, name ->
            navigateToArtist(id,
                name)
        }
    }
    private val searchViewModel: SearchViewModel by viewModel()
    private val searchHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() {
        binding.searchFlipper.displayedChild = START_SEARCH_VIEW_FLIPPER_CHILD
        binding.searchArtistsList.adapter = adapter
        binding.searchArtistsList.configurePaging(true) { searchViewModel.loadMore() }
    }

    private fun initListeners() {
        binding.searchSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(text: String?): Boolean {
                if (!text.isNullOrBlank()) {
                    searchHandler.postDelayed({
                        searchViewModel.retrieveArtists(text)
                    }, TEXT_SEARCH_DELAY)
                } else {
                    binding.searchFlipper.displayedChild = START_SEARCH_VIEW_FLIPPER_CHILD
                    adapter.submitList(emptyList())
                }
                return false
            }

        })
    }

    private fun initObservers() {
        searchViewModel.loading.observe(viewLifecycleOwner, {
//            showLoader()
        })

        searchViewModel.results.observe(viewLifecycleOwner, {
//            hideLoader()
            adapter.submitList(it)
            binding.searchFlipper.displayedChild =
                if (it.isNotEmpty()) RESULTS_VIEW_FLIPPER_CHILD else NO_RESULTS_VIEW_FLIPPER_CHILD
            binding.searchArtistsList.smoothScrollToPosition(0)
        })

        searchViewModel.error.observe(viewLifecycleOwner, {
//            hideLoader()
            showError()
        })

        searchViewModel.next.observe(viewLifecycleOwner, {
            binding.searchArtistsList.configurePaging(it != null) { searchViewModel.loadMore() }
        })
    }

    private fun navigateToArtist(id: String, name: String) {
        hideKeyboard()
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToArtistFragment(
            id,
            name))
    }
}