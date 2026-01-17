package com.saha.androidfm.ui.theme

import androidx.compose.ui.graphics.Color

// MARK: - Legacy Colors (keeping for compatibility)
val Purple80 = Color(0xFFA78BFA)
val PurpleGrey80 = Color(0xFFC4B5FD)
val Pink80 = Color(0xFFF472B6)

val Purple40 = Color(0xFF8B5CF6)
val PurpleGrey40 = Color(0xFF9F7AEA)
val Pink40 = Color(0xFFEC4899)

// MARK: - Backgrounds
val backgroundColor = Color(0xFF111923) // Deep charcoal with subtle blue undertone (FIXED)
val surface = Color(0xFF1C1B23) // Elevated surface with subtle purple-gray tint
val cardBackgroundColor = Color(0xFF25232D) // Card background with purple-gray tint

// MARK: - Text Colors
val primaryTextColor = Color(0xFFF0F6FC) // Soft white for primary text (easier on eyes)
val secondaryTextColor = Color(0xFF8B949E) // Muted gray-blue for secondary text
val tabBarColor = Color(0xFF30363D) // Dark gray-blue for tab bar

// MARK: - Accent Colors
val accentRed = Color(0xFFFF6B9D) // Vibrant pink-red
val accent = Color(0xFF627BFF) // Vibrant purple - main accent color (modern, energetic)

// MARK: - Button Colors
val primaryButtonColor = Color(0xFF627BFF) // Vibrant purple accent for primary buttons
val primaryButtonColor20 = Color(0xFF9DACFF) // 20% opacity purple
val primaryButtonColor50 = Color(0x808B5CF6) // 50% opacity purple
val secondaryButtonColor = Color(0xFF21262D) // Dark gray-blue for secondary buttons

// MARK: - Station Icon Colors (vibrant, distinct colors)
val stationIconPurple = Color(0xFFBC8CFF) // Soft purple
val stationIconBlue = Color(0xFF58A6FF) // Bright blue
val stationIconOrange = Color(0xFFFFA657) // Warm orange
val stationIconGreen = Color(0xFF3FB950) // Fresh green
val stationIconRed = Color(0xFFFF6B9D) // Pink-red

// MARK: - Gradient Colors
val purpleGradientStart = Color(0xFFBC8CFF) // Soft purple
val purpleGradientEnd = Color(0xFF9D4EDD) // Rich purple
val purpleGradientAlternative = Color(0xFFD9468A) // Pink-purple
val onboardingPurple = Color(0xFFA855F7) // Modern vibrant purple

// Blue Gradients (modern, professional)
val blueGradientStart = Color(0xFF58A6FF) // Bright blue
val blueGradientEnd = Color(0xFF1F6FEB) // Deep blue
val cyanGradientStart = Color(0xFF56D4DD) // Cyan
val cyanGradientEnd = Color(0xFF39C5CF) // Deep cyan

// Warm Gradients (energetic, vibrant)
val pinkGradientStart = Color(0xFFFF6B9D) // Pink
val pinkGradientEnd = Color(0xFFFFA657) // Orange
val goldGradientStart = Color(0xFFFFD700) // Bright gold
val goldGradientEnd = Color(0xFFFFB84D) // Warm gold

// MARK: - System/Utility Colors
val white = Color(0xFFFFFFFF)
val grayLight = Color(0xFF21262D) // Light gray with blue tint
val darkGray = Color(0xFF30363D) // Medium gray-blue
val systemRed = Color(0xFFF85149) // Modern red
val systemGreen = Color(0xFF3FB950) // Modern green
val systemBlue = Color(0xFF58A6FF) // Modern blue

// MARK: - Opacity Variants (white with opacity on dark background)
val whiteOpacity15 = Color(0x26FFFFFF) // White at 15% opacity
val whiteOpacity30 = Color(0x4DFFFFFF) // White at 30% opacity
val whiteOpacity60 = Color(0x99FFFFFF) // White at 60% opacity

// MARK: - Color Arrays
val stationIconColors = listOf(
    stationIconPurple,
    stationIconBlue,
    stationIconOrange,
    stationIconGreen,
    stationIconRed
)
