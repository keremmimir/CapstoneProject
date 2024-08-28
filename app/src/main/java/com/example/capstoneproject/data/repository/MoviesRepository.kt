package com.example.capstoneproject.data.repository

import com.example.capstoneproject.model.DataModel

interface MoviesRepository {
    suspend fun getMovies(): List<DataModel>
    suspend fun getSeries(): List<DataModel>
}