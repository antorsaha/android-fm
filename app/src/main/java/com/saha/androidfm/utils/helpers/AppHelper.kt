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

    /**
     * Get the app version name from PackageInfo
     * Returns version name like "1.0" or "Unknown" if unable to retrieve
     */
    fun getVersionName(context: Context): String {
        return try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            packageInfo.versionName ?: "Unknown"
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.e("Error getting version name: ${e.message}", e, tag = "AppHelper")
            "Unknown"
        }
    }

    /**
     * Open social media link - tries to open in app first, falls back to browser
     * @param context Context
     * @param url Web URL of the social media profile
     * @param appPackage Package name of the social media app (e.g., "com.facebook.katana")
     * @param appUri Deep link URI for the app (optional)
     */
    fun openSocialMedia(
        context: Context,
        url: String,
        appPackage: String? = null,
        appUri: String? = null
    ) {
        try {
            // Try to open in app first if app URI is provided
            if (!appUri.isNullOrEmpty() && appPackage != null && isAppInstalled(context, appPackage)) {
                try {
                    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(appUri)).apply {
                        setPackage(appPackage)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(appIntent)
                    return
                } catch (e: Exception) {
                    // If app intent fails, fall through to browser
                    Logger.d("App intent failed, opening in browser", tag = "AppHelper")
                }
            }

            // Fall back to opening in browser
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(browserIntent)
        } catch (e: Exception) {
            Logger.e("Error opening social media: ${e.message}", e, tag = "AppHelper")
        }
    }

    /**
     * Check if an app is installed
     */
    private fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(packageName, 0)
            }
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * Open Facebook profile - tries app first, then browser
     */
    fun openFacebook(context: Context, facebookUrl: String) {
        // Extract Facebook page name from URL
        val pageName = facebookUrl.substringAfterLast("/")
        val appUri = "fb://facewebmodal/f?href=$facebookUrl"
        openSocialMedia(
            context = context,
            url = facebookUrl,
            appPackage = "com.facebook.katana",
            appUri = appUri
        )
    }

    /**
     * Open Instagram profile - tries app first, then browser
     */
    fun openInstagram(context: Context, instagramUrl: String) {
        // Extract Instagram username from URL
        val username = instagramUrl.substringAfterLast("/").removeSuffix("/")
        val appUri = "instagram://user?username=$username"
        openSocialMedia(
            context = context,
            url = instagramUrl,
            appPackage = "com.instagram.android",
            appUri = appUri
        )
    }

    /**
     * Open TikTok profile - tries app first, then browser
     */
    fun openTikTok(context: Context, tiktokUrl: String) {
        // Extract TikTok username from URL (e.g., @denneryfm)
        val username = tiktokUrl.substringAfterLast("@").removeSuffix("/")
        val appUri = "tiktok://user?username=$username"
        openSocialMedia(
            context = context,
            url = tiktokUrl,
            appPackage = "com.zhiliaoapp.musically",
            appUri = appUri
        )
    }

    /**
     * Open email client with pre-filled recipient
     */
    fun openEmail(context: Context, emailAddress: String) {
        try {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$emailAddress")
                putExtra(Intent.EXTRA_SUBJECT, "Contact Dennery FM")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            val chooserIntent = Intent.createChooser(emailIntent, "Send Email")
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            Logger.e("Error opening email: ${e.message}", e, tag = "AppHelper")
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