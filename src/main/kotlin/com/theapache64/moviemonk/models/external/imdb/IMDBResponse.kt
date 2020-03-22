package com.theapache64.moviemonk.models.external.imdb

import com.google.gson.annotations.SerializedName

data class IMDBResponse(
    @SerializedName("actor")
    val actor: Any?,
    @SerializedName("aggregateRating")
    val aggregateRating: AggregateRating?,
    @SerializedName("contentRating")
    val contentRating: String?, // PG
    @SerializedName("@context")
    val context: String?, // http://schema.org
    @SerializedName("creator")
    val creator: Any?,
    @SerializedName("datePublished")
    val datePublished: String?, // 2019-01-16
    @SerializedName("description")
    val description: String?, // The Kid Who Would Be King is a movie starring Louis Ashbourne Serkis, Denise Gough, and Dean Chaumoo. A band of kids embark on an epic quest to thwart a medieval menace.
    @SerializedName("director")
    val director: Any?,
    @SerializedName("duration")
    val duration: String?, // PT2H
    @SerializedName("genre")
    val genre: Any?,
    @SerializedName("image")
    val image: String?, // https://m.media-amazon.com/images/M/MV5BZjk1ZDE0ZDMtYmIxOC00ZmIwLThiOWQtMDFmYjc5ZjQzYTNlXkEyXkFqcGdeQXVyNjIxMTQ4OTc@._V1_.jpg
    @SerializedName("keywords")
    val keywords: String?, // reference to led zeppelin,close up of eye,evil woman,good versus evil,final battle
    @SerializedName("name")
    val name: String?, // The Kid Who Would Be King
    @SerializedName("review")
    val review: Review?,
    @SerializedName("trailer")
    val trailer: Trailer?,
    @SerializedName("@type")
    val type: String?, // Movie
    @SerializedName("url")
    val url: String? // /title/tt6811018/
) {
    data class Actor(
        @SerializedName("name")
        val name: String?, // Tom Taylor
        @SerializedName("@type")
        val type: String?, // Person
        @SerializedName("url")
        val url: String? // /name/nm6999211/
    )

    data class AggregateRating(
        @SerializedName("bestRating")
        val bestRating: String?, // 10.0
        @SerializedName("ratingCount")
        val ratingCount: Long?, // 12758
        @SerializedName("ratingValue")
        val ratingValue: String?, // 6.0
        @SerializedName("@type")
        val type: String?, // AggregateRating
        @SerializedName("worstRating")
        val worstRating: String? // 1.0
    )

    data class Creator(
        @SerializedName("name")
        val name: String?, // Joe Cornish
        @SerializedName("@type")
        val type: String?, // Organization
        @SerializedName("url")
        val url: String? // /company/co0057311/
    )

    data class Director(
        @SerializedName("name")
        val name: String?, // Joe Cornish
        @SerializedName("@type")
        val type: String?, // Person
        @SerializedName("url")
        val url: String? // /name/nm0180428/
    )

    data class Review(
        @SerializedName("author")
        val author: Author?,
        @SerializedName("dateCreated")
        val dateCreated: String?, // 2019-05-04
        @SerializedName("inLanguage")
        val inLanguage: String?, // English
        @SerializedName("itemReviewed")
        val itemReviewed: ItemReviewed?,
        @SerializedName("name")
        val name: String?, // Surprisingly good
        @SerializedName("reviewBody")
        val reviewBody: String?, // Went with my husband, 7 and 12 year old boys. We all enjoyed it. A bit scary for the seven year old in parts. Yes it's a bit cheesy but has a lovely message, genuinely funny and moving in parts. Great CGI. We'd watch it again and recommend it
        @SerializedName("reviewRating")
        val reviewRating: ReviewRating?,
        @SerializedName("@type")
        val type: String? // Review
    ) {
        data class Author(
            @SerializedName("name")
            val name: String?, // clairehaywardot
            @SerializedName("@type")
            val type: String? // Person
        )

        data class ItemReviewed(
            @SerializedName("@type")
            val type: String?, // CreativeWork
            @SerializedName("url")
            val url: String? // /title/tt6811018/
        )

        data class ReviewRating(
            @SerializedName("bestRating")
            val bestRating: String?, // 10
            @SerializedName("ratingValue")
            val ratingValue: String?, // 8
            @SerializedName("@type")
            val type: String?, // Rating
            @SerializedName("worstRating")
            val worstRating: String? // 1
        )
    }

    data class Trailer(
        @SerializedName("description")
        val description: String?, // Alex (Ashbourne Serkis) thinks he's just another nobody, until he stumbles upon the mythical Sword in the Stone, Excalibur. Now, he must unite his friends and enemies into a band of knights and, together with the legendary wizard Merlin (Patrick Stewart), take on the wicked enchantress Morgana (Rebecca Ferguson). With the future at stake, Alex must become the great leader he never dreamed he could be.
        @SerializedName("embedUrl")
        val embedUrl: String?, // /video/imdb/vi2788866585
        @SerializedName("name")
        val name: String?, // Official Trailer
        @SerializedName("thumbnail")
        val thumbnail: Thumbnail?,
        @SerializedName("thumbnailUrl")
        val thumbnailUrl: String?, // https://m.media-amazon.com/images/M/MV5BZjY2MmQ4ZTktMDJiMi00ZTY0LTgzODItNzIwYmFjMjVlYWRjXkEyXkFqcGdeQW1yb3NzZXI@._V1_.jpg
        @SerializedName("@type")
        val type: String?, // VideoObject
        @SerializedName("uploadDate")
        val uploadDate: String? // 2018-12-27T14:27:46Z
    ) {
        data class Thumbnail(
            @SerializedName("contentUrl")
            val contentUrl: String?, // https://m.media-amazon.com/images/M/MV5BZjY2MmQ4ZTktMDJiMi00ZTY0LTgzODItNzIwYmFjMjVlYWRjXkEyXkFqcGdeQW1yb3NzZXI@._V1_.jpg
            @SerializedName("@type")
            val type: String? // ImageObject
        )
    }
}