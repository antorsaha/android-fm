@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.saha.androidfm.views

import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.VideoCameraFront
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.facebook.ads.AdView as MetaAdView
import com.facebook.ads.AdSize as MetaAdSize
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize
import com.google.gson.Gson
import com.saha.androidfm.data.enums.Screen
import com.saha.androidfm.ui.theme.accent
import com.saha.androidfm.ui.theme.backgroundColor
import com.saha.androidfm.ui.theme.secondaryTextColor
import com.saha.androidfm.utils.helpers.AdNetwork
import com.saha.androidfm.utils.helpers.AppConstants
import com.saha.androidfm.utils.helpers.LoadingManager
import com.saha.androidfm.utils.helpers.PreferencesManager
import com.saha.androidfm.utils.navigation.NavigationWrapper
import com.saha.androidfm.viewmodels.RadioPlayerViewModel
import com.saha.androidfm.views.dialogs.AppLoader
import com.saha.androidfm.views.screens.SettingScreen
import com.saha.androidfm.views.screens.WebViewScreen
import com.saha.androidfm.views.screens.WebViewScreenRoute
import com.saha.androidfm.views.screens.homeScreen.LiveStreamScreenRoute
import com.saha.androidfm.views.screens.homeScreen.RadioScreen
import com.saha.androidfm.views.screens.homeScreen.RadioScreenRoute
import com.saha.androidfm.views.screens.homeScreen.SettingsScreenRoute
import com.saha.androidfm.views.screens.liveSteam.LiveSteamScreen
import com.saha.androidfm.views.screens.onboarding.OnboardingScreen
import com.saha.androidfm.views.screens.onboarding.OnboardingScreenRoute

