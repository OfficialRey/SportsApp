package com.sportsapp.logic.models

import java.time.LocalDate

class Jam(
    var id: Int? = null,
    var sportPlaceID: Int = -1,
    var participantAmount: Int = 0,
    var date: LocalDate,
    var startTime: Time = Time.zero(),
    var endTime: Time = Time.zero(),
    var participants: ArrayList<User> = ArrayList()
) {

    fun isAtMaxCapacity(): Boolean {
        return participants.size >= participantAmount
    }
}