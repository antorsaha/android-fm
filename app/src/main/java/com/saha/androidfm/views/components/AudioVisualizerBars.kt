package com.saha.androidfm.views.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.saha.androidfm.ui.theme.accent
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun AudioVisualizerBars(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    barCount: Int = 50,
    barColor: Color = accent,
    minBarHeight: Float = 0.1f,
    maxBarHeight: Float = 1.0f
) {
    // Use FloatArray for better performance - avoids list allocations
    var currentHeights by remember {
        mutableStateOf(FloatArray(barCount) { minBarHeight })
    }

    // Store target heights for smooth interpolation
    var targetHeights by remember {
        mutableStateOf(FloatArray(barCount) { minBarHeight })
    }

    // Smooth animation interpolation factor
    val animationSpeed = 0.15f
    var lastTargetUpdate by remember { mutableStateOf(0L) }
    var lastFrameTime by remember { mutableStateOf(0L) }

    // Continuously update heights when playing - using smooth interpolation
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            lastTargetUpdate = System.currentTimeMillis()
            lastFrameTime = System.currentTimeMillis()
            // Initialize target heights
            val newTargets = FloatArray(barCount) {
                Random.nextFloat() * (maxBarHeight - minBarHeight) + minBarHeight
            }
            targetHeights = newTargets
            
            while (isActive) {
                val now = System.currentTimeMillis()
                val frameDelta = now - lastFrameTime
                lastFrameTime = now
                
                // Update target heights every 150-250ms (less frequent updates)
                if (now - lastTargetUpdate > 200) {
                    val newTargets = FloatArray(barCount) {
                        Random.nextFloat() * (maxBarHeight - minBarHeight) + minBarHeight
                    }
                    targetHeights = newTargets
                    lastTargetUpdate = now
                }
                
                // Smoothly interpolate current heights towards targets
                // Use FloatArray operations for better performance
                val newHeights = FloatArray(barCount) { index ->
                    val current = currentHeights[index]
                    val target = targetHeights[index]
                    val diff = target - current
                    // Smooth interpolation with frame-time aware speed
                    if (abs(diff) > 0.01f) {
                        current + diff * animationSpeed
                    } else {
                        target
                    }
                }
                currentHeights = newHeights
                
                // Update at ~30fps instead of 60fps to reduce recomposition overhead
                // Still smooth enough for visual effect
                delay(33)
            }
        } else {
            // When paused, quickly reset to minimum (fewer frames for faster response)
            var hasChanges = true
            var iterations = 0
            while (isActive && hasChanges && iterations < 10) { // Limit iterations to avoid blocking
                hasChanges = false
                val newHeights = FloatArray(barCount) { index ->
                    val current = currentHeights[index]
                    val diff = minBarHeight - current
                    if (abs(diff) > 0.01f) {
                        hasChanges = true
                        current + diff * (animationSpeed * 2f) // Faster when pausing
                    } else {
                        minBarHeight
                    }
                }
                currentHeights = newHeights
                iterations++
                if (hasChanges) {
                    delay(16)
                }
            }
            // Final reset - instant
            currentHeights = FloatArray(barCount) { minBarHeight }
            targetHeights = FloatArray(barCount) { minBarHeight }
        }
    }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val barWidth = width / barCount
            val spacing = barWidth * 0.15f // 15% spacing between bars
            val actualBarWidth = barWidth - spacing

            // Direct array access for better performance
            for (index in 0 until barCount) {
                val barHeightRatio = currentHeights[index]
                val barHeight = barHeightRatio * height

                val x = index * barWidth + spacing / 2
                // Position bar from bottom
                val barTop = height - barHeight

                // Draw bar
                drawRect(
                    color = barColor,
                    topLeft = Offset(x, barTop),
                    size = Size(actualBarWidth, barHeight)
                )
            }
        }
    }
}