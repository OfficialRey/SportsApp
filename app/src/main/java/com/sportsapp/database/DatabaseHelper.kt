package com.sportsapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONArray
import org.json.JSONObject


const val DATABASE_NAME = "SportsDatabase"
const val DATABASE_VERSION = 1

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        createDefaultTables()
    }


    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun requestQuery(query: String): JSONArray {
        val result = JSONArray()
        val database = readableDatabase
        val cursor = database.rawQuery(query, null)

        // Convert cursor to JSON to return the json object and close the cursor
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val column = cursor.columnCount
            val rowObject = JSONObject()
            for (i in 0 until column) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(
                            cursor.getColumnName(i),
                            cursor.getString(i)
                        )
                    } catch (_: Exception) {
                    }
                }
            }
            result.put(rowObject)
            cursor.moveToNext()
        }
        cursor.close()
        database.close()
        return result
    }

    fun executeQuery(query: String) {
        val database = writableDatabase
        database.execSQL(query)
        database.close()
    }

    private fun createDefaultTables() {
        // Create user table
        executeQuery(
            "CREATE TABLE IF NOT EXISTS $USER_TABLE (" +
                    "$USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$USER_NAME VARCHAR($USER_NAME_LENGTH)," +
                    "$USER_PASSWORD VARCHAR($PASSWORD_LENGTH)," +
                    "$USER_IMAGE STRING)"
        )

        executeQuery(
            "CREATE TABLE IF NOT EXISTS $LOCATION_TABLE (" +
                    "$LOCATION_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$LOCATION_CREATOR INTEGER," +
                    "$LOCATION_REVIEWS INTEGER," +
                    "$LOCATION_IMAGE INTEGER," +
                    "$LOCATION_ADDRESS INTEGER," +
                    "$LOCATION_TYPE INTEGER," +
                    "$LOCATION_POSITION STRING," +
                    "$LOCATION_DATE DATE)"
        )

        executeQuery(
            "CREATE TABLE IF NOT EXISTS $ADDRESS_TABLE (" +
                    "$ADDRESS_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$ADDRESS_POSTAL_CODE STRING," +
                    "$ADDRESS_CITY STRING," +
                    "$ADDRESS_STREET STRING," +
                    "$ADDRESS_NUMBER STRING)"
        )

        executeQuery(
            "CREATE TABLE IF NOT EXISTS $REVIEW_TABLE (" +
                    "$REVIEW_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$REVIEW_CREATOR VARCHAR," +
                    "$REVIEW_RATING INTEGER," +
                    "$REVIEW_TITLE VARCHAR(128)," +
                    "$REVIEW_DESCRIPTION VARCHAR(4096)," +
                    "$REVIEW_DATE DATE)"
        )

        // Create image table
        // Images are saved separately as BLOB data to increase performance
        executeQuery(
            "CREATE TABLE IF NOT EXISTS $IMAGE_TABLE (" +
                    "$IMAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$IMAGE_DATA VARCHAR)"
        )

        executeQuery(
            "CREATE TABLE IF NOT EXISTS $LOCAGE_TABLE (" +
                    "$LOCAGE_LOCATION INTEGER," +
                    "$LOCAGE_IMAGE INTEGER)"
        )

        executeQuery(
            "CREATE TABLE IF NOT EXISTS $LOVIEW_TABLE (" +
                    "$LOVIEW_LOCATION INTEGER," +
                    "$LOVIEW_REVIEW INTEGER)"
        )
    }

    private fun deleteDefaultTables() {
        executeQuery("DROP TABLE IF EXISTS $USER_TABLE")
        executeQuery("DROP TABLE IF EXISTS $LOCATION_TABLE")
        executeQuery("DROP TABLE IF EXISTS $ADDRESS_TABLE")
        executeQuery("DROP TABLE IF EXISTS $REVIEW_TABLE")
        executeQuery("DROP TABLE IF EXISTS $IMAGE_TABLE")
        executeQuery("DROP TABLE IF EXISTS $LOCAGE_TABLE")
        executeQuery("DROP TABLE IF EXISTS $LOVIEW_TABLE")
    }

    companion object {
        // Using concept of ERM

        const val USER_NAME_LENGTH = 16
        const val PASSWORD_LENGTH = 32

        // User Table
        // Stores user-related data
        const val USER_TABLE = "User"
        const val USER_ID = "ID"
        const val USER_NAME = "Name"
        const val USER_PASSWORD = "PasswordHash"
        const val USER_IMAGE = "ImageID"

        // Location Table
        // Stores location-related data
        const val LOCATION_TABLE = "Location"
        const val LOCATION_ID = "ID"
        const val LOCATION_TYPE =
            "Type" // The type of location (basketball court, soccer arena etc.)
        const val LOCATION_IMAGE = "Image" //Foreign key to get images
        const val LOCATION_POSITION = "Position" //GeoPos
        const val LOCATION_ADDRESS = "Address"
        const val LOCATION_REVIEWS = "Reviews"
        const val LOCATION_CREATOR = "Creator" // User-ID
        const val LOCATION_DATE = "CreationDate"

        // Address Table
        // Stores address-related data
        const val ADDRESS_TABLE = "Address"
        const val ADDRESS_ID = "ID"
        const val ADDRESS_POSTAL_CODE = "PostalCode"
        const val ADDRESS_CITY = "City"
        const val ADDRESS_STREET = "Street"
        const val ADDRESS_NUMBER = "Number"

        // Review Table
        // Stores review-related data
        const val REVIEW_TABLE = "Review"
        const val REVIEW_ID = "ID"
        const val REVIEW_CREATOR = "Creator"
        const val REVIEW_RATING = "Rating"
        const val REVIEW_TITLE = "Title"
        const val REVIEW_DESCRIPTION = "Description"
        const val REVIEW_DATE = "CreationDate"

        // Image Table
        // Stores images
        const val IMAGE_TABLE = "Image"
        const val IMAGE_ID = "ID"
        const val IMAGE_DATA = "ImageData"

        // Location-Image Table
        // Stores references of images regarding a location
        // 1 : n principle
        const val LOCAGE_TABLE = "LocationImage"
        const val LOCAGE_LOCATION = "Location"
        const val LOCAGE_IMAGE = "Image"

        // Location-Review Table
        // Stores references of reviews regarding a location
        const val LOVIEW_TABLE = "LocationReview"
        const val LOVIEW_LOCATION = "Location"
        const val LOVIEW_REVIEW = "Review"

    }
}