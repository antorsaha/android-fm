package com.saha.androidfm.views.screens.homeScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saha.androidfm.R
import com.saha.androidfm.ui.theme.accent
import com.saha.androidfm.ui.theme.backgroundColor
import com.saha.androidfm.ui.theme.primaryTextColor
import com.saha.androidfm.ui.theme.secondaryTextColor
import com.saha.androidfm.utils.helpers.AppHelper
import com.saha.androidfm.utils.helpers.createAdManager
import com.saha.androidfm.viewmodels.RadioPlayerViewModel
import com.saha.androidfm.views.components.AudioVisualizerBars
import com.saha.androidfm.views.components.HeightGap
import com.saha.androidfm.views.components.WidthGap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// Route objects for navigation
object RadioScreenRoute
object LiveStreamScreenRoute
object SettingsScreenRoute

@SuppressLint("DefaultLocale")
@Composable
fun RadioScreen(
    radioPlayerViewModel: RadioPlayerViewModel
) {
    val context = LocalContext.current
    val activity = context as? android.app.Activity
    val isPlaying by radioPlayerViewModel.isPlaying.collectAsState()
    val sleepTimerRemaining by radioPlayerViewModel.sleepTimerRemainingMillis.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Separate state for animations - updates with delay after actual playback state changes
    var isAnimating by remember { mutableStateOf(isPlaying) }
    var showSleepTimerCover by remember { mutableStateOf(false) }

    // Initialize unified Ad Manager (supports both AdMob and Meta)
    val adManager = remember { createAdManager(context) }

    // Load ad when screen is created
    LaunchedEffect(Unit) {
        adManager.loadAd()
    }

    // Update animation state when playback state actually changes
    LaunchedEffect(isPlaying) {
        // Add a small delay to ensure the actual playback state is confirmed
        delay(100) // 100ms delay to match actual playback state
        isAnimating = isPlaying
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()

        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeightGap(16.dp)

                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                    color = secondaryTextColor,
                )

                HeightGap(56.dp)


                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(136.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = backgroundColor,
                    tonalElevation = 6.dp,   // Material 3
                    shadowElevation = 6.dp,
                    border = BorderStroke(0.2.dp, secondaryTextColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.icon_image),
                            contentDescription = "App Icon",
                            modifier = Modifier.size(120.dp)

                        )

                        WidthGap(16.dp)

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            HeightGap(4.dp)

                            Text(
                                text = stringResource(R.string.app_description),
                                style = MaterialTheme.typography.bodyMedium,
                                color = secondaryTextColor
                            )
                        }


                    }

                }


                if (sleepTimerRemaining != null) {
                    HeightGap(16.dp)
                    val minutes = (sleepTimerRemaining!! / 1000) / 60
                    val seconds = (sleepTimerRemaining!! / 1000) % 60
                    Text(
                        text = String.format("Sleep timer: %02d:%02d", minutes, seconds),
                        style = MaterialTheme.typography.bodyMedium,
                        color = accent,
                        fontWeight = FontWeight.Bold
                    )
                }

            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            // Audio Visualizer Bars
            AudioVisualizerBars(
                isPlaying = isAnimating,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 24.dp)
            )

            HeightGap(40.dp)

            // Control Icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Moon Icon (Sleep Timer)
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = "Sleep Timer",
                    tint = primaryTextColor,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { showSleepTimerCover = true })

                // Play/Pause Button
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            color = accent, shape = CircleShape
                        )
                        .clickable(
                            indication = null, // Remove ripple for instant feedback
                            interactionSource = remember { MutableInteractionSource() }) {
                            // Launch in coroutine scope to ensure non-blocking
                            coroutineScope.launch {
                                if (isPlaying) {
                                    // If already playing, just pause (no ad needed)
                                    isAnimating = false
                                    radioPlayerViewModel.togglePlayPause()
                                } else {
                                    // If not playing, show ad first, then play after ad closes
                                    activity?.let {
                                        adManager.showAd(it) {
                                            // This callback executes after ad is closed
                                            // Now play the audio
                                            coroutineScope.launch {
                                                isAnimating = true
                                                radioPlayerViewModel.togglePlayPause()
                                            }
                                        }
                                    } ?: run {
                                        // If no activity context, play directly
                                        isAnimating = true
                                        radioPlayerViewModel.togglePlayPause()
                                    }
                                }
                            }
                        }, contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isAnimating) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = primaryTextColor,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Share Icon
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share App",
                    tint = primaryTextColor,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            AppHelper.shareApp(context)
                        })
            }

            HeightGap(32.dp)

        }

        // Sleep Timer Full Screen Cover
        AnimatedVisibility(
            visible = showSleepTimerCover, enter = fadeIn(), exit = fadeOut()
        ) {
            SleepTimerCover(
                onClose = { showSleepTimerCover = false },
                onOptionSelected = { minutes ->
                    radioPlayerViewModel.setSleepTimer(minutes)
                    showSleepTimerCover = false
                })
        }

    }
}

@Composable
fun SleepTimerCover(
    onClose: () -> Unit, onOptionSelected: (Int) -> Unit
) {
    val timerOptions = listOf(
        "Off" to 0,
        "15 minutes" to 15,
        "30 minutes" to 30,
        "45 minutes" to 45,
        "60 minutes" to 60,
        "90 minutes" to 90
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor.copy(alpha = 0.95f))
            .clickable(enabled = true, onClick = onClose) // Close on background click
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = primaryTextColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Set Sleep Timer",
                style = MaterialTheme.typography.headlineMedium,
                color = primaryTextColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(48.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                    .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(timerOptions) { (label, minutes) ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(minutes) }
                        .padding(vertical = 16.dp, horizontal = 24.dp),
                        contentAlignment = Alignment.Center) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleLarge,
                            color = primaryTextColor,
                            fontSize = 20.sp
                        )
                    }
                    if (timerOptions.last().first != label) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 32.dp),
                            thickness = 0.5.dp,
                            color = secondaryTextColor.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}
