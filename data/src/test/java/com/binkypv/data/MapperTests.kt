package com.binkypv.data

import com.binkypv.data.model.*
import com.binkypv.domain.model.*
import org.junit.Test

class MapperTests {

    @Test
    fun map_artist_entity_to_artist_model() {
        //given
        val entity = ArtistSearchResultEntity(
            listOf(
                ArtistEntity("id",
                    "name",
                    "link",
                    "picture",
                    "pictureSmall",
                    "pictureMedium",
                    "pictureBig",
                    "pictureXL",
                    0,
                    0,
                    false,
                    "tracklist",
                    "type")
            ),
            1,
            "http://api.deezer.com/whatever?next=25",
            null
        )
        val expected = SearchResultModel(
            25,
            listOf(
                ArtistModel(
                    "name", "picture", "id"
                )
            )
        )

        //when
        val actual = entity.toDomain()

        //then
        assert(actual == expected)
    }

    @Test
    fun map_album_detail_entity_to_album_detail_model() {
        //given
        val entity = AlbumDetailsEntity(
            "id",
            "title",
            "upc",
            "link",
            "share",
            "cover",
            "coverSmall",
            "coverMedium",
            "coverBig",
            "coverXL",
            "md5image",
            "genreId",
            GenresWrapperEntity(listOf(GenreEntity("id", "name", "picture", "type"))),
            "label",
            0,
            0,
            0,
            0,
            "releaseDate",
            "recordType",
            false,
            "tracklist",
            false,
            0,
            0,
            listOf(ContributorEntity("id",
                "name",
                "link",
                "share",
                "picture",
                "pictureSmall",
                "pictureMedium",
                "pictureBig",
                "pictureXL",
                false,
                "tracklist",
                "type",
                "role")),
            AlbumArtistEntity("id",
                "name",
                "picture",
                "pictureSmall",
                "pictureMedium",
                "pictureBig",
                "pictureXL",
                "tracklist",
                "type"),
            "type",
            TracksWrapperEntity(listOf(TrackEntity("id",
                true,
                "title",
                "titleShort",
                "titleVersion",
                "link",
                0,
                0,
                false,
                0,
                0,
                "preview",
                "md5image",
                "type",
                TrackArtistEntity("id", "name", "tracklist", "type"))))
        )
        val expected = AlbumDetailsModel(
            "coverBig","title","name",listOf(TrackModel("title","name","id"))
        )

        //when
        val actual = entity.toDomain()

        //then
        assert(actual == expected)
    }

    @Test
    fun map_album_entity_to_album_model() {
        //given
        val entity = ArtistAlbumsEntity(
            listOf(
                AlbumEntity("id",
                    "title",
                    "link",
                    "cover",
                    "coverSmall",
                    "coverMedium",
                    "coverBig",
                    "coverXL",
                    "md5image",
                    "genreId",
                    0,
                    "releaseDate",
                    "recordType",
                    "tracklist",
                    false,
                    "type")
            ),
            "http://api.deezer.com/whatever?next=25",
            null
        )
        val expected = AlbumsResultModel(
            25,
            listOf(
                AlbumModel(
                    "title", "artist", "coverMedium", "id"
                )
            )
        )

        //when
        val actual = entity.toDomain("artist")

        //then
        assert(actual == expected)
    }

}