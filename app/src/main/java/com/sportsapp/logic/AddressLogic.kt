package com.sportsapp.logic

import android.content.Context
import com.sportsapp.database.DatabaseHelper
import com.sportsapp.logic.models.Address
import org.json.JSONObject

class AddressLogic(context: Context?) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)

    fun createAddress(address: Address): Int {
        databaseHelper.executeQuery("INSERT INTO ${DatabaseHelper.ADDRESS_TABLE} (${DatabaseHelper.ADDRESS_POSTAL_CODE}, ${DatabaseHelper.ADDRESS_CITY}, ${DatabaseHelper.ADDRESS_STREET}, ${DatabaseHelper.ADDRESS_NUMBER}) VALUES ('${address.postalCode}', '${address.city}', '${address.street}', '${address.houseNumber}')")
        // Get assigned address id
        val result = databaseHelper.requestQuery(
            "SELECT ${DatabaseHelper.ADDRESS_ID} FROM ${DatabaseHelper.ADDRESS_TABLE} WHERE" +
                    " ${DatabaseHelper.ADDRESS_POSTAL_CODE} LIKE '${address.postalCode}' AND" +
                    " ${DatabaseHelper.ADDRESS_CITY} LIKE '${address.city}' AND" +
                    " ${DatabaseHelper.ADDRESS_STREET} LIKE '${address.street}' AND" +
                    " ${DatabaseHelper.ADDRESS_NUMBER} LIKE '${address.houseNumber}'"
        )
        if (result.length() > 0) {
            return ((result[0] as JSONObject).get(DatabaseHelper.ADDRESS_ID) as String).toInt()
        }
        return -1
    }

    fun loadAddress(id: Int): Address? {
        val result =
            databaseHelper.requestQuery("SELECT * FROM ${DatabaseHelper.ADDRESS_TABLE} WHERE ${DatabaseHelper.ADDRESS_ID} LIKE '$id'")
        if (result.length() > 0) {
            val json: JSONObject = result[0] as JSONObject
            val postalCode: Int = (json.get(DatabaseHelper.ADDRESS_POSTAL_CODE) as String).toInt()
            val city: String = json.get(DatabaseHelper.ADDRESS_CITY) as String
            val street: String = json.get(DatabaseHelper.ADDRESS_STREET) as String
            val streetNumber: String = json.get(DatabaseHelper.ADDRESS_NUMBER) as String
            return Address(id, postalCode, city, street, streetNumber)
        }
        return null
    }
}