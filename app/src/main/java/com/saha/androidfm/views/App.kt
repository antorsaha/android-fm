package com.saha.androidfm.views

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.saha.androidfm.utils.helpers.ConfirmationDialogSpec
import com.saha.androidfm.utils.helpers.DialogManager
import com.saha.androidfm.utils.helpers.ErrorDialogSpec
import com.saha.androidfm.utils.helpers.LoadingManager
import com.saha.androidfm.utils.helpers.SuccessDialogSpec
import com.saha.androidfm.utils.navigation.NavigationWrapper
import com.saha.androidfm.views.dialogs.AppLoader
import com.saha.androidfm.views.dialogs.IosConfirmationDialog
import com.saha.androidfm.views.dialogs.IosErrorDialog
import com.saha.androidfm.views.dialogs.IosSuccessDialog
import com.saha.androidfm.views.screens.lifeSteam.LiveSteamScreen
import com.saha.androidfm.views.screens.homeScreen.HomeScreen
import com.saha.androidfm.views.screens.homeScreen.HomeScreenRoute

@Composable
fun App() {
    val navController = rememberNavController()
    val isLoading by LoadingManager.isLoading.collectAsState()
    val centralDialogSpec by DialogManager.dialog.collectAsState()

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
        navController = navController, startDestination = NavigationWrapper(
            data = null, screenName = HomeScreenRoute::class.java.name
        )
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
                HomeScreenRoute::class.java.name -> {
                    HomeScreen(navController)
                }

                /*TestScreen::class.java.name -> {
                    val stickerPack = remember {
                        StickerPack(
                            identifier = "sample_sticker_pack3",
                            name = "ai sticker3",
                            publisher = "ai antor publisher",
                            trayImageFile = "sticker_1.webp",
                            imageDataVersion = "1",
                            stickers = listOf(
                                Sticker(
                                    imageFileName = "sticker_1.webp",
                                    emojis = listOf("😀"),
                                    accessibilityText = "Sticker 1",
                                    drawableResId = R.drawable.sticker_1
                                ),
                                Sticker(
                                    imageFileName = "sticker_2.webp",
                                    emojis = listOf("😃"),
                                    accessibilityText = "Sticker 2",
                                    drawableResId = R.drawable.sticker_2
                                ),
                                Sticker(
                                    imageFileName = "sticker_3.webp",
                                    emojis = listOf("😄"),
                                    accessibilityText = "Sticker 3",
                                    drawableResId = R.drawable.sticker_3
                                ),
                                *//*Sticker(
                                    imageFileName = "sticker_4.webp",
                                    emojis = listOf("😁"),
                                    accessibilityText = "Sticker 4",
                                    drawableResId = R.drawable.sticker_4
                                ),
                                Sticker(
                                    imageFileName = "sticker_5.webp",
                                    emojis = listOf("😆"),
                                    accessibilityText = "Sticker 5",
                                    drawableResId = R.drawable.sticker_5
                                ),
                                Sticker(
                                    imageFileName = "sticker_6.webp",
                                    emojis = listOf("😅"),
                                    accessibilityText = "Sticker 6",
                                    drawableResId = R.drawable.sticker_6
                                ),
                                Sticker(
                                    imageFileName = "sticker_7.webp",
                                    emojis = listOf("😂"),
                                    accessibilityText = "Sticker 7",
                                    drawableResId = R.drawable.sticker_7
                                ),
                                Sticker(
                                    imageFileName = "sticker_9.webp",
                                    emojis = listOf("🤣"),
                                    accessibilityText = "Sticker 9",
                                    drawableResId = R.drawable.sticker_9
                                ),
                                Sticker(
                                    imageFileName = "sticker_10.webp",
                                    emojis = listOf("😊"),
                                    accessibilityText = "Sticker 10",
                                    drawableResId = R.drawable.sticker_10
                                ),
                                Sticker(
                                    imageFileName = "sticker_11.webp",
                                    emojis = listOf("😇"),
                                    accessibilityText = "Sticker 11",
                                    drawableResId = R.drawable.sticker_11
                                ),
                                Sticker(
                                    imageFileName = "sticker_12.webp",
                                    emojis = listOf("🙂"),
                                    accessibilityText = "Sticker 12",
                                    drawableResId = R.drawable.sticker_12
                                ),
                                Sticker(
                                    imageFileName = "sticker_13.webp",
                                    emojis = listOf("🙃"),
                                    accessibilityText = "Sticker 13",
                                    drawableResId = R.drawable.sticker_13
                                ),
                                Sticker(
                                    imageFileName = "sticker_14.webp",
                                    emojis = listOf("😉"),
                                    accessibilityText = "Sticker 14",
                                    drawableResId = R.drawable.sticker_14
                                ),
                                Sticker(
                                    imageFileName = "sticker_15.webp",
                                    emojis = listOf("😌"),
                                    accessibilityText = "Sticker 15",
                                    drawableResId = R.drawable.sticker_15
                                ),
                                Sticker(
                                    imageFileName = "sticker_16.webp",
                                    emojis = listOf("😍"),
                                    accessibilityText = "Sticker 16",
                                    drawableResId = R.drawable.sticker_16
                                )*//*
                            )
                        )
                    }

                    StickerPackDetailsScreen(
                        stickerPack = stickerPack,
                        onBackClick = { navController.popBackStack() }
                    )
                }*/

                else -> {
                    throw IllegalArgumentException("Unknown route: ${args.screenName}")
                }

            }

        }

    }

}