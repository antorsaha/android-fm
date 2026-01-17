package com.saha.androidfm.views.screens.homeScreen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.VideoCameraFront
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.saha.androidfm.R
import com.saha.androidfm.data.enums.Screen
import com.saha.androidfm.ui.theme.accent
import com.saha.androidfm.ui.theme.backgroundColor
import com.saha.androidfm.ui.theme.secondaryTextColor
import com.saha.androidfm.ui.theme.surface
import com.saha.androidfm.utils.helpers.AppConstants
import com.saha.androidfm.viewmodels.RadioPlayerViewModel
import com.saha.androidfm.views.screens.SettingScreen
import com.saha.androidfm.views.screens.lifeSteam.LiveSteamScreen

object HomeScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    LocalContext.current
    val viewModel: HomeViewModel = hiltViewModel()
    val radioPlayerViewMode: RadioPlayerViewModel = hiltViewModel()

    val home = Screen("home", "Radio", Icons.Default.Radio)
    val history = Screen("history", "Live Stream", Icons.Default.VideoCameraFront)
    val settings = Screen("settings", "More", Icons.Filled.Menu)

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
            containerColor = backgroundColor,
            bottomBar = {
                Column {
                    // Show banner ad only for Radio and Live Stream screens
                    val shouldShowAd = currentDestination?.route == home.route || 
                                      currentDestination?.route == history.route
                    
                    if (shouldShowAd) {
                        AndroidView(
                            factory = { context ->
                                AdView(context).apply {
                                    adUnitId = AppConstants.BANNER_AD_UNIT_ID
                                    setAdSize(AdSize.BANNER)
                                    loadAd(AdRequest.Builder().build())
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    
                    FloatingBottomNavigationBar(
                        items = items,
                        currentRoute = currentDestination?.route,
                        onItemClick = { screen ->
                            navControllerBottomNavigation.navigate(screen.route) {
                                popUpTo(navControllerBottomNavigation.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }) { paddingValues ->

            NavHost(
                navController = navControllerBottomNavigation,
                startDestination = home.route,
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                composable(
                    home.route, enterTransition = {
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
                    }) {
                    RadioScreen(
                        navControllerBottomNavigation,
                        navController,
                        radioPlayerViewMode
                    )
                }
                composable(
                    history.route, enterTransition = {
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
                    }) { LiveSteamScreen(navControllerBottomNavigation) }
                composable(
                    settings.route, enterTransition = {
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
                    }) { SettingScreen(navController,navControllerBottomNavigation) }

            }

        }
    }
}

@Composable
fun FloatingBottomNavigationBar(
    items: List<Screen>,
    currentRoute: String?,
    onItemClick: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 8.dp)
            .padding(bottom = 16.dp)
            .height(64.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(35.dp),
                ambientColor = Color.Black.copy(alpha = 0.3f),
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(35.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        surface.copy(alpha = 0.95f),
                        surface.copy(alpha = 0.9f)
                    )
                )
            )
            .border(width = 0.3.dp, color = secondaryTextColor, shape = RoundedCornerShape(35.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { screen ->
                FloatingNavItem(
                    screen = screen,
                    isSelected = currentRoute == screen.route,
                    onClick = { onItemClick(screen) }
                )
            }
        }
    }
}

@Composable
fun FloatingNavItem(
    screen: Screen,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) accent else secondaryTextColor,
        animationSpec = tween(durationMillis = 200),
        label = "iconColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) accent else secondaryTextColor,
        animationSpec = tween(durationMillis = 200),
        label = "textColor"
    )

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = screen.icon,
            contentDescription = screen.title,
            modifier = Modifier.size(24.dp),
            tint = iconColor
        )
        Text(
            text = screen.title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
