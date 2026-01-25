package com.saha.androidfm.views.screens.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.saha.androidfm.R
import com.saha.androidfm.ui.theme.accent
import com.saha.androidfm.ui.theme.backgroundColor
import com.saha.androidfm.ui.theme.secondaryTextColor
import kotlinx.coroutines.launch

/**
 * Route object for the Onboarding screen.
 * 
 * This object is used for navigation routing to identify the onboarding screen
 * in the app's navigation graph. It serves as a unique identifier for this screen.
 */
object OnboardingScreenRoute

/**
 * Data class representing a single page in the onboarding flow.
 * 
 * Each onboarding page can display either a video or an image, along with
 * a title and description. The page supports custom background colors for
 * visual variety.
 * 
 * @property title The main heading text displayed on the page
 * @property description The descriptive text explaining the feature or benefit
 * @property videoResId Optional resource ID for a video file (raw resource).
 *                      If provided, the video will be displayed instead of an image.
 *                      The video plays automatically, loops, and is muted.
 * @property imageResId Optional resource ID for an image drawable.
 *                      Used when videoResId is null. The image is displayed
 *                      using AsyncImage for efficient loading.
 * @property backgroundColor The background color for the page. Defaults to the
 *                           app's theme background color.
 * 
 * Note: Either videoResId or imageResId should be provided, but not both.
 *       If both are null, the page will display without media content.
 */
data class OnboardingPage(
    val title: String,
    val description: String,
    @param:RawRes val videoResId: Int? = null,
    @param:DrawableRes val imageResId: Int? = null,
    val backgroundColor: Color = com.saha.androidfm.ui.theme.backgroundColor
)

/**
 * Main onboarding screen composable that displays a swipeable pager with multiple pages.
 * 
 * This screen introduces users to the app's features through a series of pages that can
 * be swiped horizontally. Each page contains either a video or image, along with descriptive
 * text. Users can navigate through pages using swipe gestures or the Continue button.
 * 
 * The screen includes:
 * - A horizontal pager for swiping between pages
 * - Page indicators showing the current position
 * - A Continue/Get Started button that advances pages or completes onboarding
 * - Integration with OnboardingViewModel to persist completion status
 * 
 * @param onFinish Callback function invoked when the user completes the onboarding flow.
 *                 This is typically used to navigate to the main app screen.
 * 
 * The onboarding completion status is saved to SharedPreferences via the ViewModel,
 * ensuring the onboarding screens won't be shown again on subsequent app launches.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    // Get the ViewModel instance using Hilt dependency injection
    val viewModel: OnboardingViewModel = hiltViewModel()

    // Define the list of onboarding pages with their content
    // Each page represents a feature or benefit of the app
    val pages = listOf(
        OnboardingPage(
            title = "Don't Touch That Dial",
            description = "Stream live radio 24/7\nNever miss your favorite shows",
            imageResId = R.drawable.onboarding_image_1
        ),
        OnboardingPage(
            // Alternative title (commented out): "Welcome to FM Radio"
            title = "Better & louder",
            description = "Crystal clear sound quality\nEnjoy every beat and melody",
            videoResId = R.raw.onboarding_video_2 // This page uses a video instead of an image
        ),
        OnboardingPage(
            title = "Blazing The Airwaves",
            description = "Stream anywhere, anytime\nYour music companion on the go",
            imageResId = R.drawable.onboarding_image_3
        ),
        OnboardingPage(
            title = "The Best music Lives Here",
            description = "Your favorite radio station\nEnjoy live music every day",
            imageResId = R.drawable.onboarding_image_4
        )
    )

    // Create and remember the pager state to track the current page
    // The pageCount lambda ensures the pager updates if the pages list changes
    val pagerState = rememberPagerState(pageCount = { pages.size })
    
    // Coroutine scope for launching animations and async operations
    val scope = rememberCoroutineScope()

    // Main container for the entire onboarding screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Horizontal pager allows users to swipe between onboarding pages
        // Each page is rendered using the OnboardingPageContent composable
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // Render the content for each page
            OnboardingPageContent(
                page = pages[page]
            )
        }

        // Bottom Section with Indicator and Button
        // This overlay is positioned at the bottom and contains navigation controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Page Indicator - Shows dots representing each page
            // The current page is highlighted with a longer, colored indicator
            PageIndicator(
                currentPage = pagerState.currentPage,
                pageCount = pages.size
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Continue Button - Advances to next page or completes onboarding
            ContinueButton(
                onClick = {
                    // Launch coroutine for smooth page transitions
                    scope.launch {
                        if (pagerState.currentPage < pages.size - 1) {
                            // Not on last page: animate to next page
                            pagerState.animateScrollToPage(
                                page = pagerState.currentPage + 1,
                                animationSpec = tween(durationMillis = 500) // 500ms animation
                            )
                        } else {
                            // On last page: complete onboarding and navigate away
                            // Save completion status to prevent showing onboarding again
                            viewModel.completeOnboarding()
                            // Trigger navigation callback to move to main app
                            onFinish()
                        }
                    }
                },
                // Button text changes to "Get Started" on the last page
                isLastPage = pagerState.currentPage == pages.size - 1
            )
        }
    }
}

/**
 * Composable function that renders the content for a single onboarding page.
 * 
 * This function displays the media content (video or image) and text content
 * for an onboarding page. The layout is split into two sections:
 * - Top 60%: Media content (video or image) with a gradient overlay
 * - Bottom 40%: Title and description text
 * 
 * The gradient overlay ensures text readability by creating a smooth transition
 * from the media content to the text section.
 * 
 * @param page The OnboardingPage data object containing all content for this page
 */
