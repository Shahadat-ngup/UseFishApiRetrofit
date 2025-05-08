package com.example.fishapi
import retrofit2.http.GET

interface ApiService {
    @GET("fish")
    suspend fun getAllFish(): List<Fish>

    @GET("comments")
    suspend fun getAllComments(): List<Comment>
}