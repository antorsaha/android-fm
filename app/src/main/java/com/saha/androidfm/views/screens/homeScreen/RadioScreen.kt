package com.saha.androidfm.views.screens.homeScreen

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saha.androidfm.R
import com.saha.androidfm.ui.theme.accent
import com.saha.androidfm.ui.theme.backgroundColor
import com.saha.androidfm.ui.theme.primaryTextColor
import com.saha.androidfm.ui.theme.secondaryTextColor
import com.saha.androidfm.utils.helpers.AppConstants
import com.saha.androidfm.utils.helpers.AppHelper
import com.saha.androidfm.viewmodels.RadioPlayerViewModel
import com.saha.androidfm.views.components.AudioVisualizerBars
import com.saha.androidfm.views.components.CircularAnimatedImage
import com.saha.androidfm.views.components.HeightGap

private const val TAG = "HomeScreenContent"

@Composable
fun RadioScreen(
    navController: NavController,
    parentNavController: NavController,
    radioPlayerViewModel: RadioPlayerViewModel
) {
    val context = LocalContext.current
    val isPlaying by radioPlayerViewModel.isPlaying.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    // Separate state for animations - updates with delay after actual playback state changes
    var isAnimating by remember { mutableStateOf(isPlaying) }
    
    // Update animation state when playback state actually changes
    LaunchedEffect(isPlaying) {
        // Add a small delay to ensure the actual playback state is confirmed
        delay(100) // 100ms delay to match actual playback state
        isAnimating = isPlaying
    }
    
    // painterResource is already optimized and cached by Compose internally
    val imagePainter = painterResource(R.drawable.img_denneryfm)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeightGap(16.dp)

                Text(
                    text = AppConstants.STATION_NAME,
                    style = MaterialTheme.typography.titleMedium,
                    color = secondaryTextColor,
                )

                HeightGap(32.dp)


                CircularAnimatedImage(
                    painter = imagePainter,
                    isPlaying = isAnimating,
                    modifier = Modifier.size(200.dp),
                    imageSize = 200.dp,
                    animationDuration = 5000, // 5 seconds per rotation (slow)
                    repetitions = 2,
                    contentDescription = "Spinning image"
                )

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

            HeightGap(24.dp)

            // Frequency Tuner
            /*FrequencyTuner(
                currentFrequency = currentFrequency,
                minFrequency = 85f,
                maxFrequency = 92f,
                modifier = Modifier.fillMaxWidth()
            )*/

            HeightGap(32.dp)

            // Control Icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Moon Icon (Night Mode)
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = "Night Mode",
                    tint = primaryTextColor,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { /* Handle night mode */ }
                )

                // Play/Pause Button
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            color = accent,
                            shape = CircleShape
                        )
                        .clickable(
                            indication = null, // Remove ripple for instant feedback
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            // Launch in coroutine scope to ensure non-blocking
                            coroutineScope.launch {
                                isAnimating = !isAnimating
                                radioPlayerViewModel.togglePlayPause()
                            }
                        },
                    contentAlignment = Alignment.Center
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
                        }
                )
            }

            HeightGap(32.dp)


        }

    }
}
