package com.example.myapplication.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MusicData {
    @SerializedName("feed")
    @Expose
    var feed: Feed? = null

    class Feed {
        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("author")
        @Expose
        var author: Author? = null

        @SerializedName("links")
        @Expose
        var links: List<Link>? = null

        @SerializedName("copyright")
        @Expose
        var copyright: String? = null

        @SerializedName("country")
        @Expose
        var country: String? = null

        @SerializedName("icon")
        @Expose
        var icon: String? = null

        @SerializedName("updated")
        @Expose
        var updated: String? = null

        @SerializedName("results")
        @Expose
        var musicResults: List<MusicResult>? = null
    }

    class Genre {
        @SerializedName("genreId")
        @Expose
        var genreId: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("url")
        @Expose
        var url: String? = null
    }

    class Link {
        @SerializedName("self")
        @Expose
        var self: String? = null
    }

    class MusicResult {
        @SerializedName("artistName")
        @Expose
        var artistName: String? = null

        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("releaseDate")
        @Expose
        var releaseDate: String? = null

        @SerializedName("kind")
        @Expose
        var kind: String? = null

        @SerializedName("artistId")
        @Expose
        var artistId: String? = null

        @SerializedName("artistUrl")
        @Expose
        var artistUrl: String? = null

        @SerializedName("artworkUrl100")
        @Expose
        var artworkUrl100: String? = null

        @SerializedName("genres")
        @Expose
        var genres: List<Genre>? = null

        @SerializedName("url")
        @Expose
        var url: String? = null

        @SerializedName("contentAdvisoryRating")
        @Expose
        var contentAdvisoryRating: String? = null
    }

    class Author {
        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("url")
        @Expose
        var url: String? = null
    }
}