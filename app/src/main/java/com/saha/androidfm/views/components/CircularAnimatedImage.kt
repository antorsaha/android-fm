package com.saha.androidfm.views.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun CircularAnimatedImage(
    painter: Painter,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    imageSize: Dp = 64.dp,
    animationDuration: Int = 3000, // Slow animation (3 seconds per circle)
    repetitions: Int = 2,
    contentDescription: String? = null
) {
    var currentRotation by remember { mutableStateOf(0f) }
    var animationStartTime by remember { mutableStateOf(0L) }
    var isAnimating by remember { mutableStateOf(false) }
    
    // Single LaunchedEffect to handle both reset and animation
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            // Start new animation cycle - reset rotation when starting
            currentRotation = 0f
            animationStartTime = System.currentTimeMillis()
            isAnimating = true
            
            val totalDuration = animationDuration * repetitions
            val totalDegrees = 360f * repetitions // 2 full rotations = 720 degrees
            
            while (isActive && isAnimating) {
                val elapsed = System.currentTimeMillis() - animationStartTime
                val progress = (elapsed.toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f)
                
                currentRotation = totalDegrees * progress
                
                // Check if we've completed all rotations
                if (progress >= 1f) {
                    isAnimating = false
                    currentRotation = totalDegrees // Final position
                    break
                }

                // Update at ~30fps - smooth enough for rotation animation
                delay(33)
            }
        } else {
            // When pausing, immediately stop animation and reset
            isAnimating = false
            currentRotation = 0f
        }
    }
    
    // When paused, target is always 0f (original image)
    // When playing, target is currentRotation
    val rotationTarget = if (isPlaying && isAnimating) currentRotation else 0f
    
    // Animate rotation smoothly when playing, snap when pausing
    val animatedRotation by animateFloatAsState(
        targetValue = rotationTarget,
        animationSpec = if (isPlaying && isAnimating) {
            // Use short duration for responsive animation
            tween(
                durationMillis = 50,
                easing = androidx.compose.animation.core.LinearEasing
            )
        } else {
            // When paused, use snap for instant reset to 0
            snap<Float>()
        },
        label = "rotation_animation"
    )
    
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(imageSize)
                .rotate(animatedRotation)
        )
    }
}
