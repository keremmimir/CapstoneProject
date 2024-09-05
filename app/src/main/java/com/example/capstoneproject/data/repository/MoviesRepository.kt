package com.example.capstoneproject.data.repository

import com.example.capstoneproject.model.DataModel

interface MoviesRepository {
    suspend fun getMovies(): Result<List<DataModel>>
    suspend fun getSeries(): Result<List<DataModel>>
}