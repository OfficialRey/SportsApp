package com.sportsapp.logic

import android.content.Context
import com.sportsapp.database.DatabaseHelper
import com.sportsapp.logic.models.Jam
import com.sportsapp.logic.models.SportPlace
import com.sportsapp.logic.models.Time
import com.sportsapp.logic.models.User
import org.json.JSONObject
import java.time.LocalDate

class JamLogic(context: Context?) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)

    fun createJam(jam: Jam) {
        databaseHelper.executeQuery(
            "INSERT INTO ${DatabaseHelper.JAM_TABLE}" +
                    " (${DatabaseHelper.JAM_SPOT_ID}, ${DatabaseHelper.JAM_MAX_PARTICIPANTS}, ${DatabaseHelper.JAM_DATE}, ${DatabaseHelper.JAM_START_TIME}, ${DatabaseHelper.JAM_END_TIME})" +
                    " VALUES" +
                    " ('${jam.sportPlaceID}', '${jam.participantAmount}', '${jam.date}', '${jam.startTime}', '${jam.endTime}')"
        )

        val result = databaseHelper.requestQuery(
            "SELECT ${DatabaseHelper.JAM_ID} FROM ${DatabaseHelper.JAM_TABLE} WHERE" +
                    " ${DatabaseHelper.JAM_SPOT_ID}='${jam.sportPlaceID}' AND" +
                    " ${DatabaseHelper.JAM_MAX_PARTICIPANTS}='${jam.participantAmount}' AND" +
                    " ${DatabaseHelper.JAM_DATE}='${jam.date}' AND" +
                    " ${DatabaseHelper.JAM_START_TIME}='${jam.startTime}' AND" +
                    " ${DatabaseHelper.JAM_END_TIME}='${jam.endTime}'"
        )
        if (result.length() > 0) {
            val jamID = ((result[0] as JSONObject).get(DatabaseHelper.JAM_ID) as String).toInt()
            val creator = jam.participants[0]
            addUserToJam(creator.userID, jamID)
        }
    }

    private fun removeJam(id: Int) {
        databaseHelper.executeQuery(
            "DELETE FROM ${DatabaseHelper.JAM_TABLE} WHERE ${DatabaseHelper.JAM_ID}='$id'"
        )
        cleanTables()
    }

    fun getJam(spotID: Int): Jam? {
        val result = databaseHelper.requestQuery(
            "SELECT * FROM ${DatabaseHelper.JAM_TABLE} WHERE ${DatabaseHelper.JAM_SPOT_ID}='$spotID'"
        )
        if (result.length() > 0) {
            val jam = buildJam(result[0] as JSONObject)

            // Load users as well
            jam.participants = loadParticipants(jam.id!!)
            return jam
        }
        return null
    }

    private fun getJams(): ArrayList<Jam> {
        val result = databaseHelper.requestQuery(
            "SELECT * FROM ${DatabaseHelper.JAM_TABLE}"
        )
        val jams = ArrayList<Jam>()
        for (i in 0 until result.length()) {
            jams.add(buildJam(result[i] as JSONObject))
        }
        return jams
    }

    fun getJamSportPlaces(): ArrayList<SportPlace> {
        val jams = getJams()
        val sportPlaces = ArrayList<SportPlace>()
        for (jam in jams) {
            val sportPlace = GlobalValues.getSportPlaceLogic()!!.getSportPlace(jam.id!!)
            if (sportPlace != null) {
                sportPlaces.add(sportPlace)
            }
        }
        return sportPlaces
    }

    fun addUserToJam(userID: Int, jamID: Int) {
        databaseHelper.executeQuery(
            "INSERT INTO ${DatabaseHelper.JUSER_TABLE}" +
                    " (${DatabaseHelper.JUSER_JAM_ID}, ${DatabaseHelper.JUSER_USER_ID})" +
                    " VALUES" +
                    " ('$jamID','$userID')"
        )
    }

    fun removeUserFromJam(userID: Int) {
        databaseHelper.executeQuery(
            "DELETE FROM ${DatabaseHelper.JUSER_TABLE} WHERE ${DatabaseHelper.JUSER_USER_ID}='$userID'"
        )
        cleanTables()
    }

    private fun cleanTables() {
        val jams = databaseHelper.requestQuery("SELECT * FROM ${DatabaseHelper.JAM_TABLE}")
        for (i in 0 until jams.length()) {
            val currentJam = buildJam(jams[i] as JSONObject)
            val emptyJams =
                databaseHelper.requestQuery("SELECT * FROM ${DatabaseHelper.JUSER_TABLE} WHERE ${DatabaseHelper.JUSER_JAM_ID}='${currentJam.id}'")
            if (emptyJams.length() == 0) {
                removeJam(currentJam.id!!)
            }
        }
    }

    private fun loadParticipants(jamID: Int): ArrayList<User> {
        val result = databaseHelper.requestQuery(
            "SELECT * FROM ${DatabaseHelper.JUSER_TABLE} WHERE ${DatabaseHelper.JUSER_JAM_ID}='$jamID'"
        )
        val participants = ArrayList<User>()
        for (i in 0 until result.length()) {
            val id = ((result[i] as JSONObject).get(DatabaseHelper.JUSER_USER_ID) as String).toInt()
            val user = GlobalValues.getUserLogic()!!.loadUser(id)
            if (user != null) {
                participants.add(user)
            }
        }
        return participants
    }


    fun isFull(jam: Jam): Boolean {
        val participants = loadParticipants(jam.id!!)
        return participants.size >= jam.participantAmount
    }

    private fun buildJam(json: JSONObject): Jam {
        val id = (json.get(DatabaseHelper.JAM_ID) as String).toInt()
        val spotID = (json.get(DatabaseHelper.JAM_SPOT_ID) as String).toInt()
        val maxParticipants = (json.get(DatabaseHelper.JAM_MAX_PARTICIPANTS) as String).toInt()
        val date = LocalDate.parse(json.get(DatabaseHelper.JAM_DATE) as String)
        val startTime = Time.fromString(json.get(DatabaseHelper.JAM_START_TIME) as String)
        val endTime = Time.fromString(json.get(DatabaseHelper.JAM_END_TIME) as String)

        return Jam(id, spotID, maxParticipants, date, startTime, endTime)
    }
}