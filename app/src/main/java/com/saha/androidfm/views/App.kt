package com.saha.androidfm.views

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.gson.Gson
import com.saha.androidfm.utils.helpers.ConfirmationDialogSpec
import com.saha.androidfm.utils.helpers.DialogManager
import com.saha.androidfm.utils.helpers.ErrorDialogSpec
import com.saha.androidfm.utils.helpers.LoadingManager
import com.saha.androidfm.utils.helpers.PreferencesManager
import com.saha.androidfm.utils.helpers.SuccessDialogSpec
import com.saha.androidfm.utils.navigation.NavigationWrapper
import com.saha.androidfm.views.screens.onboarding.OnboardingViewModel
import com.saha.androidfm.views.dialogs.AppLoader
import com.saha.androidfm.views.dialogs.IosConfirmationDialog
import com.saha.androidfm.views.dialogs.IosErrorDialog
import com.saha.androidfm.views.dialogs.IosSuccessDialog
import com.saha.androidfm.views.screens.WebViewScreen
import com.saha.androidfm.views.screens.WebViewScreenRoute
import com.saha.androidfm.views.screens.lifeSteam.LiveSteamScreen
import com.saha.androidfm.views.screens.homeScreen.HomeScreen
import com.saha.androidfm.views.screens.homeScreen.HomeScreenRoute
import com.saha.androidfm.views.screens.onboarding.OnboardingScreen
import com.saha.androidfm.views.screens.onboarding.OnboardingScreenRoute
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager.create(context) }
    val isLoading by LoadingManager.isLoading.collectAsState()
    val centralDialogSpec by DialogManager.dialog.collectAsState()

    // Determine start destination based on onboarding completion
    val startDestination = remember {
        if (preferencesManager.isOnboardingCompleted()) {
            NavigationWrapper(
                data = null,
                screenName = HomeScreenRoute::class.java.name
            )
        } else {
            NavigationWrapper(
                data = null,
                screenName = OnboardingScreenRoute::class.java.name
            )
        }
    }

    val animationDuration = 500

    if (isLoading) {
        AppLoader()
    }

    // Show dialogs when there's a request from DialogManager
    centralDialogSpec?.let { spec ->
        when (spec) {
            is SuccessDialogSpec -> {
                IosSuccessDialog(
                    spec = spec,
                    onDismiss = {
                        DialogManager.dismiss()
                        spec.onDismiss?.invoke()
                    }
                )
            }

            is ErrorDialogSpec -> {
                IosErrorDialog(
                    spec = spec,
                    onDismiss = {
                        DialogManager.dismiss()
                        spec.onDismiss?.invoke()
                    }
                )
            }

            is ConfirmationDialogSpec -> {
                IosConfirmationDialog(
                    spec = spec,
                    onDismiss = {
                        DialogManager.dismiss()
                        spec.onDismiss?.invoke()
                    }
                )
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("profile") {
            LiveSteamScreen(navController)
        }

        composable<NavigationWrapper>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            }, exitTransition = {
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
            }, popExitTransition = {
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
                        navController = navController,
                        onFinish = {
                            // Navigate to home and remove onboarding from stack
                            navController.navigate(
                                NavigationWrapper(
                                    data = null,
                                    screenName = HomeScreenRoute::class.java.name
                                )
                            ) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                HomeScreenRoute::class.java.name -> {
                    HomeScreen(navController)
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