package com.saha.androidfm.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saha.androidfm.ui.theme.*
import com.saha.androidfm.ui.theme.white
import com.saha.androidfm.viewmodels.RadioPlayerViewModel

@Composable
fun RadioPlayer(
    viewModel: RadioPlayerViewModel,
    modifier: Modifier = Modifier
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentUrl by viewModel.currentUrl.collectAsState()
    val stationName by viewModel.stationName.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val playbackError by viewModel.playbackError.collectAsState()
    
    var urlInput by remember { mutableStateOf("") }
    var showUrlInput by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(surface)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Station Info Card
        StationInfoCard(
            stationName = stationName ?: "No Station",
            isLoading = isLoading,
            errorMessage = errorMessage ?: playbackError
        )
        
        // URL Input Section
        if (showUrlInput) {
            UrlInputSection(
                urlInput = urlInput,
                onUrlInputChange = { urlInput = it },
                onPlayClick = {
                    if (urlInput.isNotBlank()) {
                        viewModel.play(urlInput, "Custom Station")
                        showUrlInput = false
                    }
                },
                onClear = {
                    urlInput = ""
                },
                onDismiss = { showUrlInput = false }
            )
        }
        
        // Player Controls
        PlayerControls(
            isPlaying = isPlaying,
            isLoading = isLoading,
            onPlayPause = { viewModel.togglePlayPause() },
            onStop = { viewModel.stop() },
            onAddUrl = { showUrlInput = true },
            hasActiveStream = currentUrl != null
        )
    }
}

@Composable
private fun StationInfoCard(
    stationName: String,
    isLoading: Boolean,
    errorMessage: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Station Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Radio,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Station Name
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stationName,
                    style = MaterialTheme.typography.titleMedium,
                    color = primaryTextColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = accentRed,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else if (isLoading) {
                    Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.bodySmall,
                        color = secondaryTextColor,
                        maxLines = 1
                    )
                }
            }
            
            // Loading Indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = accent,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UrlInputSection(
    urlInput: String,
    onUrlInputChange: (String) -> Unit,
    onPlayClick: () -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Enter Stream URL or File Path",
                    style = MaterialTheme.typography.titleSmall,
                    color = primaryTextColor,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = primaryTextColor
                    )
                }
            }
            
            OutlinedTextField(
                value = urlInput,
                onValueChange = onUrlInputChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "http://example.com/stream or /path/to/file.m3u",
                        color = secondaryTextColor
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = primaryTextColor,
                    unfocusedTextColor = primaryTextColor,
                    focusedBorderColor = accent,
                    unfocusedBorderColor = secondaryTextColor.copy(alpha = 0.5f),
                    cursorColor = accent
                ),
                singleLine = true
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                Button(
                    onClick = onClear,
                    enabled = urlInput.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accent
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("clear")
                }


                Button(
                    onClick = onPlayClick,
                    enabled = urlInput.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accent
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Play")
                }
            }
        }
    }
}

@Composable
private fun PlayerControls(
    isPlaying: Boolean,
    isLoading: Boolean,
    onPlayPause: () -> Unit,
    onStop: () -> Unit,
    onAddUrl: () -> Unit,
    hasActiveStream: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Add URL Button
        IconButton(
            onClick = onAddUrl,
            modifier = Modifier.size(50.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add URL",
                tint = primaryTextColor.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Stop Button
        IconButton(
            onClick = onStop,
            enabled = hasActiveStream,
            modifier = Modifier.size(60.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = "Stop",
                tint = if (hasActiveStream) primaryTextColor else primaryTextColor.copy(alpha = 0.3f),
                modifier = Modifier.size(28.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(20.dp))
        
        // Play/Pause Button
        Button(
            onClick = onPlayPause,
            enabled = hasActiveStream || !isLoading,
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                purpleGradientStart,
                                purpleGradientEnd
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = white,
                        strokeWidth = 3.dp
                    )
                } else {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = white,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.width(20.dp))
        
        // Placeholder for symmetry
        Spacer(modifier = Modifier.size(60.dp))
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Placeholder for symmetry
        Spacer(modifier = Modifier.size(50.dp))
    }
}
