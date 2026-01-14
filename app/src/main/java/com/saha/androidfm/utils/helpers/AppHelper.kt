package com.saha.androidfm.utils.helpers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saha.androidfm.data.enums.NetworkState
import com.saha.fairdrivepartnerapp.utils.helper.CounterHelper
import java.io.File
import java.net.URLConnection
import java.util.Calendar


object AppHelper {

    var isNetworkConnected by mutableStateOf(NetworkState.UNKNOWN)

    //val isNetworkConnected = _isNetworkConnected.asStateFlow()
    private val counterHelper = CounterHelper()
    private var isFirstTime = true


    fun goToLocationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    fun getMimeType(file: File): String? {
        return URLConnection.guessContentTypeFromName(file.name)
    }

    fun getCurrentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    /**
     * Check if notification permission is granted
     * For Android 13+ (API 33+), POST_NOTIFICATIONS permission is required
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // For Android 12 and below, notification permission is granted by default
            true
        }
    }

    fun shareApp(context: Context) {
        try {
            val packageName = context.packageName
            val playStoreUrl = "https://play.google.com/store/apps/details?id=$packageName"

            val shareMessage = """
                🎵 Tune in to Dennery FM - The Best Music Lives Here! 🎵
                
                Download the app now and enjoy high-quality streaming!
                
                $playStoreUrl
                
            """.trimIndent()

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Check out Dennery FM Radio App!")
                putExtra(Intent.EXTRA_TEXT, shareMessage)
            }

            val chooserIntent = Intent.createChooser(shareIntent, "Share Dennery FM")
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            Logger.e("Error sharing app: ${e.message}", e, tag = "AppHelper")
        }
    }

    fun setNetworkConnection(isConnected: Boolean) {
        Logger.d(tag = "HomeScreen", message = "setNetworkConnection: $isConnected")
        counterHelper.stopCountdown()

        counterHelper.countdownTimer(seconds = 1, onStart = {

        }, onCountDownFinished = {
            when (isNetworkConnected) {
                NetworkState.CONNECTED -> {
                    if (!isConnected) {
                        isNetworkConnected = NetworkState.DISCONNECTED
                    }
                }

                NetworkState.DISCONNECTED -> {
                    if (isConnected) {
                        isNetworkConnected = NetworkState.CONNECTED

                        CounterHelper().countdownTimer(
                            seconds = 3,
                            onStart = {},
                            onCountDownFinished = {
                                isNetworkConnected = NetworkState.UNKNOWN
                            })
                    }
                }

                else -> {
                    Logger.d(tag = "HomeScreen", message = "isFirst time $isFirstTime")
                    if (!isFirstTime) {
                        if (isConnected) {
                            isNetworkConnected = NetworkState.CONNECTED

                            CounterHelper().countdownTimer(
                                seconds = 3,
                                onStart = {},
                                onCountDownFinished = {
                                    isNetworkConnected = NetworkState.UNKNOWN
                                })


                        } else {
                            isNetworkConnected = NetworkState.DISCONNECTED
                        }

                    } else {
                        isFirstTime = false

                        if (!isConnected) {
                            isNetworkConnected = NetworkState.DISCONNECTED
                        }
                    }


                    //isNetworkConnected = NetworkState.UNKNOWN
                }
            }
        })
    }


}