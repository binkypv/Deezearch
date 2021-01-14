package com.binkypv.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.binkypv.presentation.adapter.TracklistAdapter
import com.binkypv.presentation.databinding.FragmentAlbumBinding
import com.binkypv.presentation.utils.configurePaging
import com.binkypv.presentation.viewmodel.AlbumTracksViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumFragment : BaseFragment() {
    private lateinit var binding: FragmentAlbumBinding
    private val adapter: TracklistAdapter by lazy { TracklistAdapter() }
    private val albumTracksViewModel: AlbumTracksViewModel by viewModel()
    private val args: AlbumFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        initObservers()
        albumTracksViewModel.retrieveAlbumTracks(args.albumId)
    }

    private fun initViews(){
        binding.albumTracklist.adapter = adapter
    }

    private fun initListeners() {
        binding.albumToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initObservers() {
        albumTracksViewModel.loading.observe(viewLifecycleOwner, {
//            showLoader()
        })

        albumTracksViewModel.results.observe(viewLifecycleOwner, {
//            hideLoader()
            adapter.submitList(it.tracklist)

            Glide.with(binding.albumCoverImage)
                .load(it.coverUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.albumCoverImage)

            binding.albumArtist.text = it.artist
            binding.albumTitle.text = it.title
        })

        albumTracksViewModel.error.observe(viewLifecycleOwner, {
//            hideLoader()
            showError()
        })
    }
}