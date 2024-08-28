package com.example.capstoneproject.service

import com.example.capstoneproject.service.response.GetMoviesResponse
import com.example.capstoneproject.service.response.GetSeriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface IMDbMoviesAPI {

    @GET("/")
    suspend fun getMovies(): Response<List<GetMoviesResponse>>

    @GET("series/")
    suspend fun getSeries(): Response<List<GetSeriesResponse>>
}