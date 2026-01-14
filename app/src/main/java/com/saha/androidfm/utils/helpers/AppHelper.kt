package com.saha.androidfm.utils.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saha.fairdrivepartnerapp.utils.helper.CounterHelper
import com.saha.androidfm.data.enums.NetworkState
import java.io.File
import java.net.URLConnection
import java.util.Calendar


object AppHelper {

    var isNetworkConnected by mutableStateOf(NetworkState.UNKNOWN)

    //val isNetworkConnected = _isNetworkConnected.asStateFlow()
    private val counterHelper = CounterHelper()
    private var isFirstTime = true

    @SuppressLint("DefaultLocale")
    fun currencyFormatedText(amount: Double): String {
        return String.format("₹%.2f", amount)
    }

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