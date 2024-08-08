package com.example.capstoneproject.Service

import com.example.capstoneproject.Model.DataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface IMDbMoviesAPI {

    @GET("/")
    @Headers(
        "x-rapidapi-key: c604dcefedmsh447e0047f439293p10f6cajsn805880157cc5",
        "x-rapidapi-host: imdb-top-100-movies.p.rapidapi.com"
    )
    suspend fun getMovies(): Response<List<DataModel>>

    @GET("series/")
    @Headers(
        "x-rapidapi-key: c604dcefedmsh447e0047f439293p10f6cajsn805880157cc5",
        "x-rapidapi-host: imdb-top-100-movies.p.rapidapi.com"
    )
    suspend fun getSeries(): Response<List<DataModel>>
}