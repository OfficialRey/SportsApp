package com.sportsapp.logic

import android.content.Context
import com.sportsapp.database.DatabaseHelper
import com.sportsapp.log.CustomLogger
import com.sportsapp.logic.models.Review
import org.json.JSONObject
import java.time.LocalDate

class ReviewLogic(context: Context?) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)

    fun createReview(sportPlaceID: Int, review: Review): Int {
        // Create review
        databaseHelper.executeQuery(
            "INSERT INTO ${DatabaseHelper.REVIEW_TABLE}" +
                    " (${DatabaseHelper.REVIEW_CREATOR}, ${DatabaseHelper.REVIEW_RATING}, ${DatabaseHelper.REVIEW_TITLE}, ${DatabaseHelper.REVIEW_DESCRIPTION}, ${DatabaseHelper.REVIEW_DATE})" +
                    " VALUES" +
                    " ('${review.creator}', '${review.rating}', '${review.title}', '${review.description}', CURRENT_TIMESTAMP)"
        )

        val result = databaseHelper.requestQuery(
            "SELECT ${DatabaseHelper.REVIEW_ID} FROM ${DatabaseHelper.REVIEW_TABLE} WHERE" +
                    " ${DatabaseHelper.REVIEW_CREATOR}='${review.creator}' AND" +
                    " ${DatabaseHelper.REVIEW_RATING}='${review.rating}' AND" +
                    " ${DatabaseHelper.REVIEW_TITLE}='${review.title}' AND" +
                    " ${DatabaseHelper.REVIEW_DESCRIPTION}='${review.description}'"
        )

        if (result.length() > 0) {
            val reviewID =
                ((result[0] as JSONObject).get(DatabaseHelper.LOCATION_ID) as String).toInt()

            // Create LoView Entry
            databaseHelper.executeQuery(
                "INSERT INTO ${DatabaseHelper.LOVIEW_TABLE} (${DatabaseHelper.LOVIEW_LOCATION}, ${DatabaseHelper.LOVIEW_REVIEW}) VALUES ($sportPlaceID, $reviewID)"
            )
            return reviewID
        }
        return -1
    }

    fun loadReviews(sportPlaceID: Int): ArrayList<Review> {
        /*val result = databaseHelper.requestQuery(
            "SELECT * FROM ${DatabaseHelper.REVIEW_TABLE}" +
                    " INNER JOIN ${DatabaseHelper.LOVIEW_TABLE} ON ${DatabaseHelper.LOVIEW_TABLE}.${DatabaseHelper.LOVIEW_REVIEW} = ${DatabaseHelper.REVIEW_TABLE}.${DatabaseHelper.REVIEW_ID}" +
                    " WHERE ${DatabaseHelper.LOVIEW_TABLE}.${DatabaseHelper.LOVIEW_LOCATION}='$sportPlaceID'"
        )
        CustomLogger.logDebug("ReviewLoading", "RESULTS: ${reviews.size}")
         */

        // Query IDs of reviews of spot
        val loviewIDs =
            databaseHelper.requestQuery(
                "SELECT ${DatabaseHelper.LOVIEW_REVIEW} FROM ${DatabaseHelper.LOVIEW_TABLE} WHERE ${DatabaseHelper.LOVIEW_LOCATION}='$sportPlaceID'"
            )

        // Loop over review ids and create review model objects
        val reviews = ArrayList<Review>()
        for (i in 0 until loviewIDs.length()) {
            val reviewID = (loviewIDs[i] as JSONObject).get(DatabaseHelper.LOVIEW_REVIEW)
            val result = databaseHelper.requestQuery(
                "SELECT * FROM ${DatabaseHelper.REVIEW_TABLE} WHERE ${DatabaseHelper.REVIEW_ID}='$reviewID'"
            )
            if (result.length() > 0) {
                reviews.add(buildReview(result[0] as JSONObject))
            }
        }

        return reviews
    }

    private fun buildReview(json: JSONObject): Review {
        val id = (json.get(DatabaseHelper.REVIEW_ID) as String).toInt()
        val creator = (json.get(DatabaseHelper.REVIEW_CREATOR) as String).toInt()
        val rating = (json.get(DatabaseHelper.REVIEW_RATING) as String).toInt()
        val title = json.get(DatabaseHelper.REVIEW_TITLE) as String
        val description = json.get(DatabaseHelper.REVIEW_DESCRIPTION) as String
        val date = (json.get(DatabaseHelper.REVIEW_DATE) as String).split(" ")[0]
        val creationDate: LocalDate = LocalDate.parse(date)

        return Review(id, creator, rating, title, description, creationDate)
    }

    fun getRating(sportPlaceID: Int): Float {
        val reviews = loadReviews(sportPlaceID)
        var rating = 0F
        for (i in 0 until reviews.size) {
            rating += reviews[i].rating
        }
        rating /= reviews.size.toFloat()
        return rating
    }
}