package com.example.capstoneproject.service

import okhttp3.Interceptor
import okhttp3.OkHttpClient

class ApiClient {
    private val client: OkHttpClient

    init {
        val headerInterceptor = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("x-rapidapi-key", "c604dcefedmsh447e0047f439293p10f6cajsn805880157cc5")
                .header("x-rapidapi-host", "imdb-top-100-movies.p.rapidapi.com")
                .build()
            chain.proceed(request)
        }

        client = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()
    }

    fun getClient(): OkHttpClient {
        return client
    }
}