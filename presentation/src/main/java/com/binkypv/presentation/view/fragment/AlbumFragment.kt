package com.binkypv.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.binkypv.presentation.R
import com.binkypv.presentation.adapter.AlbumsAdapter
import com.binkypv.presentation.databinding.FragmentArtistBinding
import com.binkypv.presentation.utils.GridSpacingItemDecoration
import com.binkypv.presentation.viewmodel.ArtistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumFragment : BaseFragment() {
    private lateinit var binding: FragmentArtistBinding
    private val adapter: AlbumsAdapter by lazy { AlbumsAdapter { id -> navigateToAlbum(id) } }
    private val searchViewModel: ArtistViewModel by viewModel()
    private val args: ArtistFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentArtistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
        searchViewModel.retrieveAlbums(args.artistId, args.artistName)
    }

    private fun initViews() {
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setDisplayShowHomeEnabled(true)

        val toolbar = activity?.actionBar as Toolbar
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.artistAlbumList.layoutManager = GridLayoutManager(context, 2)
        binding.artistAlbumList.addItemDecoration(GridSpacingItemDecoration(2,
            resources.getDimensionPixelSize(R.dimen.small_space),
            true))
        binding.artistAlbumList.adapter = adapter
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
    }

    private fun navigateToAlbum(id: String) {

    }
}