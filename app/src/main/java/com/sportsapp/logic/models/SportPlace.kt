package com.sportsapp.logic.models

import android.graphics.Bitmap
import com.sportsapp.log.CustomLogger
import java.time.LocalDate

data class SportPlace(
    var id: Int? = null,
    var creator: Int,
    var address: Int,
    var type: Int,
    var creationDate: LocalDate? = null,
    var image: Bitmap? = null
) {
    override fun toString(): String {
        return "SportPlace{id: $id, creator: $creator, address: $address, type: $type, creationDate: $creationDate, image: ${image.hashCode()}}"
    }

    fun equivalent(sportPlace: SportPlace): Boolean {
        return (creator == sportPlace.creator
                && address == sportPlace.address
                && type == sportPlace.type)
    }
}