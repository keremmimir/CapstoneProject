package com.example.capstoneproject.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.MoviesRepository
import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.data.repository.MoviesRepositoryImpl
import com.example.capstoneproject.model.Type
import kotlinx.coroutines.launch

class ListViewModel :
    ViewModel() {
    private val moviesRepository: MoviesRepository = MoviesRepositoryImpl()
    val movies = MutableLiveData<List<DataModel>>()
    val series = MutableLiveData<List<DataModel>>()
    val isLoading = MutableLiveData<Boolean>()
    val filteredItems = MutableLiveData<List<DataModel>>()

    private val moviesList = mutableListOf<DataModel>()
    private val seriesList = mutableListOf<DataModel>()

    fun fetchData(type: Type) {
        when (type) {
            Type.MOVIES -> getMovies()
            Type.SERIES -> getSeries()
        }
    }

    private fun getMovies() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val result = moviesRepository.getMovies()
                moviesList.clear()
                moviesList.addAll(result)
                movies.value = moviesList
            } catch (e: Exception) {

            } finally {
                isLoading.value = false
            }
        }
    }

    private fun getSeries() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val result = moviesRepository.getSeries()
                seriesList.clear()
                seriesList.addAll(result)
                series.value = seriesList
            } catch (e: Exception) {

            } finally {
                isLoading.value = false
            }
        }
    }

    fun updateMoviesWithFavorites(favoriteIds: Set<String>) {
        movies.value = movies.value?.map { movie ->
            movie.copy(isFavorite = favoriteIds.contains(movie.imdbId))
        }
    }

    fun updateSeriesWithFavorites(favoriteIds: Set<String>) {
        series.value = series.value?.map { series ->
            series.copy(isFavorite = favoriteIds.contains(series.imdbId))
        }
    }

    fun searchItems(query: String) {
        val combinedList = (movies.value ?: emptyList()) + (series.value ?: emptyList())
        val filteredList = combinedList.filter {
            it.title?.contains(query, ignoreCase = true) ?: false
        }
        filteredItems.value = filteredList
    }
}

