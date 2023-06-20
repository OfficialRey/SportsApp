package com.sportsapp.log

import android.util.Log

class CustomLogger {

    companion object {

        fun logError(tag: String, message: String) {
            Log.e(tag, "ERROR: $message")
        }

        fun logWarning(tag: String, message: String) {
            Log.w(tag, "WARNING: $message")
        }

        fun logInfo(tag: String, message: String) {
            Log.i(tag, "Info: $message")
        }

        fun logDebug(tag: String, message: String) {
            Log.i(tag, "DEBUG: $message")
        }

        fun logVerbose(tag: String, message: String) {
            Log.i(tag, "Verbose: $message")
        }
    }
}