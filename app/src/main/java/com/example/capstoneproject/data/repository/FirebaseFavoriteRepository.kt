package com.example.capstoneproject.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFavoriteRepository @Inject constructor(
    private val firestore: FirebaseFirestore
)  {

    // Favori ekleme
    suspend fun addFavorite(userId: String, imdbId: String) {
        val favoriteRef = firestore.collection("users").document(userId)
            .collection("favorites").document(imdbId)

        favoriteRef.set(mapOf("itemId" to imdbId)).await()
    }

    // Favori kaldırma
    suspend fun removeFavorite(userId: String, imdbId: String) {
        val favoriteRef = firestore.collection("users").document(userId)
            .collection("favorites").document(imdbId)

        favoriteRef.delete().await()
    }

    // Favori durumu kontrol etme
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

    // Tüm favori öğelerinin IMDb ID'lerini getirme
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