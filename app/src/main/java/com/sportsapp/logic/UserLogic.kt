package com.sportsapp.logic

import android.content.Context
import com.sportsapp.database.DatabaseHelper
import com.sportsapp.log.CustomLogger
import com.sportsapp.logic.models.User
import org.json.JSONObject
import java.security.MessageDigest
import java.util.Base64

class UserLogic(context: Context?) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)

    fun createUser(userName: String, password: String) {
        val passwordHash = convertToSHA256(password)
        databaseHelper.executeQuery("INSERT INTO ${DatabaseHelper.USER_TABLE} (${DatabaseHelper.USER_NAME}, ${DatabaseHelper.USER_PASSWORD}) VALUES ('$userName', '$passwordHash')")
        val user = fetchUser(userName)
        if (user != null) {
            GlobalValues.setUser(user)
        }
    }

    fun existsUser(userName: String): Boolean {
        val result =
            databaseHelper.requestQuery("SELECT ${DatabaseHelper.USER_NAME} FROM ${DatabaseHelper.USER_TABLE} WHERE ${DatabaseHelper.USER_NAME} LIKE '$userName'")
        return result.length() > 0
    }

    private fun fetchUser(userName: String): User? {
        val result =
            databaseHelper.requestQuery("SELECT * FROM ${DatabaseHelper.USER_TABLE} WHERE ${DatabaseHelper.USER_NAME} LIKE '$userName'")
        if (result.length() > 0) {
            return buildUser(result[0] as JSONObject)
        }
        return null
    }

    fun loadUser(userID: Int): User? {
        val result = databaseHelper.requestQuery(
            "SELECT * FROM ${DatabaseHelper.USER_TABLE} WHERE ${DatabaseHelper.USER_ID}='$userID'"
        )
        if (result.length() > 0) {
            return buildUser(result[0] as JSONObject)
        }
        return null
    }

    fun logOutUser() {
        GlobalValues.setUser(null)
    }

    fun logInUser(userName: String) {
        GlobalValues.setUser(fetchUser(userName))
        CustomLogger.logInfo("Login", "Logged in $userName")
    }

    fun checkPassword(userName: String, password: String): Boolean {
        val result =
            databaseHelper.requestQuery("SELECT ${DatabaseHelper.USER_PASSWORD} FROM ${DatabaseHelper.USER_TABLE} WHERE ${DatabaseHelper.USER_NAME} LIKE '$userName'")
        if (result.length() > 0) {
            val json: JSONObject = result[0] as JSONObject
            val passwordHash = json.get(DatabaseHelper.USER_PASSWORD)
            if (convertToSHA256(password) == passwordHash) {
                return true
            }
        }
        return false
    }

    private fun convertToSHA256(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray(Charsets.UTF_8))
        return Base64.getUrlEncoder().encodeToString(bytes)
    }

    private fun buildUser(json: JSONObject): User {
        val userID: Int = (json.get(DatabaseHelper.USER_ID) as String).toInt()
        val userName: String = json.get(DatabaseHelper.USER_NAME) as String
        val userHash: String = json.get(DatabaseHelper.USER_PASSWORD) as String
        return User(userID, userName, userHash)
    }
}