package com.example.capstoneproject.repository

import android.content.Context
import com.example.capstoneproject.constants.Constants

class SharedPreferencesRepository(private val context: Context) {

    private val sharedPref = context.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)

    fun isFavorite(id: String): Boolean {
        return sharedPref.getBoolean(id, false)
    }

    fun toggleFavorite(id: String) {
        val isFavorited = isFavorite(id)

        with(sharedPref.edit()) {
            if (!isFavorited) {
                putBoolean(id, true)
                apply()
            } else {
                remove(id)
                apply()
            }
        }
    }

    fun getFavoriteIds(): Set<String> {
        return sharedPref.all.filterValues { it as? Boolean == true }.keys
    }
}