/**
 * Main application composable that sets up the navigation structure and UI.
 * 
 * This is the root composable of the app that:
 * - Manages navigation between screens using Jetpack Navigation
 * - Displays a bottom navigation bar for main screens
 * - Shows banner ads on Radio and Live Stream screens
 * - Handles onboarding flow for first-time users
 * - Manages global loading states and dialogs
 * 
 * The app uses a single-activity architecture with Compose Navigation,
 * where all screens are composables managed by a NavHost.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    // Navigation controller for managing screen transitions
    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context as? Activity
    
    // Preferences manager for storing app state (e.g., onboarding completion)
    val preferencesManager = remember { PreferencesManager.create(context) }
    
    // Global state manager for loading indicator
    val isLoading by LoadingManager.isLoading.collectAsState()

    // Define bottom navigation items with icons and labels
    val home = Screen("radio", "Radio", Icons.Default.Radio)
    val history = Screen("liveStream", "Live Stream", Icons.Default.VideoCameraFront)
    val settings = Screen("settings", "More", Icons.Filled.Menu)
    val bottomNavItems = listOf(home, history, settings)

    // Get current route to determine if bottom navigation should be shown
    // Bottom nav is only visible on main screens (Radio, Live Stream, Settings)
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRouteWrapper = currentBackStackEntry?.toRoute<NavigationWrapper>()
    val currentScreenName = currentRouteWrapper?.screenName
    val shouldShowBottomNav = currentScreenName == RadioScreenRoute::class.java.name || 
                              currentScreenName == LiveStreamScreenRoute::class.java.name || 
                              currentScreenName == SettingsScreenRoute::class.java.name

    // Determine start destination based on onboarding completion status
    // First-time users see onboarding, returning users go directly to Radio screen
    val startDestination = remember {
        if (preferencesManager.isOnboardingCompleted()) {
            NavigationWrapper(
                data = null,
                screenName = RadioScreenRoute::class.java.name
            )
        } else {
            NavigationWrapper(
                data = null,
                screenName = OnboardingScreenRoute::class.java.name
            )
        }
    }

    // Animation duration for screen transitions
    val animationDuration = 500

    // Show global loading indicator when isLoading is true
    if (isLoading) {
        AppLoader()
    }

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            // Only show bottom navigation on main screens
            if (shouldShowBottomNav) {
                Column {
                    // Show banner ad only for Radio and Live Stream screens (not Settings)
                    val shouldShowAd = currentScreenName == RadioScreenRoute::class.java.name || 
                                       currentScreenName == LiveStreamScreenRoute::class.java.name

                    if (shouldShowAd) {
                        // Display banner ad based on configured ad network
                        // Supports AdMob, Meta (Facebook), and Unity Ads
                        when (AppConstants.AD_NETWORK) {
                            AdNetwork.META -> {
                                // Meta (Facebook) Banner Ad
                                AndroidView(
                                    factory = { context ->
                                        MetaAdView(
                                            context,
                                            AppConstants.getBannerAdUnitId(),
                                            MetaAdSize.BANNER_HEIGHT_50
                                        ).apply {
                                            loadAd()
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                            AdNetwork.UNITY -> {
                                // Unity Ads Banner
                                if (activity != null) {
                                    AndroidView(
                                        factory = { ctx ->
                                            BannerView(
                                                activity,
                                                AppConstants.getBannerAdUnitId(),
                                                UnityBannerSize(320, 50)
                                            ).apply {
                                                load()
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }
                            else -> {
                                // AdMob Banner Ad
                                AndroidView(
                                    factory = { context ->
                                        AdView(context).apply {
                                            adUnitId = AppConstants.getBannerAdUnitId()
                                            setAdSize(AdSize.BANNER)
                                            loadAd(AdRequest.Builder().build())
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    NavigationBar {
                        bottomNavItems.forEach { screen ->
                            val targetScreenName = when (screen.route) {
                                home.route -> RadioScreenRoute::class.java.name
                                history.route -> LiveStreamScreenRoute::class.java.name
                                settings.route -> SettingsScreenRoute::class.java.name
                                else -> RadioScreenRoute::class.java.name
                            }
                            
                            val isSelected = currentScreenName == targetScreenName
                            
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.title,
                                        tint = if (isSelected) accent else secondaryTextColor
                                    )
                                },
                                label = {
                                    Text(text = screen.title)
                                },
                                selected = isSelected,
                                onClick = {
                                    // Don't navigate if already on the target screen
                                    if (!isSelected) {
                                        navController.navigate(
                                            NavigationWrapper(
                                                data = null,
                                                screenName = targetScreenName
                                            )
                                        ) {
                                            // Use launchSingleTop to prevent duplicate entries
                                            // This will replace the current screen if it's the same type
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable<NavigationWrapper>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(animationDuration)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(animationDuration)
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(animationDuration)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(animationDuration)
                        )
                    }
                ) {
                    val args = it.toRoute<NavigationWrapper>()

                    when (args.screenName) {
                        OnboardingScreenRoute::class.java.name -> {
                            OnboardingScreen(
                                onFinish = {
                                    // Navigate to radio screen and remove onboarding from stack
                                    navController.navigate(
                                        NavigationWrapper(
                                            data = null,
                                            screenName = RadioScreenRoute::class.java.name
                                        )
                                    ) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        RadioScreenRoute::class.java.name -> {
                            val radioPlayerViewModel: RadioPlayerViewModel = hiltViewModel()
                            RadioScreen(radioPlayerViewModel)
                        }

                        LiveStreamScreenRoute::class.java.name -> {
                            val radioPlayerViewModel: RadioPlayerViewModel = hiltViewModel()
                            LiveSteamScreen(radioPlayerViewModel)
                        }

                        SettingsScreenRoute::class.java.name -> {
                            SettingScreen(navController = navController)
                        }

                        WebViewScreenRoute::class.java.name -> {
                            val data = if (args.data != null) {
                                Gson().fromJson(args.data, WebViewScreenRoute::class.java)
                            } else {
                                throw Exception("WebViewScreenRoute data is null")
                            }
                            WebViewScreen(navController = navController, title = data.title, url = data.url)
                        }

                        else -> {
                            throw IllegalArgumentException("Unknown route: ${args.screenName}")
                        }
                    }
                }
            }
        }
    }
}