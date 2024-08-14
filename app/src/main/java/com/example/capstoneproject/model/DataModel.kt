package com.example.capstoneproject.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModel(
    @SerializedName("rank") val rank: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("big_image") val bigImage: String?,
    @SerializedName("genre") val genre: List<String>?,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("rating") val rating: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("imdbid") val imdbId: String?,
    @SerializedName("imdb_link") val imdbLink: String?
) : Parcelable
