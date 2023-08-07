package com.sportsapp.logic.models

class Time(
    var hours: Int = 0,
    var minutes: Int = 0
) {

    override fun toString(): String {
        return "${hours}:$minutes"
    }

    companion object {
        fun fromString(code: String): Time {
            val data = code.split(":")
            return Time(data[0].toInt(), data[1].toInt())
        }

        fun zero(): Time {
            return Time(0, 0)
        }
    }
}