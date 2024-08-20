package com.example.capstoneproject.service

import com.example.capstoneproject.model.DataModel
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object IMDbMoviesAPIService {
    private val BASE_URL = "https://imdb-top-100-movies.p.rapidapi.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getIMDbAPI(): IMDbMoviesAPI {
        return retrofit.create(IMDbMoviesAPI::class.java)
    }
}