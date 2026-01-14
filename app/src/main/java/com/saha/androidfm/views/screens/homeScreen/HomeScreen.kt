package com.saha.androidfm.views.screens.homeScreen

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.saha.androidfm.R
import com.saha.androidfm.data.enums.Screen
import com.saha.androidfm.ui.theme.backgroundColor
import com.saha.androidfm.ui.theme.primaryButtonColor
import com.saha.androidfm.viewmodels.RadioPlayerViewModel
import com.saha.androidfm.views.screens.SettingScreen
import com.saha.androidfm.views.screens.history.HistoryScreen

object HomeScreenRoute

@Composable
fun HomeScreen(navController: NavController) {
    val context: Context = LocalContext.current
    val viewModel: HomeViewModel = hiltViewModel()
    val radioPlayerViewMode: RadioPlayerViewModel = hiltViewModel()

    val home =
        Screen("home", "Sticker Maker", ImageVector.vectorResource(id = R.drawable.ic_home))
    val history = Screen("history", "History", Icons.AutoMirrored.Filled.List)
    val settings = Screen("settings", "Settings", Icons.Default.Settings)

    val items = listOf(
        home, history, settings
    )

    val navControllerBottomNavigation = rememberNavController()

    val currentBackStackEntry by navControllerBottomNavigation.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val animationDuration = 200



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = backgroundColor
            )
    ) {
        // Your screen content goes here

        Scaffold(
            //modifier = Modifier.background(Color.Red),
            containerColor = backgroundColor, bottomBar = {
                NavigationBar(
                    containerColor = backgroundColor,
                    tonalElevation = 4.dp,
                    modifier = Modifier.shadow(
                            elevation = 4.dp, ambientColor = Color.Black.copy(alpha = 0.3f)
                        )
                ) {
                    items.forEach { screen ->
                        NavigationBarItem(icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                modifier = Modifier.size(24.dp),
                                tint = if (currentDestination?.route == screen.route) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }, label = {
                            Text(
                                screen.title,
                                color = if (currentDestination?.route == screen.route) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        },
                        selected = false,
                        onClick = {
                            navControllerBottomNavigation.navigate(screen.route) {
                                popUpTo(navControllerBottomNavigation.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        })
                    }
                }
            }) { paddingValues ->

            NavHost(
                navController = navControllerBottomNavigation,
                startDestination = home.route,
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                composable(home.route, enterTransition = {
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
                    }) { HomeScreenContent(navControllerBottomNavigation, navController, radioPlayerViewMode) }
                composable(history.route, enterTransition = {
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
                    }) { HistoryScreen(navControllerBottomNavigation) }
                composable(settings.route, enterTransition = {
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
                    }) { SettingScreen(navControllerBottomNavigation) }

            }

        }
    }
}
