package com.example.capstoneproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.repository.SharedPreferencesRepository

class ListViewModelFactory(private val sharedPreferencesRepository: SharedPreferencesRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(sharedPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}