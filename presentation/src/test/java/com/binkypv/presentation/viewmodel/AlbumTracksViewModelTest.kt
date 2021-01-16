package com.binkypv.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.binkypv.domain.model.AlbumDetailsModel
import com.binkypv.domain.model.AlbumModel
import com.binkypv.domain.model.AlbumsResultModel
import com.binkypv.domain.model.TrackModel
import com.binkypv.domain.repository.DeezerRepository
import com.binkypv.presentation.model.AlbumDetailsDisplay
import com.binkypv.presentation.model.AlbumListItem
import com.binkypv.presentation.model.AlbumLoadingItem
import com.binkypv.presentation.model.toDisplay
import com.binkypv.presentation.utils.CoroutinesRule
import com.bumptech.glide.load.HttpException
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.test.KoinTest

@RunWith(JUnit4::class)
class AlbumTracksViewModelTest: KoinTest {
    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = CoroutinesRule()

    private val repo: DeezerRepository = mockk()
    private val observer = mockk<Observer<AlbumDetailsDisplay>>(relaxed = true)
    private val viewmodel = AlbumTracksViewModel(repo)
    private val errorObserver = mockk<Observer<String>>(relaxed = true)
    private val loaderObserver = mockk<Observer<Unit>>(relaxed = true)

    @Before
    fun setup() {
        MockKAnnotations.init()
        viewmodel.results.observeForever(observer)
        viewmodel.error.observeForever(errorObserver)
        viewmodel.loading.observeForever(loaderObserver)
    }

    @Test
    fun given_album_when_detail_retrieval_triggered_then_show_result() {
        // given
        val model = AlbumDetailsModel(
            "coverUrl",
            "artist",
            "title",
            listOf(
                TrackModel(
                    "title",
                    "performers",
                    "id"
                )
            )
        )

        coEvery { repo.getAlbumDetails(any()) } returns model

        // when
        runBlocking {
            viewmodel.start("id")
        }

        // then
        coVerify(exactly = 1) { repo.getAlbumDetails("id") }
        verify { observer.onChanged(model.toDisplay()) }
    }

    @Test
    fun given_album_when_detail_retrieval_fails_then_show_error() {
        // given
        coEvery { repo.getAlbumDetails(any()) }.throws(HttpException("error"))

        // when
        runBlocking {
            viewmodel.start("id")
        }

        // then
        coVerify(exactly = 1) { repo.getAlbumDetails("id") }
        verify { errorObserver.onChanged("error") }
    }
}