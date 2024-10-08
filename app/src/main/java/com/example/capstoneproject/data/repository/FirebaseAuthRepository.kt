package com.example.capstoneproject.data.repository

import com.example.capstoneproject.constants.Constants
import com.example.capstoneproject.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun signUp(
        name: String,
        surname: String,
        email: String,
        password: String
    ): Result<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("UID is null")

            val user =
                User(uid = uid, name = name, surname = surname, email = email, password = password)

            firestore.collection("users").document(uid).set(user).await()
            Result.success(Constants.SIGN_UP_RESULT)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Constants.SIGN_IN_RESULT)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut(): Result<String> {
        return try {
            auth.signOut()
            Result.success(Constants.LOGOUT_RESULT)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}