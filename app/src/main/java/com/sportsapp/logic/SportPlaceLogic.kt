package com.sportsapp.logic

import android.content.Context
import com.sportsapp.database.DatabaseHelper
import com.sportsapp.logic.models.SportPlace
import org.json.JSONObject
import java.time.LocalDate

class SportPlaceLogic(context: Context?) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)

    fun getAllSportPlaces(): ArrayList<SportPlace> {
        // Note: Resource heavy - Only use sparely
        val result = databaseHelper.requestQuery("SELECT * FROM ${DatabaseHelper.LOCATION_TABLE}")
        val places = ArrayList<SportPlace>()
        for (i in 0 until result.length()) {
            places.add(buildSportPlace(result[i] as JSONObject))
        }
        return places
    }

    fun getSportPlace(sportPlaceID: Int): SportPlace? {
        val result = databaseHelper.requestQuery(
            "SELECT * FROM ${DatabaseHelper.LOCATION_TABLE} WHERE ${DatabaseHelper.LOCATION_ID}='$sportPlaceID'"
        )
        if (result.length() > 0) {
            return buildSportPlace(result[0] as JSONObject)
        }
        return null
    }

    fun querySportPlaces(filter: String): ArrayList<SportPlace> {
        val result = databaseHelper.requestQuery(
            "SELECT * FROM ${DatabaseHelper.LOCATION_TABLE}, ${DatabaseHelper.ADDRESS_TABLE} WHERE" +
                    " ${DatabaseHelper.ADDRESS_TABLE}.${DatabaseHelper.ADDRESS_ID} = ${DatabaseHelper.LOCATION_TABLE}.${DatabaseHelper.LOCATION_ID} AND" +
                    " (${DatabaseHelper.ADDRESS_TABLE}.${DatabaseHelper.ADDRESS_STREET} LIKE '%$filter%' OR" +
                    " ${DatabaseHelper.ADDRESS_TABLE}.${DatabaseHelper.ADDRESS_CITY} LIKE '%$filter%')"
        )

        val places = ArrayList<SportPlace>()
        for (i in 0 until result.length()) {
            val sportPlace = buildSportPlace(result[i] as JSONObject)
            if (!containsPlace(places, sportPlace)) {
                places.add(buildSportPlace(result[i] as JSONObject))
            }
        }
        return places
    }

    fun createSportPlace(sportPlace: SportPlace): Int {
        // Create sport place
        databaseHelper.executeQuery("INSERT INTO ${DatabaseHelper.LOCATION_TABLE} (${DatabaseHelper.LOCATION_CREATOR}, ${DatabaseHelper.LOCATION_ADDRESS}, ${DatabaseHelper.LOCATION_TYPE}, ${DatabaseHelper.LOCATION_DATE}) VALUES ('${sportPlace.creator}', '${sportPlace.address}', '${sportPlace.type}', CURRENT_TIMESTAMP)")
        // Get assigned location id
        val result = databaseHelper.requestQuery(
            "SELECT ${DatabaseHelper.LOCATION_ID} FROM ${DatabaseHelper.LOCATION_TABLE} WHERE" +
                    " ${DatabaseHelper.LOCATION_CREATOR} LIKE '${sportPlace.creator}' AND" +
                    " ${DatabaseHelper.LOCATION_ADDRESS} LIKE '${sportPlace.address}' AND" +
                    " ${DatabaseHelper.LOCATION_TYPE} LIKE '${sportPlace.type}'"
        )
        if (result.length() > 0) {
            return ((result[0] as JSONObject).get(DatabaseHelper.LOCATION_ID) as String).toInt()
        }
        return -1
    }

    fun loadSportPlaceList(): ArrayList<String> {
        val list = ArrayList<String>()
        val result =
            databaseHelper.requestQuery("SELECT ${DatabaseHelper.TYPE_NAME} FROM ${DatabaseHelper.TYPE_TABLE}")
        for (i in 0 until result.length()) {
            list.add((result[i] as JSONObject).get(DatabaseHelper.TYPE_NAME) as String)
        }
        return list
    }

    fun loadSportPlaceType(type: Int): String {
        return loadSportPlaceList()[type]
    }

    private fun buildSportPlace(json: JSONObject): SportPlace {
        val id: Int = (json.get(DatabaseHelper.LOCATION_ID) as String).toInt()
        val creator: Int = (json.get(DatabaseHelper.LOCATION_CREATOR) as String).toInt()
        val address: Int = (json.get(DatabaseHelper.LOCATION_ADDRESS) as String).toInt()
        val type: Int = (json.get(DatabaseHelper.LOCATION_TYPE) as String).toInt()
        val date = (json.get(DatabaseHelper.LOCATION_DATE) as String).split(" ")[0]
        val creationDate: LocalDate = LocalDate.parse(date)
        val image = GlobalValues.getImageLogic()!!.loadImage(id)

        return SportPlace(id, creator, address, type, creationDate, image)
    }

    companion object {
        fun containsPlace(places: ArrayList<SportPlace>, sportPlace: SportPlace): Boolean {
            for (i in 0 until places.size) {
                if (sportPlace.equivalent(places[i])) {
                    return true
                }
            }
            return false
        }

    }
}