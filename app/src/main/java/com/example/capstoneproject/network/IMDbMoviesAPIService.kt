package com.example.capstoneproject.network

import com.example.capstoneproject.constants.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object IMDbMoviesAPIService {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(ApiClient().getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getIMDbAPI(): IMDbMoviesAPI {
        return retrofit.create(IMDbMoviesAPI::class.java)
    }
}