package com.example.capstoneproject.network.response

import com.example.capstoneproject.model.DataModel
import com.google.gson.annotations.SerializedName


data class GetMoviesResponse(
    @SerializedName("rank") val rank: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("big_image") val bigImage: String?,
    @SerializedName("genre") val genre: List<String>?,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("rating") val rating: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("year") val year: Int?,
    @SerializedName("imdbid") val imdbId: String?,
    @SerializedName("imdb_link") val imdbLink: String?,
)

fun GetMoviesResponse.toDataModel(): DataModel = DataModel(
    rank = rank,
    title = title,
    description = description,
    image = image,
    bigImage = bigImage,
    genre = genre,
    thumbnail = thumbnail,
    rating = rating,
    year = year.toString(),
    imdbId = imdbId,
    imdbLink = imdbLink
)


