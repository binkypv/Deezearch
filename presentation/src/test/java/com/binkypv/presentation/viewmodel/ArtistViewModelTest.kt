package com.binkypv.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.binkypv.domain.model.AlbumModel
import com.binkypv.domain.model.AlbumsResultModel
import com.binkypv.domain.repository.DeezerRepository
import com.binkypv.presentation.model.AlbumListItem
import com.binkypv.presentation.model.AlbumLoadingItem
import com.binkypv.presentation.model.toDisplay
import com.binkypv.presentation.utils.CoroutinesRule
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
class ArtistViewModelTest : KoinTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = CoroutinesRule()

    private val repo: DeezerRepository = mockk()
    private val observer = mockk<Observer<List<AlbumListItem>>>(relaxed = true)
    private val viewmodel = ArtistViewModel(repo)

    @Before
    fun setup() {
        MockKAnnotations.init()
        viewmodel.results.observeForever(observer)
    }

    @Test
    fun given_single_page_result_when_album_retrieval_is_triggered_then_show_results() {
        // given
        val model = AlbumsResultModel(
            null,
            listOf(
                AlbumModel(
                    "name",
                    "artist",
                    "imageUrl",
                    "id"
                )
            )
        )

        coEvery { repo.getAlbums(any(), any(), any()) } returns model

        // when
        runBlocking {
            viewmodel.retrieveAlbums("id", "name")
        }

        // then
        coVerify(exactly = 1) { repo.getAlbums("id", "name") }
        verify { observer.onChanged(model.albums.map { it.toDisplay() }) }
    }

    @Test
    fun given_several_page_result_when_album_retrieval_is_triggered_then_show_loader() {
        // given
        val model = AlbumsResultModel(
            25,
            listOf(
                AlbumModel(
                    "name",
                    "artist",
                    "imageUrl",
                    "id"
                )
            )
        )
        val result: List<AlbumListItem> = mutableListOf<AlbumListItem>().apply {
            addAll(
                model.albums.map { it.toDisplay() })
            add(AlbumLoadingItem)
        }.toList()
        coEvery { repo.getAlbums(any(), any()) } returns model

        // when
        runBlocking {
            viewmodel.retrieveAlbums("id", "name")
        }

        // then
        coVerify(exactly = 1) { repo.getAlbums("id", "name") }
        verify { observer.onChanged(result) }
    }

    @Test
    fun given_several_page_result_when_load_next_page_then_fetch_next_index() {
        // given
        val model = AlbumsResultModel(
            25,
            listOf(
                AlbumModel(
                    "name",
                    "artist",
                    "imageUrl",
                    "id"
                )
            )
        )
        coEvery { repo.getAlbums(any(), any()) } returns model

        // when
        runBlocking {
            viewmodel.retrieveAlbums("id", "name")
            viewmodel.loadMore("id", "name")
        }

        // then
        coVerify(exactly = 1) { repo.getAlbums("id", "name", 25) }
    }
}