@Composable
fun OnboardingPageContent(
    page: OnboardingPage
) {
    // Main container with page-specific background color
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(page.backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Video or Image Section (Top 60% of screen)
            // This section displays the primary visual content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f) // Takes 60% of available vertical space
            ) {
                // Conditionally render video or image based on what's provided
                when {
                    // If video resource is provided, use VideoPlayer
                    page.videoResId != null -> {
                        VideoPlayer(videoResId = page.videoResId)
                    }

                    // If image resource is provided, use AsyncImage for efficient loading
                    page.imageResId != null -> {
                        AsyncImage(
                            model = page.imageResId,
                            contentDescription = page.title, // Accessibility description
                            modifier = Modifier.fillMaxSize(),
                            // Note: ContentScale.Crop is commented out - images use default scaling
                            // Uncomment and adjust if you need different image scaling behavior
                        )
                    }
                }

                // Gradient Overlay for better text readability
                // Creates a smooth visual transition from media to text content
                // The gradient goes from transparent at the top to semi-opaque background at bottom
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent, // Fully transparent at top
                                    backgroundColor.copy(alpha = 0.8f) // 80% opaque at bottom
                                ),
                                startY = 0f // Gradient starts at the top
                            )
                        )
                )
            }

            // Text Content Section (Bottom 40%)
            // Contains the page title and description
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f) // Takes 40% of available vertical space
                    .padding(horizontal = 32.dp), // Horizontal padding for text margins
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Page Title - Large, bold text
                Text(
                    text = page.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Page Description - Smaller, regular weight text
                // Uses secondary text color for visual hierarchy
                Text(
                    text = page.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = secondaryTextColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp // Increased line height for better readability
                )
            }
        }
    }
}

/**
 * Composable function that displays a video player using ExoPlayer.
 * 
 * This function creates an ExoPlayer instance to play a video from raw resources.
 * The video is configured to:
 * - Play automatically when ready
 * - Play once without repeating
 * - Be muted (no audio)
 * - Hide player controls
 * - Hide buffering indicators
 * 
 * The ExoPlayer instance is properly managed with lifecycle-aware disposal to
 * prevent memory leaks. When the composable is removed from composition, the
 * player is released.
 * 
 * @param videoResId The raw resource ID of the video file to play
 * 
 * Note: This function uses @UnstableApi annotation because ExoPlayer's API
 *       is still marked as unstable. This is safe for production use but may
 *       require updates if ExoPlayer API changes.
 */
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(@RawRes videoResId: Int) {
    val context = LocalContext.current

    // Create and remember ExoPlayer instance
    // The remember block ensures the player is only created once per videoResId
    // If videoResId changes, a new player will be created
    val exoPlayer = remember(videoResId) {
        ExoPlayer.Builder(context).build().apply {
            // Construct URI for the video resource
            // Format: "android.resource://[package_name]/[resource_id]"
            val videoUri = "android.resource://${context.packageName}/$videoResId".toUri()
            
            // Set the media item and prepare for playback
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            
            // Configure playback behavior
            playWhenReady = true // Start playing automatically when ready
            repeatMode = ExoPlayer.REPEAT_MODE_OFF // Play once, don't repeat
            volume = 0f // Mute the video (onboarding videos are typically silent)
        }
    }

    // Lifecycle management: Release ExoPlayer when composable is disposed
    // This prevents memory leaks and ensures proper resource cleanup
    DisposableEffect(videoResId) {
        onDispose {
            // Release player resources when the composable is removed
            exoPlayer.release()
        }
    }

    // Embed the ExoPlayer in Compose using AndroidView
    // This allows us to use the native Android PlayerView within Jetpack Compose
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = false // Hide playback controls (play/pause buttons, etc.)
                setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER) // Hide buffering indicator
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

