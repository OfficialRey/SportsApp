package com.sportsapp.logic.models

import java.time.LocalDate

data class Review(
    var id: Int? = null,
    var creator: Int,
    var rating: Int,
    var title: String,
    var description: String,
    var creationDate: LocalDate? = null,
)