@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.saha.androidfm.views.screens.lifeSteam

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import android.net.Uri
import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.saha.androidfm.R
import com.saha.androidfm.ui.theme.accent
import com.saha.androidfm.ui.theme.backgroundColor
import com.saha.androidfm.ui.theme.primaryTextColor
import com.saha.androidfm.ui.theme.secondaryTextColor
import com.saha.androidfm.utils.helpers.AppConstants
import com.saha.androidfm.utils.helpers.M3UParser
import com.saha.androidfm.views.components.HeightGap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@ExperimentalMaterial3Api
@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)
@Composable
fun LiveSteamScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity
    
    // State for video player
    var exoPlayer: ExoPlayer? by remember { mutableStateOf(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var isFullscreen by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var showMuteOverlay by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var retryKey by remember { mutableStateOf(0) }
    var isStreamAvailable by remember { mutableStateOf(false) }
    
    // Helper function to create MediaSource with proper configuration
    @OptIn(UnstableApi::class)
    fun createMediaSource(ctx: android.content.Context, uri: Uri): MediaSource {
        // Create HTTP data source factory with proper headers for streaming
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("ExoPlayer/Media3")
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(30000) // 30 seconds
            .setReadTimeoutMs(30000) // 30 seconds
            .setDefaultRequestProperties(
                mapOf(
                    "Connection" to "keep-alive",
                    "Accept" to "*/*",
                    "User-Agent" to "ExoPlayer/Media3"
                )
            )
        
        // Create default data source factory
        val dataSourceFactory = DefaultDataSource.Factory(ctx, httpDataSourceFactory)
        
        // Create progressive media source for HTTP streaming
        val mediaItem = MediaItem.Builder()
            .setUri(uri)
            .build()
        
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
    }
    
    // Initialize ExoPlayer with proper MediaSource for video streaming
    LaunchedEffect(retryKey) {
        try {
            val streamUrl = AppConstants.LIVE_STREAM_VIDEO_URL
            Log.d("LiveStreamScreen", "Loading stream URL: $streamUrl")
            
            var actualStreamUrl = streamUrl
            
            // Check if URL is M3U playlist (needs parsing) or direct stream
            if (streamUrl.endsWith(".m3u") || streamUrl.endsWith(".m3u8")) {
                Log.d("LiveStreamScreen", "M3U playlist detected - parsing...")
                isLoading = true
                
                // Parse M3U playlist to get actual stream URL
                try {
                    val m3uContent = withContext(Dispatchers.IO) {
                        java.net.URL(streamUrl).openStream().bufferedReader().use { it.readText() }
                    }
                    val streams = M3UParser.parseM3UContent(m3uContent)
                    
                    if (streams.isNotEmpty()) {
                        actualStreamUrl = streams[0].url
                        Log.d("LiveStreamScreen", "Found ${streams.size} streams, using first: $actualStreamUrl")
                    } else {
                        errorMessage = "No streams found in M3U playlist"
                        isLoading = false
                        return@LaunchedEffect
                    }
                } catch (e: Exception) {
                    errorMessage = "Failed to parse M3U playlist: ${e.message}"
                    isLoading = false
                    Log.e("LiveStreamScreen", "Error parsing M3U", e)
                    return@LaunchedEffect
                }
            }
            
            val player = ExoPlayer.Builder(context)
                .build()
                .apply {
                    // Create MediaSource for better streaming support
                    val uri = actualStreamUrl.toUri()
                    Log.d("LiveStreamScreen", "Creating MediaSource for URI: $uri")
                    val mediaSource = createMediaSource(context, uri)
                    
                    setMediaSource(mediaSource)
                    prepare()
                    playWhenReady = false
                    volume = if (isMuted) 0f else 1f
                    
                    addListener(object : Player.Listener {
                        override fun onIsPlayingChanged(playing: Boolean) {
                            isPlaying = playing
                        }
                        
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            when (playbackState) {
                                Player.STATE_BUFFERING -> {
                                    isLoading = true
                                    errorMessage = null
                                    isStreamAvailable = false
                                }
                                Player.STATE_READY -> {
                                    isLoading = false
                                    errorMessage = null
                                    isStreamAvailable = true
                                    Log.d("LiveStreamScreen", "Stream is available and ready")
                                }
                                Player.STATE_IDLE -> {
                                    isLoading = false
                                    isStreamAvailable = false
                                }
                                Player.STATE_ENDED -> {
                                    isLoading = false
                                    isStreamAvailable = false
                                }
                            }
                        }
                        
                        override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                            isLoading = false
                            isStreamAvailable = false
                            val errorType = when {
                                error.errorCode in 2000..2999 -> "Network error - stream may not be available"
                                error.errorCode in 4000..4999 -> "Stream format not supported"
                                else -> "Stream not available"
                            }
                            errorMessage = errorType
                            Log.e("LiveStreamScreen", "Player error: ${error.message}", error)
                        }
                    })
                }
            exoPlayer = player
        } catch (e: Exception) {
            isLoading = false
            errorMessage = "Failed to initialize player: ${e.message}"
            Log.e("LiveStreamScreen", "Error initializing player", e)
        }
    }
    
    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer?.release()
        }
    }
    
    // Handle fullscreen
    LaunchedEffect(isFullscreen) {
        activity?.let { act ->
            if (isFullscreen) {
                act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                act.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                
                // Use WindowInsetsController for API 30+ (replaces deprecated systemUiVisibility)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    act.window.insetsController?.let { controller ->
                        controller.hide(WindowInsets.Type.systemBars())
                        controller.systemBarsBehavior = 
                            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    }
                } else {
                    @Suppress("DEPRECATION")
                    act.window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
                }
            } else {
                act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                act.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                
                // Use WindowInsetsController for API 30+ (replaces deprecated systemUiVisibility)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    act.window.insetsController?.show(WindowInsets.Type.systemBars())
                } /*else {
                    @Suppress("DEPRECATION")
                    act.window.decorView.systemUiVisibility = View.SYSTEM_UI_VISIBLE
                }*/
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeightGap(16.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.live_stream),
                    style = MaterialTheme.typography.titleMedium,
                    color = secondaryTextColor,
                )
            }
            HeightGap(24.dp)

            
            // Video Player
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .aspectRatio(16f / 11f)
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                when {
                    errorMessage != null || (!isLoading && !isStreamAvailable) -> {
                        // Show stream not available message
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = "Stream not available",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.steam_not_available_message),
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )

                        }
                    }
                    isLoading -> {
                        // Show loading indicator
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                color = accent
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading stream...",
                                color = Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    isStreamAvailable && exoPlayer != null -> {
                        // Show video player when stream is available
                        AndroidView(
                            factory = { ctx ->
                                PlayerView(ctx).apply {
                                    this.player = exoPlayer
                                    useController = true
                                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                                    
                                    // Customize controller
                                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                            update = { view ->
                                view.player = exoPlayer
                            }
                        )
                    }
                    else -> {
                        // Fallback: show not available
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = "No stream",
                                tint = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No stream available",
                                color = Color.White.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                
                // Fullscreen button overlay
                IconButton(
                    onClick = { isFullscreen = !isFullscreen },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                        contentDescription = if (isFullscreen) "Exit Fullscreen" else "Fullscreen",
                        tint = Color.White
                    )
                }
                
                // Mute/Unmute button overlay (only show when muted or when user taps)
                if (isMuted || showMuteOverlay) {
                    IconButton(
                        onClick = {
                            isMuted = !isMuted
                            exoPlayer?.volume = if (isMuted) 0f else 1f
                            showMuteOverlay = false
                        },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = if (isMuted) "Unmute" else "Mute",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
            
            if (!isFullscreen) {
                Spacer(modifier = Modifier.height(24.dp))
                
                // LIVE Badge and Title
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // LIVE Badge - show status
                    Surface(
                        modifier = Modifier.padding(bottom = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = if (isStreamAvailable) accent else Color.Gray
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        if (isStreamAvailable) Color.Red else Color.White.copy(alpha = 0.5f), 
                                        CircleShape
                                    )
                            )
                            Text(
                                text = "LIVE",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                    
                    // Title
                    Text(
                        text = "Dennery FM Live Stream",
                        color = primaryTextColor,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Subtitle
                    Text(
                        text = "Watch live broadcasts and special events",
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Bottom Controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Refresh/Loop Button
                    IconButton(
                        onClick = {
                            exoPlayer?.seekTo(0)
                            exoPlayer?.play()
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    // Play/Pause Button
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(accent, CircleShape)
                            .clickable {
                                if (isPlaying) {
                                    exoPlayer?.pause()
                                } else {
                                    exoPlayer?.play()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    
                    // Volume Button
                    IconButton(
                        onClick = {
                            isMuted = !isMuted
                            exoPlayer?.volume = if (isMuted) 0f else 1f
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = if (isMuted) "Unmute" else "Mute",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}
