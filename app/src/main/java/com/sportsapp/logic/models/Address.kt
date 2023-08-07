package com.sportsapp.logic.models

data class Address(
    var id: Int? = null,
    var postalCode: Int,
    var city: String,
    var street: String,
    var houseNumber: String
)