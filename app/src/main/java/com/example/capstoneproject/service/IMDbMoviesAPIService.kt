package com.example.capstoneproject.service

import com.example.capstoneproject.model.DataModel
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object IMDbMoviesAPIService {
    private val BASE_URL = "https://imdb-top-100-movies.p.rapidapi.com/"

    private val retrofit: Retrofit by lazy {
        val gson = GsonBuilder()
            .registerTypeAdapter(DataModel::class.java, ShowDeserializer())
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    fun getIMDbAPI(): IMDbMoviesAPI {
        return retrofit.create(IMDbMoviesAPI::class.java)
    }
}