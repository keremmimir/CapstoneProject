package com.example.capstoneproject.data.repository

import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.network.IMDbMoviesAPI
import com.example.capstoneproject.network.IMDbMoviesAPIService
import com.example.capstoneproject.network.response.toDataModel

class MoviesRepositoryImpl : MoviesRepository {

    private val imdbMoviesAPI: IMDbMoviesAPI = IMDbMoviesAPIService.getIMDbAPI()

    override suspend fun getMovies(): Result<List<DataModel>> {
        return try {
            val movies = imdbMoviesAPI.getMovies().map { it.toDataModel() }
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSeries(): Result<List<DataModel>> {
        return try {
            val series = imdbMoviesAPI.getSeries().map { it.toDataModel() }
            Result.success(series)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}