package com.binkypv.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.binkypv.presentation.R
import com.binkypv.presentation.adapter.AlbumsAdapter
import com.binkypv.presentation.databinding.FragmentArtistBinding
import com.binkypv.presentation.utils.GridSpacingItemDecoration
import com.binkypv.presentation.utils.configurePaging
import com.binkypv.presentation.viewmodel.ArtistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val NO_RESULTS_VIEW_FLIPPER_CHILD = 0
private const val RESULTS_VIEW_FLIPPER_CHILD = 1
private const val LOADING_VIEW_FLIPPER_CHILD = 2

class ArtistFragment : BaseFragment<FragmentArtistBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentArtistBinding =
        FragmentArtistBinding::inflate

    private val adapter: AlbumsAdapter by lazy { AlbumsAdapter { id -> navigateToAlbum(id) } }
    private val artistViewModel: ArtistViewModel by viewModel()
    private val args: ArtistFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        initObservers()
        artistViewModel.retrieveAlbums(args.artistId, args.artistName)
    }

    private fun initViews() {
        binding.artistAlbumList.layoutManager = GridLayoutManager(context, 2)
        binding.artistAlbumList.addItemDecoration(GridSpacingItemDecoration(2,
            resources.getDimensionPixelSize(R.dimen.small_space),
            true))
        binding.artistAlbumList.adapter = adapter
        binding.artistAlbumList.configurePaging(true) {
            artistViewModel.loadMore(args.artistId,
                args.artistName)
        }
        binding.artistFlipper.displayedChild = LOADING_VIEW_FLIPPER_CHILD
    }

    private fun initListeners() {
        binding.albumsToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initObservers() {
        artistViewModel.loading.observe(viewLifecycleOwner, {
            binding.artistFlipper.displayedChild = LOADING_VIEW_FLIPPER_CHILD
        })

        artistViewModel.results.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            binding.artistFlipper.displayedChild =
                if (it.isNotEmpty()) RESULTS_VIEW_FLIPPER_CHILD else NO_RESULTS_VIEW_FLIPPER_CHILD
        })

        artistViewModel.error.observe(viewLifecycleOwner, {
            binding.artistFlipper.displayedChild = NO_RESULTS_VIEW_FLIPPER_CHILD
            showError()
        })

        artistViewModel.next.observe(viewLifecycleOwner, {
            binding.artistAlbumList.configurePaging(it != null) {
                artistViewModel.loadMore(args.artistId,
                    args.artistName)
            }
        })
    }

    private fun navigateToAlbum(id: String) {
        findNavController().navigate(ArtistFragmentDirections.actionArtistFragmentToAlbumFragment(id))
    }
}