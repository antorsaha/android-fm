package com.saha.androidfm.utils.helpers

object AppConstants{

    const val STATION_SEAM_URL = "http://118.179.219.244:8000/;?n=d70ecddb2aebf420177c"
    const val STATION_FREQUENCY = "88.9"
    const val STATION_NAME = "ABC FM"
    
    // Live Stream Video URL
    const val LIVE_STREAM_VIDEO_URL = "https://nmxlive.akamaized.net/hls/live/529965/Live_1/index.m3u8"

    const val WEBSITE_URL = "https://www.google.com/"
    const val CONTACT_ADDRESS = "abcfm@gmail.com"
    const val ABOUT_US_URL = "https://www.google.com/"
    const val PRIVACY_POLICY_URL = "https://www.google.com/"
    const val TERMS_OF_USE_URL = "https://www.google.com/"

    const val FACEBOOK_URL = "https://www.facebook.com/"
    const val INSTAGRAM_URL = "https://www.instagram.com/"
    const val TIKTOK_URL = "https://www.tiktok.com/"
    
    // Ad Network Configuration
    // Change this to switch between ad networks: AdNetwork.ADMOB, AdNetwork.META, or AdNetwork.UNITY
    val AD_NETWORK = AdNetwork.ADMOB
    
    // AdMob Configuration
    // Replace with your actual values from AdMob console
    const val ADMOB_APPLICATION_ID = "ca-app-pub-3940256099942544~3347511713" // Test application ID
    const val ADMOB_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111" // Test banner ad unit
    const val ADMOB_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712" // Test interstitial ad unit
    
    // Meta (Facebook) Ads Configuration
    // Replace with your actual placement IDs from Meta Audience Network
    const val META_BANNER_PLACEMENT_ID = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID" // Test placement ID
    const val META_INTERSTITIAL_PLACEMENT_ID = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID" // Test placement ID
    
    // Unity Ads Configuration
    // Replace with your actual Game ID and Placement IDs from Unity Ads dashboard
    const val UNITY_GAME_ID = "YOUR_UNITY_GAME_ID" // Your Unity Ads Game ID
    const val UNITY_BANNER_PLACEMENT_ID = "Banner_Android" // Banner placement ID
    const val UNITY_INTERSTITIAL_PLACEMENT_ID = "Interstitial_Android" // Interstitial placement ID
    const val UNITY_TEST_MODE = true // Set to false for production
    
    // Helper functions to get ad unit IDs based on selected network
    fun getBannerAdUnitId(): String {
        return when (AD_NETWORK) {
            AdNetwork.META -> META_BANNER_PLACEMENT_ID
            AdNetwork.UNITY -> UNITY_BANNER_PLACEMENT_ID
            AdNetwork.ADMOB -> ADMOB_BANNER_AD_UNIT_ID
        }
    }
    
    fun getInterstitialAdUnitId(): String {
        return when (AD_NETWORK) {
            AdNetwork.META -> META_INTERSTITIAL_PLACEMENT_ID
            AdNetwork.UNITY -> UNITY_INTERSTITIAL_PLACEMENT_ID
            AdNetwork.ADMOB -> ADMOB_INTERSTITIAL_AD_UNIT_ID
        }
    }
}