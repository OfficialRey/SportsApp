package com.sportsapp.junit

import com.sportsapp.logic.models.Jam
import com.sportsapp.logic.models.SportPlace
import com.sportsapp.logic.models.Time
import com.sportsapp.logic.models.User
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate

class ExampleTest {
    @Test
    fun testSportPlaceEquivalency() {
        val base = SportPlace(
            id = 0,
            creator = 0,
            address = 0,
            type = 0,
            creationDate = null,
            image = null
        )
        val place1 = SportPlace(
            id = 0,
            creator = 0,
            address = 0,
            type = 0,
            creationDate = null,
            image = null
        )
        val place2 = SportPlace(
            id = 1,
            creator = 0,
            address = 0,
            type = 2,
            creationDate = null,
            image = null
        )
        Assert.assertTrue(base.equivalent(place1))
        Assert.assertFalse(base.equivalent(place2))
    }

    @Test
    fun testIsJamFull() {
        val user = User(0, "Test", "123")
        val user1 = User(1, "Test2", "123")
        val participants = arrayListOf(user, user1)
        val jam = Jam(
            id = 0,
            sportPlaceID = 0,
            participantAmount = 2,
            date = LocalDate.now(),
            startTime = Time.zero(),
            endTime = Time.zero(),
            participants = ArrayList()
        )
        val jam1 = Jam(
            id = 0,
            sportPlaceID = 0,
            participantAmount = 2,
            date = LocalDate.now(),
            startTime = Time.zero(),
            endTime = Time.zero(),
            participants = participants
        )
        Assert.assertFalse(jam.isAtMaxCapacity())
        Assert.assertTrue(jam1.isAtMaxCapacity())
    }
}