package com.saha.androidfm.views.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FrequencyTuner(
    currentFrequency: Float,
    minFrequency: Float,
    maxFrequency: Float,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val padding = 16.dp

    Box(
        modifier = modifier
            .height(120.dp)
            .padding(vertical = 16.dp)
    ) {
        // Draw background lines and ticks
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val paddingPx = padding.toPx()

            // Calculate frequency range
            val frequencyRange = maxFrequency - minFrequency
            val frequencyPosition =
                ((currentFrequency - minFrequency) / frequencyRange) * (width - 2 * paddingPx) + paddingPx

            // Draw horizontal line
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(paddingPx, height / 2),
                end = Offset(width - paddingPx, height / 2),
                strokeWidth = 2.dp.toPx()
            )

            // Draw numbers and tick marks
            val numbers = (minFrequency.toInt()..maxFrequency.toInt())
            val spacing = (width - 2 * paddingPx) / frequencyRange

            numbers.forEach { number ->
                val x = paddingPx + (number - minFrequency) * spacing

                // Draw long tick mark for whole numbers
                drawLine(
                    color = Color.White,
                    start = Offset(x, height / 2 - 8.dp.toPx()),
                    end = Offset(x, height / 2 + 8.dp.toPx()),
                    strokeWidth = 2.dp.toPx()
                )

                // Draw short tick marks for 0.2, 0.4, 0.6, 0.8 increments
                for (i in 1..4) {
                    val tickFreq = number + (i * 0.2f)
                    if (tickFreq <= maxFrequency) {
                        val tickX = paddingPx + (tickFreq - minFrequency) * spacing
                        drawLine(
                            color = Color.White.copy(alpha = 0.5f),
                            start = Offset(tickX, height / 2 - 4.dp.toPx()),
                            end = Offset(tickX, height / 2 + 4.dp.toPx()),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            }

            // Draw red indicator line
            drawLine(
                color = Color(0xFFFF0000), // Red color
                start = Offset(frequencyPosition, height / 2 - 30.dp.toPx()),
                end = Offset(frequencyPosition, height / 2 + 30.dp.toPx()),
                strokeWidth = 3.dp.toPx()
            )

            // Draw red indicator circle at the top
            drawCircle(
                color = Color(0xFFFF0000),
                radius = 6.dp.toPx(),
                center = Offset(frequencyPosition, height / 2 - 30.dp.toPx())
            )
        }

        // Draw numbers above the line
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val boxWidth = constraints.maxWidth.toFloat()
            val numbers = (minFrequency.toInt()..maxFrequency.toInt())
            val frequencyRange = maxFrequency - minFrequency
            val paddingPx = with(density) { padding.toPx() }
            val spacing = (boxWidth - 2 * paddingPx) / frequencyRange
            val textOffsetPx = with(density) { 7.dp.toPx() }

            numbers.forEach { number ->
                val xPosition = paddingPx + (number - minFrequency) * spacing

                Text(
                    text = number.toString(),
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = (xPosition - textOffsetPx).toInt(),
                                y = 0
                            )
                        }
                )
            }
        }
    }
}