package com.saha.androidfm.utils.helpers

import android.util.Log
import com.saha.androidfm.BuildConfig

object Logger {
    private const val TAG = "AndroidFM"

    fun d(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun e(message: String, throwable: Throwable? = null, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }

    fun i(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    fun w(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message)
        }
    }

    fun v(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message)
        }
    }

    fun wtf(message: String, throwable: Throwable? = null, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            if (throwable != null) {
                Log.wtf(tag, message, throwable)
            } else {
                Log.wtf(tag, message)
            }
        }
    }

    // Utility function to log method entry (useful for debugging)
    fun methodStart(methodName: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "→ $methodName")
        }
    }

    // Utility function to log method exit
    fun methodEnd(methodName: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "← $methodName")
        }
    }

    // Log network calls
    fun logNetworkCall(url: String, method: String, response: String? = null) {
        if (BuildConfig.DEBUG) {
            Log.d("$TAG-Network", "[$method] $url")
            response?.let {
                Log.d("$TAG-Network", "Response: $it")
            }
        }
    }

    // Log exceptions with custom formatting
    fun exception(e: Throwable, message: String? = null) {
        if (BuildConfig.DEBUG) {
            val logMessage =
                message?.let { "$it: ${e.message}" } ?: e.message ?: "Exception occurred"
            Log.e(TAG, logMessage, e)
        }
    }
}