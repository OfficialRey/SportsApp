package com.sportsapp.logic.models

data class User(
    var userID: Int,
    var userName: String,
    var passwordHash: String
)