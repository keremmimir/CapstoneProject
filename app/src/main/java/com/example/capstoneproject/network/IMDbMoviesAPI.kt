package com.example.capstoneproject.network

import com.example.capstoneproject.network.response.GetMoviesResponse
import com.example.capstoneproject.network.response.GetSeriesResponse
import retrofit2.Response
import retrofit2.http.GET

interface IMDbMoviesAPI {

    @GET("/")
    suspend fun getMovies(): List<GetMoviesResponse>

    @GET("series/")
    suspend fun getSeries(): List<GetSeriesResponse>
}