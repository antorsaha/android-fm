package com.saha.androidfm.views.screens.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.saha.androidfm.R
import com.saha.androidfm.ui.theme.backgroundColor
import com.saha.androidfm.ui.theme.secondaryTextColor
import com.saha.androidfm.ui.theme.surface
import com.saha.androidfm.utils.helpers.AppConstants
import com.saha.androidfm.viewmodels.RadioPlayerViewModel
import com.saha.androidfm.views.components.HeightGap
import com.saha.androidfm.views.components.WidthGap

private const val TAG = "HomeScreenContent"

@Composable
fun HomeScreenContent(
    navController: NavController,
    parentNavController: NavController,
    radioPlayerViewModel: RadioPlayerViewModel
) {
    LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeightGap(8.dp)

                Text(
                    text = stringResource(R.string.radio),
                    style = MaterialTheme.typography.titleMedium,
                    color = secondaryTextColor,
                    fontWeight = FontWeight.Normal
                )

                HeightGap(4.dp)
                Text(
                    text = AppConstants.STATION_FREQUENCY,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 48.sp
                )

                HeightGap(8.dp)

                Text(
                    text = stringResource(R.string.mhz),
                    style = MaterialTheme.typography.titleMedium,
                    color = secondaryTextColor,
                    fontWeight = FontWeight.Normal
                )

                HeightGap(32.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = surface,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )

                    WidthGap(4.dp)

                    Text(
                        text = AppConstants.STATION_NAME,
                        style = MaterialTheme.typography.bodyMedium
                    )

                }
            }


        }
    }
}