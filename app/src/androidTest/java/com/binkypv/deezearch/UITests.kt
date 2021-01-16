package com.binkypv.deezearch

import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.binkypv.deezearch.utils.RecyclerViewMatcher
import com.binkypv.presentation.adapter.ArtistListItemViewHolder
import com.binkypv.presentation.model.*
import com.binkypv.presentation.view.activity.DeezearchActivity
import com.binkypv.presentation.viewmodel.AlbumTracksViewModel
import com.binkypv.presentation.viewmodel.ArtistViewModel
import com.binkypv.presentation.viewmodel.SearchViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

class UITests {
    private lateinit var scenario: ActivityScenario<DeezearchActivity>

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var artistViewModel: ArtistViewModel
    private lateinit var albumTracksViewModel: AlbumTracksViewModel

    private lateinit var next: MutableLiveData<Int?>
    private lateinit var artistResults: MutableLiveData<List<ArtistListItem>>
    private lateinit var albumsResults: MutableLiveData<List<AlbumListItem>>
    private lateinit var albumDetails: MutableLiveData<AlbumDetailsDisplay>

    private lateinit var module: Module

    @Before
    fun before() {
        MockKAnnotations.init(this)
        searchViewModel = mockk(relaxed = true)
        artistViewModel = mockk(relaxed = true)
        albumTracksViewModel = mockk(relaxed = true)

        next = MutableLiveData()
        artistResults = MutableLiveData()
        albumsResults = MutableLiveData()
        albumDetails = MutableLiveData()

        every { searchViewModel.next } returns next
        every { searchViewModel.results } returns artistResults
        every { artistViewModel.results } returns albumsResults
        every { albumTracksViewModel.results } returns albumDetails

        module = module(createdAtStart = true, override = true) {
            single { searchViewModel }
            single { artistViewModel }
            single { albumTracksViewModel }
        }

        loadKoinModules(module)
        scenario = launchActivity()
    }

    @After
    fun after() {
        scenario.close()
        unloadKoinModules(module)
    }

    @Test
    fun show_list_when_results_loaded() {
        artistResults.postValue(listOf(ArtistDisplay("name", "imageUrl", "id")))
        onView(withId(R.id.search_artists_list)).check(matches(isDisplayed()))
    }

    @Test
    fun navigate_to_artist_albums_screen() {
        artistResults.postValue(listOf(ArtistDisplay("name", "imageUrl", "id")))
        onView(withId(R.id.search_artists_list)).perform(RecyclerViewActions.actionOnItemAtPosition<ArtistListItemViewHolder>(
            0,
            click()))
        onView(withId(R.id.albums_header)).check(matches(isDisplayed()))
    }

    @Test
    fun navigate_to_album_details_show_correct_data() {
        artistResults.postValue(listOf(ArtistDisplay("name", "imageUrl", "id")))
        albumsResults.postValue(listOf(AlbumDisplay("name", "artist", "imageUrl", "id")))
        albumDetails.postValue(AlbumDetailsDisplay("coverUrl",
            "title",
            "artist",
            listOf(TrackDisplay("title", "performers", "id"))))
        onView(withId(R.id.search_artists_list)).perform(RecyclerViewActions.actionOnItemAtPosition<ArtistListItemViewHolder>(
            0,
            click()))
        onView(withId(R.id.artist_album_list)).perform(RecyclerViewActions.actionOnItemAtPosition<ArtistListItemViewHolder>(
            0,
            click()))
        onView(withId(R.id.album_title)).check(matches(ViewMatchers.withText("title")))
        onView(withId(R.id.album_artist)).check(matches(ViewMatchers.withText("artist")))
        onView(RecyclerViewMatcher(R.id.album_tracklist).atPosition(0)).check(matches(
            ViewMatchers.hasDescendant(ViewMatchers.withText("title"))))
        onView(RecyclerViewMatcher(R.id.album_tracklist).atPosition(0)).check(matches(
            ViewMatchers.hasDescendant(ViewMatchers.withText("1"))))
        onView(RecyclerViewMatcher(R.id.album_tracklist).atPosition(0)).check(matches(
            ViewMatchers.hasDescendant(ViewMatchers.withText("performers"))))
    }

    @Test
    fun show_albums_list_when_results_loaded() {
        artistResults.postValue(listOf(ArtistDisplay("name", "imageUrl", "id")))
        albumsResults.postValue(listOf(AlbumDisplay("name", "artist", "imageUrl", "id")))
        onView(withId(R.id.search_artists_list)).perform(RecyclerViewActions.actionOnItemAtPosition<ArtistListItemViewHolder>(
            0,
            click()))
        onView(withId(R.id.artist_album_list)).check(matches(isDisplayed()))
        onView(RecyclerViewMatcher(R.id.artist_album_list).atPosition(0)).check(matches(
            ViewMatchers.hasDescendant(ViewMatchers.withText("name"))))
    }

    @Test
    fun show_correct_artist_results() {
        artistResults.postValue(listOf(ArtistDisplay("name", "imageUrl", "id")))
        onView(RecyclerViewMatcher(R.id.search_artists_list).atPosition(0)).check(matches(
            ViewMatchers.hasDescendant(ViewMatchers.withText("name"))))
    }

    @Test
    fun hide_list_when_no_results() {
        artistResults.postValue(emptyList())
        onView(withId(R.id.search_artists_list)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_no_results_text)).check(matches(isDisplayed()))
        onView(withId(R.id.search_no_results_image)).check(matches(isDisplayed()))
    }

    @Test
    fun show_start_image_when_entering() {
        onView(withId(R.id.search_start_image)).check(matches(isDisplayed()))
        onView(withId(R.id.search_start_text)).check(matches(isDisplayed()))
    }

}