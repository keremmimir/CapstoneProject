package com.example.capstoneproject

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ApiError(
    val code: String,
    val message: String,
    val title: String
) : Parcelable