package com.example.capstoneproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.repository.SharedPreferencesRepository
import com.example.capstoneproject.service.IMDbMoviesAPI
import com.example.capstoneproject.service.IMDbMoviesAPIService
import com.example.capstoneproject.service.response.toDataModel
import kotlinx.coroutines.launch

class ListViewModel(private val sharedPreferencesRepository: SharedPreferencesRepository) : ViewModel() {
    private val imdbMoviesAPI: IMDbMoviesAPI = IMDbMoviesAPIService.getIMDbAPI()
    val movies = MutableLiveData<List<DataModel>>()
    val series = MutableLiveData<List<DataModel>>()
    val favorites = MutableLiveData<List<DataModel>>()
    val error = MutableLiveData<String>()

    init {
        getSeries()
        getMovies()
    }
    fun getMovies(){
        if (movies.value.isNullOrEmpty()){
            viewModelScope.launch {
                try {
                    val response = imdbMoviesAPI.getMovies()
                    if (response.isSuccessful){
                        movies.value = response.body()?.map { it.toDataModel() }
                        updateFavorites()
                    }else{
                        error.value = "Error : ${response.message()}"
                    }
                }catch (e : Exception){
                    error.value = e.message
                }
            }
        }
    }

    fun getSeries(){
        if (series.value.isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val response = imdbMoviesAPI.getSeries()
                    if (response.isSuccessful){
                        series.value = response.body()?.map { it.toDataModel() }
                        updateFavorites()
                    }else{
                        error.value = "Error : ${response.message()}"
                    }
                }catch (e : Exception){
                    error.value = e.message
                }
            }
        }
    }

    fun toggleFavorite(dataModel: DataModel) {
        dataModel.imdbId?.let {
            sharedPreferencesRepository.toggleFavorite(it)
            updateFavorites()
        }
    }

    fun updateFavorites() {
        val favoriteIds = sharedPreferencesRepository.getFavoriteIds()
        val combinedList = (movies.value ?: emptyList()) + (series.value ?: emptyList())
        val favoriteList = combinedList.filter { it.imdbId in favoriteIds }
        favorites.value = favoriteList
    }
}