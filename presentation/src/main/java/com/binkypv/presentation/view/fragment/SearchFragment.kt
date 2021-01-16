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
private const val LOADING_VIEW_FLIPPER_CHILD = 3

private const val TEXT_SEARCH_DELAY = 500L

class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding =
        FragmentSearchBinding::inflate

    private val adapter: ArtistAdapter by lazy {
        ArtistAdapter { id, name ->
            navigateToArtist(id,
                name)
        }
    }
    private val searchViewModel: SearchViewModel by viewModel()
    private val searchHandler = Handler(Looper.getMainLooper())

    override fun onResume() {
        super.onResume()
        initListeners()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
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
                searchHandler.removeCallbacksAndMessages(null)
                if (!text.isNullOrBlank()) {
                    searchHandler.postDelayed({
                        adapter.submitList(emptyList())
                        searchViewModel.onSearchTermChanged(text)
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
            binding.searchFlipper.displayedChild = LOADING_VIEW_FLIPPER_CHILD
        })

        searchViewModel.results.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            binding.searchFlipper.displayedChild =
                if (it.isNotEmpty()) RESULTS_VIEW_FLIPPER_CHILD else NO_RESULTS_VIEW_FLIPPER_CHILD
        })

        searchViewModel.error.observe(viewLifecycleOwner, {
            binding.searchFlipper.displayedChild = NO_RESULTS_VIEW_FLIPPER_CHILD
            showError(it)
        })

        searchViewModel.next.observe(viewLifecycleOwner, {
            binding.searchArtistsList.configurePaging(it != null) { searchViewModel.loadMore() }
        })
    }

    override fun onPause() {
        super.onPause()
        clearListeners()
    }

    override fun onDestroyView() {
        binding.searchArtistsList.adapter = null
        super.onDestroyView()
    }

    private fun clearListeners() {
        binding.searchSearchBar.setOnQueryTextListener(null)
    }

    private fun navigateToArtist(id: String, name: String) {
        hideKeyboard()
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToArtistFragment(
            id,
            name))
    }
}