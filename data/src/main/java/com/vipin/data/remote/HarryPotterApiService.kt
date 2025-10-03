package com.vipin.data.remote

import com.vipin.data.model.Character
import retrofit2.http.GET

internal interface HarryPotterApiService {
    @GET("characters")
    suspend fun getCharacters(): List<Character>
}