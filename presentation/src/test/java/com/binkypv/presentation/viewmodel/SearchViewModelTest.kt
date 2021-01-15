package com.binkypv.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.binkypv.domain.model.ArtistModel
import com.binkypv.domain.model.SearchResultModel
import com.binkypv.domain.repository.DeezerRepository
import com.binkypv.presentation.model.ArtistListItem
import com.binkypv.presentation.model.ArtistLoadingItem
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
class SearchViewModelTest : KoinTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = CoroutinesRule()

    private val repo: DeezerRepository = mockk()
    private val observer = mockk<Observer<List<ArtistListItem>>>(relaxed = true)
    private val viewmodel = SearchViewModel(repo)

    @Before
    fun setup() {
        MockKAnnotations.init()
        viewmodel.results.observeForever(observer)
    }

    @Test
    fun given_single_page_result_when_artist_search_is_triggered_then_show_results() {
        // given
        val model = SearchResultModel(
            null,
            listOf(
                ArtistModel(
                    "name",
                    "imageUrl",
                    "id"
                )
            )
        )

        coEvery { repo.getArtists(any(), any()) } returns model

        // when
        runBlocking {
            viewmodel.onSearchTermChanged("name")
        }

        // then
        coVerify(exactly = 1) { repo.getArtists("name", null) }
        verify { observer.onChanged(model.artists.map { it.toDisplay() }) }
    }

    @Test
    fun given_several_page_result_when_artist_search_is_triggered_then_show_loader() {
        // given
        val model = SearchResultModel(
            25,
            listOf(
                ArtistModel(
                    "name",
                    "imageUrl",
                    "id"
                )
            )
        )
        val result: List<ArtistListItem> = mutableListOf<ArtistListItem>().apply {
            addAll(
                model.artists.map { it.toDisplay() })
            add(ArtistLoadingItem)
        }.toList()
        coEvery { repo.getArtists(any(), any()) } returns model

        // when
        runBlocking {
            viewmodel.onSearchTermChanged("name")
        }

        // then
        coVerify(exactly = 1) { repo.getArtists("name", null) }
        verify { observer.onChanged(result) }
    }

    @Test
    fun given_several_page_result_when_load_next_artist_page_then_fetch_next_index() {
        // given
        val model = SearchResultModel(
            25,
            listOf(
                ArtistModel(
                    "name",
                    "imageUrl",
                    "id"
                )
            )
        )
        coEvery { repo.getArtists(any(), any()) } returns model

        // when
        runBlocking {
            viewmodel.onSearchTermChanged("name")
            viewmodel.loadMore()
        }

        // then
        coVerify(exactly = 1) { repo.getArtists("name", 25) }
    }
}