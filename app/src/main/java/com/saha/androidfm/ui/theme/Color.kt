package com.saha.androidfm.ui.theme

import androidx.compose.ui.graphics.Color

// MARK: - Legacy Colors (keeping for compatibility)
val Purple80 = Color(0xFFB8A8FF)
val PurpleGrey80 = Color(0xFFC4B8D9)
val Pink80 = Color(0xFFFFB3C6)

val Purple40 = Color(0xFF7C6BC4)
val PurpleGrey40 = Color(0xFF6B5F7F)
val Pink40 = Color(0xFF9D6B7F)

// MARK: - Backgrounds
val backgroundColor = Color(0xFF0D1117) // Deep charcoal with subtle blue undertone
val surface = Color(0xFF161B22) // Elevated surface with subtle blue-gray
val cardBackgroundColor = Color(0xFF1C2128) // Card background with blue-gray tint

// MARK: - Text Colors
val primaryTextColor = Color(0xFFFFFFFF) // Pure white for primary text
val secondaryTextColor = Color(0xFFA8B4C8) // Soft blue-gray for secondary text
val tabBarColor = Color(0xFF4A5568) // Muted blue-gray for tab bar

// MARK: - Accent Colors
val accentRed = Color(0xFFFF6B7A) // Vibrant coral-red
val accent = Color(0xFF00D4FF) // Bright cyan - main accent color (modern, energetic)

// MARK: - Button Colors
val primaryButtonColor = Color(0xFF00D4FF) // Cyan accent for primary buttons
val primaryButtonColor20 = Color(0x3300D4FF) // 20% opacity cyan
val primaryButtonColor50 = Color(0x8000D4FF) // 50% opacity cyan
val secondaryButtonColor = Color(0xFF2A3441) // Dark blue-gray for secondary buttons

// MARK: - Station Icon Colors (vibrant, distinct colors)
val stationIconPurple = Color(0xFF9D4EDD) // Rich purple
val stationIconBlue = Color(0xFF4A9EFF) // Bright blue
val stationIconOrange = Color(0xFFFF8C42) // Vibrant orange
val stationIconGreen = Color(0xFF4ECDC4) // Teal green
val stationIconRed = Color(0xFFFF6B7A) // Coral red

// MARK: - Gradient Colors
val purpleGradientStart = Color(0xFF9D4EDD) // Rich purple
val purpleGradientEnd = Color(0xFF6A2C91) // Deep purple
val purpleGradientAlternative = Color(0xFFB84DFF) // Bright purple
val onboardingPurple = Color(0xFF8B5CF6) // Modern purple

// Cyan/Teal Gradients (modern, fresh)
val cyanGradientStart = Color(0xFF00D4FF) // Bright cyan
val cyanGradientEnd = Color(0xFF0099CC) // Deep cyan
val tealGradientStart = Color(0xFF4ECDC4) // Teal
val tealGradientEnd = Color(0xFF2E9E96) // Deep teal

// Coral/Orange Gradients (warm, energetic)
val coralGradientStart = Color(0xFFFF6B7A) // Coral
val coralGradientEnd = Color(0xFFFF8C42) // Orange
val goldGradientStart = Color(0xFFFFD93D) // Bright gold
val goldGradientEnd = Color(0xFFFFB347) // Warm gold

// MARK: - System/Utility Colors
val white = Color(0xFFFFFFFF)
val grayLight = Color(0xFF1E2430) // Light gray with blue tint
val darkGray = Color(0xFF4A5568) // Medium gray-blue
val systemRed = Color(0xFFFF4757) // Modern red
val systemGreen = Color(0xFF51CF66) // Modern green
val systemBlue = Color(0xFF4A9EFF) // Modern blue

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
