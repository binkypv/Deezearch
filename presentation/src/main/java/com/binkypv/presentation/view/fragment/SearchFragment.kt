package com.binkypv.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.binkypv.presentation.adapter.ArtistAdapter
import com.binkypv.presentation.databinding.FragmentSearchBinding
import com.binkypv.presentation.utils.ScrollPaginator
import com.binkypv.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment() {
    private lateinit var binding: FragmentSearchBinding
    private val adapter: ArtistAdapter by lazy { ArtistAdapter { id, name -> navigateToArtist(id, name) } }
    private val searchViewModel: SearchViewModel by viewModel()

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
        binding.searchArtistsList.adapter = adapter
        binding.searchArtistsList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        configurePaging(true)
    }

    private fun initListeners() {
        binding.searchButton.setOnClickListener {
            searchViewModel.retrieveArtists(binding.searchSearchBar.text.toString())
        }

        binding.searchSearchBar.doOnTextChanged { text, _, _, _ ->
            val term = text.toString()
            if(term.isNotBlank()) searchViewModel.retrieveArtists(term) else adapter.submitList(listOf())
        }
    }

    private fun initObservers() {
        searchViewModel.loading.observe(viewLifecycleOwner, {
//            showLoader()
        })

        searchViewModel.results.observe(viewLifecycleOwner, {
//            hideLoader()
            adapter.submitList(it)
        })

        searchViewModel.error.observe(viewLifecycleOwner, {
//            hideLoader()
            showError()
        })

        searchViewModel.next.observe(viewLifecycleOwner, {
            configurePaging(it != null)
        })
    }

    private fun navigateToArtist(id: String, name: String) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToArtistFragment(id, name))
    }

    private fun configurePaging(active: Boolean) {
        if (active) {
            binding.searchArtistsList.addOnScrollListener(object :
                ScrollPaginator(binding.searchArtistsList.layoutManager as LinearLayoutManager) {
                override fun loadMoreItems() {
                    searchViewModel.loadMore()
                }
            })
        }else{
            binding.searchArtistsList.clearOnScrollListeners()
        }
    }
}