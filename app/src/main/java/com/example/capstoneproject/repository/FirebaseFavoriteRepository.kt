package com.example.capstoneproject.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseFavoriteRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun addFavorite(userId: String, imdbId: String) {
        val favoriteRef = firestore.collection("users").document(userId)
            .collection("favorites").document(imdbId)

        favoriteRef.set(mapOf("itemId" to imdbId)).await()
    }

    suspend fun removeFavorite(userId: String, imdbId: String) {
        val favoriteRef = firestore.collection("users").document(userId)
            .collection("favorites").document(imdbId)

        favoriteRef.delete().await()
    }

    suspend fun isFavorite(userId: String, imdbId: String): Boolean {
        val favoriteRef = firestore.collection("users").document(userId)
            .collection("favorites").document(imdbId)

        return try {
            val document = favoriteRef.get().await()
            document.exists()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getFavoriteIds(userId: String): Set<String> {
        return try {
            val result = firestore.collection("users").document(userId)
                .collection("favorites").get().await()

            result.map { it.id }.toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }
}