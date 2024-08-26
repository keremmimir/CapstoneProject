package com.example.capstoneproject.repository

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseFavoriteRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun addFavorite(userId: String, imdbId: String) {
        val favoriteRef = firestore.collection("users").document(userId)
            .collection("favorites").document(imdbId)

        favoriteRef.set(mapOf("itemId" to imdbId))
    }

    fun removeFavorite(userId: String, imdbId: String) {
        val favoriteRef = firestore.collection("users").document(userId)
            .collection("favorites").document(imdbId)

        favoriteRef.delete()
    }

    fun isFavorite(userId: String, imdbId: String, callback: (Boolean) -> Unit) {
        val favoriteRef = firestore.collection("users").document(userId)
            .collection("favorites").document(imdbId)

        favoriteRef.get().addOnSuccessListener { document ->
            callback(document.exists())
        }
    }

    fun getFavoriteIds(userId: String, callback: (Set<String>) -> Unit) {
        firestore.collection("users").document(userId)
            .collection("favorites").get()
            .addOnSuccessListener { result ->
                val favoriteIds = result.map { it.id }.toSet()
                callback(favoriteIds)
            }
    }
}