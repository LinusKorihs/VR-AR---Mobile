package com.example.AbgabeMobile.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body // For .body() calls
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get // For GET requests
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun getRandomUsersFromApi(): RandomUserResponse {
        val randomUserUrl = "https://randomuser.me/api/?results=20"
        return client.get(randomUserUrl).body()
    }
}