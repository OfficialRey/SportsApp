package com.sportsapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sportsapp.log.CustomLogger
import org.json.JSONArray
import org.json.JSONObject


const val DATABASE_NAME = "SportsDatabase"
const val DATABASE_VERSION = 1

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    init {
        // Reset and create default tables
        if (!HAS_RESET) {
            // This function was mainly used to start with a fresh and new database
            // This function should be removed when releasing the final product
            //reset()
            //createDefaultTables()
            HAS_RESET = true
        }
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
                    "$USER_PASSWORD VARCHAR($PASSWORD_LENGTH))"
        )

        executeQuery(
            "CREATE TABLE IF NOT EXISTS $LOCATION_TABLE (" +
                    "$LOCATION_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$LOCATION_CREATOR INTEGER," +
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
                    "$REVIEW_TITLE VARCHAR($TITLE_LENGTH)," +
                    "$REVIEW_DESCRIPTION VARCHAR($DESCRIPTION_LENGTH)," +
                    "$REVIEW_DATE DATETIME)"
        )

        // Create image table
        // Images are saved separately as BLOB data to increase performance
        executeQuery(
            "CREATE TABLE IF NOT EXISTS $IMAGE_TABLE (" +
                    "$IMAGE_ID INTEGER," +
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

        executeQuery(
            "CREATE TABLE IF NOT EXISTS $TYPE_TABLE (" +
                    "$TYPE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$TYPE_NAME VARCHAR(32))"
        )

        // Jam Table
        executeQuery(
            "CREATE TABLE IF NOT EXISTS $JAM_TABLE (" +
                    "$JAM_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$JAM_SPOT_ID INTEGER," +
                    "$JAM_MAX_PARTICIPANTS INTEGER," +
                    "$JAM_DATE STRING," +
                    "$JAM_START_TIME STRING," +
                    "$JAM_END_TIME STRING)"
        )

        // Jam User Relations
        executeQuery(
            "CREATE TABLE IF NOT EXISTS $JUSER_TABLE (" +
                    "$JUSER_JAM_ID INTEGER," +
                    "$JUSER_USER_ID INTEGER)"
        )

        // Fill types of places
        executeQuery("INSERT INTO $TYPE_TABLE ($TYPE_NAME) VALUES ('Free-Running')")
        executeQuery("INSERT INTO $TYPE_TABLE ($TYPE_NAME) VALUES ('Basketball Court')")
        executeQuery("INSERT INTO $TYPE_TABLE ($TYPE_NAME) VALUES ('Calisthenics Park')")
        executeQuery("INSERT INTO $TYPE_TABLE ($TYPE_NAME) VALUES ('Playground')")
        executeQuery("INSERT INTO $TYPE_TABLE ($TYPE_NAME) VALUES ('Golf Course')")
        executeQuery("INSERT INTO $TYPE_TABLE ($TYPE_NAME) VALUES ('Swimming')")
        executeQuery("INSERT INTO $TYPE_TABLE ($TYPE_NAME) VALUES ('Running')")
        executeQuery("INSERT INTO $TYPE_TABLE ($TYPE_NAME) VALUES ('Ice Hockey')")
        executeQuery("INSERT INTO $TYPE_TABLE ($TYPE_NAME) VALUES ('Soccer Field')")
        executeQuery("INSERT INTO $TYPE_TABLE ($TYPE_NAME) VALUES ('Other')")
    }

    private fun reset() {
        CustomLogger.logInfo("Database", "Cleaned all databases!")
        executeQuery("DROP TABLE IF EXISTS $USER_TABLE")
        executeQuery("DROP TABLE IF EXISTS $LOCATION_TABLE")
        executeQuery("DROP TABLE IF EXISTS $ADDRESS_TABLE")
        executeQuery("DROP TABLE IF EXISTS $REVIEW_TABLE")
        executeQuery("DROP TABLE IF EXISTS $IMAGE_TABLE")
        executeQuery("DROP TABLE IF EXISTS $LOCAGE_TABLE")
        executeQuery("DROP TABLE IF EXISTS $LOVIEW_TABLE")
        executeQuery("DROP TABLE IF EXISTS $TYPE_TABLE")
        executeQuery("DROP TABLE IF EXISTS $JAM_TABLE")
        executeQuery("DROP TABLE IF EXISTS $JUSER_TABLE")
    }

    companion object {

        var HAS_RESET: Boolean = false

        // Using concept of ERM

        const val USER_NAME_LENGTH = 16
        const val PASSWORD_LENGTH = 32

        const val DESCRIPTION_LENGTH = 1024
        const val TITLE_LENGTH = 64

        // User Table
        // Stores user-related data
        const val USER_TABLE = "User"
        const val USER_ID = "ID"
        const val USER_NAME = "Name"
        const val USER_PASSWORD = "PasswordHash"

        // Location Table
        // Stores location-related data
        const val LOCATION_TABLE = "Location"
        const val LOCATION_ID = "ID"
        const val LOCATION_TYPE =
            "Type" // The type of location (basketball court, soccer arena etc.)
        const val LOCATION_POSITION = "Position" //GeoPos
        const val LOCATION_ADDRESS = "Address"
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

        const val TYPE_TABLE = "LocationType"
        const val TYPE_ID = "ID"
        const val TYPE_NAME = "Name"

        // Table for Jams
        const val JAM_TABLE = "Jam"
        const val JAM_ID = "ID"
        const val JAM_SPOT_ID = "SpotID"
        const val JAM_MAX_PARTICIPANTS = "MaximumParticipants"
        const val JAM_DATE = "Date"
        const val JAM_START_TIME = "StartTime"
        const val JAM_END_TIME = "EndTime"

        // Jam User Relations
        const val JUSER_TABLE = "JamUser"
        const val JUSER_JAM_ID = "JamID"
        const val JUSER_USER_ID = "UserID"
    }
}