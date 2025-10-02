package com.vipin.data.remote

import retrofit2.http.GET
import com.vipin.data.model.Character

interface HarryPotterApiService {
    @GET("characters")
    suspend fun getCharacters(): List<Character>
}