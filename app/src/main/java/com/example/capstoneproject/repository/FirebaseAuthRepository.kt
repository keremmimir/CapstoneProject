package com.example.capstoneproject.repository

import com.example.capstoneproject.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun signUp(name: String, surname: String, email: String, password: String): Result<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("UID is null")

            val user = User(uid = uid, name = name,surname = surname,email= email, password = password)

            firestore.collection("users").document(uid).set(user).await()
            Result.success("User registered successfully")
        } catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String,password: String): Result<String>{
        return try {
            auth.signInWithEmailAndPassword(email,password).await()
            Result.success("Welcome")
        } catch (e:Exception){
            Result.failure(e)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}