package com.sportsapp.logic.algorithm

import android.content.Context
import com.sportsapp.database.DatabaseHelper
import com.sportsapp.log.CustomLogger
import com.sportsapp.logic.objects.LocationModel
import com.sportsapp.logic.objects.User

class RecommendationAlgorithm(context: Context?) {

    private var databaseHelper = DatabaseHelper(context)

    // Algorithm:
    // Get new locations
    // Get locations that have JAMs

    // Filter by area

    private fun getRecommendations(user: User): ArrayList<LocationModel> {
        val list: ArrayList<LocationModel> = ArrayList()
        queryLocations("SELECT * FROM ${DatabaseHelper.LOCAGE_TABLE} WHERE ${DatabaseHelper.LOCATION_DATE} BETWEEN DATE_SUB(NOW(), INTERVAL 1 WEEK) AND NOW()")

        return list
    }

    private fun queryLocations(query: String): ArrayList<LocationModel> {
        val list: ArrayList<LocationModel> = ArrayList()
        var array = databaseHelper.requestQuery(query)
        CustomLogger.logInfo("Array", array.toString())
        return list
    }
}