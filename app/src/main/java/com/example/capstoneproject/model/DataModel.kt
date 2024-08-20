package com.example.capstoneproject.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModel(
    val rank: Int?,
    val title: String?,
    val description: String?,
    val image: String?,
    val bigImage: String?,
    val genre: List<String>?,
    val thumbnail: String?,
    val rating: String?,
    val year: String?,
    val imdbId: String?,
    val imdbLink: String?,
) : Parcelable