/**
 * Composable function that displays page indicators (dots) for the onboarding pager.
 * 
 * This function creates a row of circular indicators, one for each page in the
 * onboarding flow. The current page is highlighted with:
 * - A wider indicator (32dp vs 8dp)
 * - The accent color instead of semi-transparent white
 * 
 * The width animation provides smooth visual feedback when the user swipes
 * between pages, making the current page indicator expand while others shrink.
 * 
 * @param currentPage The index of the currently displayed page (0-based)
 * @param pageCount The total number of pages in the onboarding flow
 * @param modifier Optional Modifier to apply to the Row container
 * 
 * Example usage:
 * ```
 * PageIndicator(
 *     currentPage = 2,
 *     pageCount = 4
 * )
 * ```
 */
@Composable
fun PageIndicator(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Create one indicator dot for each page
        repeat(pageCount) { index ->
            // Determine if this indicator represents the current page
            val isSelected = currentPage == index
            
            // Animate the width of the indicator
            // Selected indicators are wider (32dp) to draw attention
            // Unselected indicators are smaller (8dp) for a subtle appearance
            val width by animateDpAsState(
                targetValue = if (isSelected) 32.dp else 8.dp,
                animationSpec = tween(durationMillis = 300), // 300ms animation duration
                label = "indicatorWidth" // Label for debugging and performance tracking
            )

            // Individual indicator dot
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp) // Spacing between indicators
                    .height(8.dp) // Fixed height for all indicators
                    .width(width) // Animated width based on selection state
                    .clip(CircleShape) // Make it circular
                    .background(
                        // Color changes based on selection state
                        if (isSelected) accent else Color.White.copy(alpha = 0.3f)
                    )
            )
        }
    }
}

/**
 * Composable function that displays the Continue/Get Started button for onboarding.
 * 
 * This button appears at the bottom of each onboarding page and allows users to:
 * - Navigate to the next page (when not on the last page)
 * - Complete onboarding and proceed to the app (when on the last page)
 * 
 * The button's appearance changes based on the page position:
 * - Text: "Continue" for intermediate pages, "Get Started" for the last page
 * - Icon: Arrow icon shown only on intermediate pages (not on last page)
 * 
 * The button uses the app's accent color and has rounded corners for a modern look.
 * 
 * @param onClick Callback function invoked when the button is clicked.
 *                This should handle page navigation or onboarding completion.
 * @param isLastPage Boolean indicating if the current page is the last one.
 *                   When true, button text changes to "Get Started" and arrow icon is hidden.
 * @param modifier Optional Modifier to apply to the Button
 * 
 * Usage example:
 * ```
 * ContinueButton(
 *     onClick = { /* Handle button click */ },
 *     isLastPage = currentPage == totalPages - 1
 * )
 * ```
 */
@Composable
fun ContinueButton(
    onClick: () -> Unit,
    isLastPage: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp) // Horizontal margins
            .height(56.dp), // Fixed height for consistent button size
        colors = ButtonDefaults.buttonColors(
            containerColor = accent // Use app's accent color
        ),
        shape = RoundedCornerShape(28.dp) // Fully rounded corners (pill shape)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Button text changes based on page position
            Text(
                text = if (isLastPage) "Get Started" else "Continue",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            // Show arrow icon only on intermediate pages (not on last page)
            // The arrow provides visual cue that there's more content to see
            if (!isLastPage) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null, // Decorative icon, no description needed
                    tint = Color.White
                )
            }
        }
    }
}
