package com.example.capstoneproject.data.repository

import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.network.IMDbMoviesAPI
import com.example.capstoneproject.network.IMDbMoviesAPIService
import com.example.capstoneproject.network.response.toDataModel

class MoviesRepositoryImpl : MoviesRepository {

    private val imdbMoviesAPI: IMDbMoviesAPI = IMDbMoviesAPIService.getIMDbAPI()

    override suspend fun getMovies(): List<DataModel> {
        return try {
            imdbMoviesAPI.getMovies().map { it.toDataModel() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getSeries(): List<DataModel> {
        return try {
            imdbMoviesAPI.getSeries().map { it.toDataModel() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}