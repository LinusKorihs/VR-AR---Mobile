package com.example.AbgabeMobile.network

import kotlinx.serialization.Serializable

@Serializable
data class RandomUserResult(
    val cell: String,
    val email: String,
    val gender: String,
    val location: RandomUserLocation,
    val name: RandomUserName, // User's name details
    val phone: String,
    val picture: RandomUserPicture // User's picture URLs
)

@Serializable
data class RandomUserLocation(
    val city: String,
    val country: String,
    val postcode: String? = null, // Postcode can be null
    val state: String,
    val street: RandomUserStreet,
)

@Serializable
data class RandomUserName(
    val first: String, // User's first name
    val last: String, // User's last name
    val title: String // User's title (e.g., Mr, Ms)
)

@Serializable
data class RandomUserPicture(
    val large: String, // URL for large picture
    val medium: String, // URL for medium picture
    val thumbnail: String // URL for thumbnail picture
)

/**
 * Represents the overall response from the Random User API.
 *
 * @property results A list of [RandomUserResult] objects, each representing a user.
 */
@Serializable
data class RandomUserResponse(
    val results: List<RandomUserResult>
)

/**
 * Represents the street details within a user's location.
 */
@Serializable
data class RandomUserStreet(
    val name: String, // Street name
    val number: Int // Street number
)