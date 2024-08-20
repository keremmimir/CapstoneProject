package com.example.capstoneproject.service

import com.example.capstoneproject.service.response.GetMoviesResponse
import com.example.capstoneproject.service.response.GetSeriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface IMDbMoviesAPI {

    @GET("/")
    @Headers(
        "x-rapidapi-key: c604dcefedmsh447e0047f439293p10f6cajsn805880157cc5",
        "x-rapidapi-host: imdb-top-100-movies.p.rapidapi.com"
    )
    suspend fun getMovies(): Response<List<GetMoviesResponse>>

    @GET("series/")
    @Headers(
        "x-rapidapi-key: c604dcefedmsh447e0047f439293p10f6cajsn805880157cc5",
        "x-rapidapi-host: imdb-top-100-movies.p.rapidapi.com"
    )
    suspend fun getSeries(): Response<List<GetSeriesResponse>>
}