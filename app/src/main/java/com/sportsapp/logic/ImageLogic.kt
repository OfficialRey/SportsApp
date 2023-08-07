package com.sportsapp.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory
import android.util.Base64

import com.sportsapp.database.DatabaseHelper;
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class ImageLogic(context: Context?) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)

    fun createImage(image: Bitmap, id: Int) {
        // Create spot image
        databaseHelper.executeQuery(
            "INSERT INTO ${DatabaseHelper.IMAGE_TABLE} (${DatabaseHelper.IMAGE_ID}, ${DatabaseHelper.IMAGE_DATA}) VALUES ('${id}', '${
                encodeBitmap(
                    image
                )
            }')"
        )
    }

    fun loadImage(id: Int): Bitmap? {
        val result = databaseHelper.requestQuery(
            "SELECT ${DatabaseHelper.IMAGE_DATA} FROM ${DatabaseHelper.IMAGE_TABLE} WHERE ${DatabaseHelper.IMAGE_ID} LIKE '$id'"
        )
        if (result.length() > 0) {
            return decodeBase64((result[0] as JSONObject).get(DatabaseHelper.IMAGE_DATA) as String)
        }
        return null
    }

    private fun encodeBitmap(image: Bitmap): String {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bytes = stream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun decodeBase64(image: String): Bitmap {
        val bytes = Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}