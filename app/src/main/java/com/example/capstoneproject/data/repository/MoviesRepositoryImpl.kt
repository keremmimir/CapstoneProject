package com.example.capstoneproject.data.repository

import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.network.IMDbMoviesAPI
import com.example.capstoneproject.network.response.toDataModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val imdbMoviesAPI: IMDbMoviesAPI
) : MoviesRepository {

    override suspend fun getMovies(): Result<List<DataModel>> {
        return try {
            val movies = imdbMoviesAPI.getMovies().map { it.toDataModel() }
            Result.success(movies)
        } catch (e: HttpException) {
            val errorMessage = "HTTP Error: ${e.code()} ${e.message()}"
            Result.failure(Exception(errorMessage))
        } catch (e: IOException) {
            Result.failure(Exception("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSeries(): Result<List<DataModel>> {
        return try {
            val series = imdbMoviesAPI.getSeries().map { it.toDataModel() }
            Result.success(series)
        } catch (e: HttpException) {
            val errorMessage = "HTTP Error: ${e.code()} ${e.message()}"
            Result.failure(Exception(errorMessage))
        } catch (e: IOException) {
            Result.failure(Exception("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}