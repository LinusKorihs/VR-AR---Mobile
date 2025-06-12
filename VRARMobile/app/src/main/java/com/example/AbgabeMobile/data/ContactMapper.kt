package com.example.AbgabeMobile.data

import com.example.AbgabeMobile.network.RandomUserResult

fun RandomUserResult.toContact(): Contact {
    return Contact(
        name = "${name.first} ${name.last}",
        phone = phone,
        email = email,
        birthday = "",
        street = location.street.name,
        houseNr = location.street.number.toString(),
        postcode = location.postcode ?: "",
        city = location.city,
        imageRes = picture.large
    )
}