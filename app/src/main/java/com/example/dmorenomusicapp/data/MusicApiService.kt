package com.example.dmorenomusicapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicApiService {
    @GET("api/albums")
    suspend fun getAlbums(): List<Album>

    @GET("api/albums/{id}")
    suspend fun getAlbumById(@Path("id") id: String): Album

    companion object {
        private const val BASE_URL = "https://musicapi.pjasoft.com/"

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun create(): MusicApiService = retrofit.create(MusicApiService::class.java)
    }
}
