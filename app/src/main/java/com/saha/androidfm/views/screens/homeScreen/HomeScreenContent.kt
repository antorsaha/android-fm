package com.saha.androidfm.views.screens.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saha.androidfm.R
import com.saha.androidfm.ui.theme.accent
import com.saha.androidfm.ui.theme.backgroundColor
import com.saha.androidfm.ui.theme.primaryTextColor
import com.saha.androidfm.ui.theme.secondaryTextColor
import com.saha.androidfm.ui.theme.surface
import com.saha.androidfm.utils.helpers.AppConstants
import com.saha.androidfm.viewmodels.RadioPlayerViewModel
import com.saha.androidfm.views.components.AudioVisualizerBars
import com.saha.androidfm.views.components.HeightGap
import com.saha.androidfm.views.components.WidthGap

private const val TAG = "HomeScreenContent"

@Composable
fun HomeScreenContent(
    navController: NavController,
    parentNavController: NavController,
    radioPlayerViewModel: RadioPlayerViewModel
) {
    LocalContext.current
    val isPlaying by radioPlayerViewModel.isPlaying.collectAsState()
    AppConstants.STATION_FREQUENCY.toFloatOrNull() ?: 88.9f

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
                HeightGap(8.dp)

                Text(
                    text = stringResource(R.string.radio),
                    style = MaterialTheme.typography.titleMedium,
                    color = secondaryTextColor,
                    fontWeight = FontWeight.Normal
                )

                HeightGap(4.dp)
                Text(
                    text = AppConstants.STATION_FREQUENCY,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 48.sp
                )

                HeightGap(8.dp)

                Text(
                    text = stringResource(R.string.mhz),
                    style = MaterialTheme.typography.titleMedium,
                    color = secondaryTextColor,
                    fontWeight = FontWeight.Normal
                )

                HeightGap(32.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = surface,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )

                    WidthGap(4.dp)

                    Text(
                        text = AppConstants.STATION_NAME,
                        style = MaterialTheme.typography.bodyMedium
                    )

                }
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            // Audio Visualizer Bars
            AudioVisualizerBars(
                isPlaying = isPlaying,
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
                        .clickable {
                            radioPlayerViewModel.togglePlayPause()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = primaryTextColor,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Share Icon
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = primaryTextColor,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { /* Handle share */ }
                )
            }

            HeightGap(32.dp)


        }

    }
}
