package com.saha.androidfm.views.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
    // Store target heights for each bar
    var targetHeights by remember {
        mutableStateOf(List(barCount) { minBarHeight })
    }

    // Remember animation durations for each bar to keep them consistent
    val animationDurations = remember {
        List(barCount) { Random.nextInt(200, 600) }
    }

    // Create animated states for each bar - call animateFloatAsState in composable context
    val animatedHeights = List(barCount) { index ->
        val target = targetHeights[index]
        animateFloatAsState(
            targetValue = target,
            animationSpec = tween(
                durationMillis = animationDurations[index],
                easing = LinearEasing
            ),
            label = "bar_$index"
        )
    }

    // Continuously update target heights when playing
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (true) {
                targetHeights = List(barCount) {
                    Random.nextFloat() * (maxBarHeight - minBarHeight) + minBarHeight
                }
                delay(Random.nextLong(100, 300))
            }
        } else {
            // Animate to minimum height when stopped
            targetHeights = List(barCount) { minBarHeight }
        }
    }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val barWidth = width / barCount
            val spacing = barWidth * 0.15f // 15% spacing between bars
            val actualBarWidth = barWidth - spacing

            animatedHeights.forEachIndexed { index, animatedHeight ->
                val barHeight = animatedHeight.value * height

                val x = index * barWidth + spacing / 2
                // Position bar from bottom - all bars have bottom at same position
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