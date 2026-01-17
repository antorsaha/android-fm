package com.saha.androidfm.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.net.Uri
import coil.compose.AsyncImage
import com.saha.androidfm.R
import com.saha.androidfm.data.enums.MenuAction
import com.saha.androidfm.ui.theme.cardBackgroundColor

/**
 * Displays a sticker item that can show either a drawable resource or an image from a Uri.
 * 
 * Supports both:
 * - Drawable resources: Use stickerResource parameter
 * - Downloaded images from Uri: Use imageUri parameter (priority over stickerResource)
 * 
 * Images from Uri are loaded using Coil and support both file:// and content:// URIs.
 * Downloaded images are stored securely in internal storage (not accessible by file managers).
 * 
 * @param modifier Modifier for the composable
 * @param stickerResource Drawable resource ID (used if imageUri is null)
 * @param imageUri Optional Uri to an image file (takes priority over stickerResource)
 * @param isFavorites Whether this item is marked as favorite
 * @param isHistory Whether this item is from history
 * @param onMenuActionClick Callback when menu action is clicked
 */
@Composable
fun StickerItem(
    modifier: Modifier,
    stickerResource: Int = R.drawable.img_denneryfm,
    imageUri: Uri? = null,
    isFavorites: Boolean = false,
    isHistory: Boolean = false,
    onMenuActionClick: (MenuAction) -> Unit,
) {
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    var isContextMenuVisible by remember { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }

    val density = LocalDensity.current

    Card(
        modifier = modifier
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
            .size(120.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(4.dp),
                ambientColor = Color.Gray,
                spotColor = Color.Gray
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        pressOffset = DpOffset(
                            with(density) { offset.x.toDp() },
                            with(density) { offset.y.toDp() })
                        isContextMenuVisible = true

                    })
            },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        border = BorderStroke(0.2.dp, Color.Gray)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            if (isFavorites) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.like),
                    contentDescription = "favorite",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            // Display image from Uri if provided, otherwise use drawable resource
            if (imageUri != null) {
                // Use Coil to load image from Uri (supports file:// and content:// URIs)
                // Images are stored securely in internal storage (not accessible by file managers)
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Downloaded Sticker",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            } else {
                // Fallback to drawable resource
                Image(
                    painter = painterResource(id = stickerResource),
                    contentDescription = "Sticker",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            DropdownMenu(
                expanded = isContextMenuVisible,
                onDismissRequest = { isContextMenuVisible = false },
                offset = pressOffset.copy(
                    y = pressOffset.y - itemHeight
                ) // now follows tap/long press location
            ) {
                MenuItems(
                    isFavorites = isFavorites, isHistory = isHistory, onMenuActionClick = {
                        isContextMenuVisible = false
                        onMenuActionClick.invoke(it)
                    })
            }
        }
    }
}


@Composable
fun MenuItems(
    isFavorites: Boolean = false,
    isHistory: Boolean = false,
    onMenuActionClick: (MenuAction) -> Unit,
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {

        if (isFavorites) {
            // remove from favorite
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onMenuActionClick.invoke(MenuAction.REMOVE_FROM_FAVORITE)
                }
                .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = stringResource(R.string.remove_from_favorite),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            // Mark as favorite
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onMenuActionClick.invoke(MenuAction.MARK_AS_FAVORITE)
                }
                .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = stringResource(R.string.mark_as_favorite),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Export as WhatsApp Sticker
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onMenuActionClick.invoke(MenuAction.EXPORT_AS_WHATSAPP_STICKER)
            }
            .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(R.string.export_as_whatsapp_sticker),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "WhatsApp",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        // Save to photo library
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onMenuActionClick.invoke(MenuAction.SAVE_AS_PHOTO)
            }
            .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(R.string.save_to_photo_library),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Download",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        // Copy Prompt
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onMenuActionClick.invoke(MenuAction.COPY_PROMPT)
            }
            .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(R.string.copy_prompt),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Copy",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        if (isHistory) {
            // Delete
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onMenuActionClick.invoke(MenuAction.DELETE)
                }
                .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = stringResource(R.string.delete),
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

