package com.binkypv.presentation.view.fragment

import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.binkypv.presentation.R
import com.binkypv.presentation.adapter.TracklistAdapter
import com.binkypv.presentation.databinding.FragmentAlbumBinding
import com.binkypv.presentation.viewmodel.AlbumTracksViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


class AlbumFragment : BaseFragment<FragmentAlbumBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAlbumBinding =
        FragmentAlbumBinding::inflate

    private val adapter: TracklistAdapter by lazy { TracklistAdapter() }
    private val albumTracksViewModel: AlbumTracksViewModel by viewModel()
    private val args: AlbumFragmentArgs by navArgs()

    private var isShow = true
    private var scrollRange = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        initObservers()
        albumTracksViewModel.start(args.albumId)
    }

    private fun initViews() {
        binding.albumTracklist.adapter = adapter
    }

    private fun initListeners() {
        binding.albumToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initObservers() {
        albumTracksViewModel.results.observe(viewLifecycleOwner, {
            adapter.submitList(it.tracklist)
            setCoverArt(it.coverUrl)
            binding.albumArtist.text = it.artist
            binding.albumTitle.text = it.title
            setCollapsingToolbarTitle(it.title)
        })

        albumTracksViewModel.error.observe(viewLifecycleOwner, {
            showError(it)
        })
    }

    private fun setCollapsingToolbarTitle(title: String) {
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener
        { _, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = binding.appBarLayout.totalScrollRange
            }
            if ((scrollRange + verticalOffset) == 0) {
                binding.albumCollapsingToolbar.title = title
                isShow = true
            } else if (isShow) {
                binding.albumCollapsingToolbar.title = " "
                isShow = false
            }
        })
    }

    private fun setCoverArt(imgUrl: String) {
        Glide.with(binding.albumCoverImage)
            .asBitmap()
            .load(imgUrl)
            .transition(BitmapTransitionOptions.withCrossFade())
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    setPlaceholderCoverArt()
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    setGradientBackground(resource)
                    return false
                }
            }).into(binding.albumCoverImage)
    }

    private fun setPlaceholderCoverArt() {
        binding.albumCoverImage.setImageDrawable(getDrawable(requireContext(),
            R.drawable.ic_album_placeholder))
    }

    private fun setGradientBackground(resource: Bitmap?) {
        resource?.let { bitmap ->
            Palette.Builder(bitmap).generate { img ->
                val dominantColor =
                    img?.getDominantColor(ContextCompat.getColor(
                        requireContext(),
                        R.color.background))
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(dominantColor ?: R.color.background, R.color.background))
                binding.albumCollapsingToolbar.background = gradient
                binding.albumCollapsingToolbar.setContentScrimColor(dominantColor
                    ?: R.color.background)
            }
        }
    }

    override fun onDestroyView() {
        binding.albumTracklist.adapter = null
        super.onDestroyView()
    }
}