package com.saha.androidfm.views.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.saha.androidfm.R
import com.saha.androidfm.ui.theme.primaryTextColor
import com.saha.androidfm.ui.theme.secondaryTextColor
import com.saha.androidfm.ui.theme.surface
import com.saha.androidfm.utils.ext.singleClick
import com.saha.androidfm.utils.helpers.AppConstants
import com.saha.androidfm.utils.helpers.AppHelper
import com.saha.androidfm.utils.navigation.NavigationWrapper
import com.saha.androidfm.views.components.HeightGap
import com.saha.androidfm.views.components.WidthGap
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SettingScreen(parentNavController: NavController, navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp) // Extra padding for bottom navigation
    ) {

        HeightGap(16.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "More",
                style = MaterialTheme.typography.titleMedium,
                color = secondaryTextColor,
            )
        }
        HeightGap(24.dp)

        // Explore Section
        Text(
            text = "Explore",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = secondaryTextColor,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        SettingItem(
            text = "News",
            icon = Icons.Default.Article
        ) {
            navigateToWebView(parentNavController, "News", AppConstants.NEWS_URL)
        }

        HeightGap(8.dp)

        SettingItem(
            text = "Schedule",
            icon = Icons.Default.CalendarToday
        ) {
            navigateToWebView(parentNavController, "Schedule", AppConstants.SCHEDULE_URL)
        }

        HeightGap(8.dp)

        SettingItem(
            text = "Events",
            icon = Icons.Default.Event
        ) {
            navigateToWebView(parentNavController, "Events", AppConstants.EVENTS_URL)
        }

        HeightGap(8.dp)

        SettingItem(
            text = "Our DJs",
            icon = Icons.Default.People
        ) {
            navigateToWebView(parentNavController, "Our DJs", AppConstants.OUR_DJ_URL)
        }

        HeightGap(32.dp)

        // Support Section
        Text(
            text = "Support",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = secondaryTextColor,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        SettingItem(
            text = "Contact Us",
            icon = Icons.Default.Email
        ) {
            // Open email client
            AppHelper.openEmail(context, AppConstants.CONTACT_ADDRESS)
        }

        HeightGap(8.dp)

        SettingItem(
            text = "About Us",
            icon = Icons.Default.Info
        ) {
            navigateToWebView(parentNavController, "About Us", AppConstants.ABOUT_US_URL)
        }

        HeightGap(8.dp)

        SettingItem(
            text = "Share with Friends",
            icon = Icons.Default.Share
        ) {
            AppHelper.shareApp(context)
        }

        HeightGap(8.dp)

        SettingItem(
            text = "Privacy Policy",
            icon = Icons.Default.PrivacyTip
        ) {
            navigateToWebView(parentNavController, "Privacy Policy", AppConstants.PRIVACY_POLICY_URL)
        }

        HeightGap(8.dp)

        SettingItem(
            text = "Terms of Use",
            icon = Icons.Default.Description
        ) {
            navigateToWebView(parentNavController, "Terms of Use", AppConstants.TERMS_OF_USE_URL)
        }

        HeightGap(32.dp)

        // Follow Us Section
        Text(
            text = "Follow Us",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = secondaryTextColor,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Social Media Icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Facebook
            SocialMediaButton(
                icon = Icons.Default.Facebook,
                onClick = {
                    AppHelper.openFacebook(context, AppConstants.FACEBOOK_URL)
                }
            )

            WidthGap(24.dp)

            // Instagram
            SocialMediaButton(
                icon = Icons.Default.CameraAlt, // Using CameraAlt as Instagram icon
                onClick = {
                    AppHelper.openInstagram(context, AppConstants.INSTAGRAM_URL)
                }
            )

            WidthGap(24.dp)

            // TikTok
            SocialMediaButton(
                icon = Icons.Default.MusicNote,
                onClick = {
                    AppHelper.openTikTok(context, AppConstants.TIKTOK_URL)
                }
            )
        }

        // Version Text
        Text(
            text = "Version ${AppHelper.getVersionName(context)}",
            style = MaterialTheme.typography.bodyMedium,
            color = secondaryTextColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

/**
 * Helper function to navigate to WebViewScreen with encoded title and URL
 */
private fun navigateToWebView(navController: NavController, title: String, url: String) {
    val data = Gson().toJson(WebViewScreenRoute(title, url))

    navController.navigate(
        NavigationWrapper(
            data = data,
            screenName = WebViewScreenRoute::class.java.name
        )
    )
}

@Composable
fun SocialMediaButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(
                color = surface,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = primaryTextColor,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun SettingItem(
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = surface,
                shape = RoundedCornerShape(12.dp)
            )
            .singleClick {
                onClick.invoke()
            }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Image(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(primaryTextColor)
            )
        }

        WidthGap(12.dp)

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = primaryTextColor,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(secondaryTextColor)
        )
    }
}
