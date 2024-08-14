package com.example.capstoneproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.service.IMDbMoviesAPI
import com.example.capstoneproject.service.IMDbMoviesAPIService
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val imdbMoviesAPI: IMDbMoviesAPI = IMDbMoviesAPIService.getIMDbAPI()
    val movies = MutableLiveData<List<DataModel>>()
    val error = MutableLiveData<String>()

    fun getMovies(){
        viewModelScope.launch {
            try {
                val response = imdbMoviesAPI.getMovies()
                if (response.isSuccessful){
                    movies.value = response.body()
                }else{
                    error.value = "Error : ${response.message()}"
                }
            }catch (e : Exception){
                error.value = e.message
            }
        }
    }

    fun getSeries(){
        viewModelScope.launch {
            try {
                val response = imdbMoviesAPI.getSeries()
                if (response.isSuccessful){
                    movies.value = response.body()
                }else{
                    error.value = "Error : ${response.message()}"
                }
            }catch (e : Exception){
                error.value = e.message
            }
        }
    }
}