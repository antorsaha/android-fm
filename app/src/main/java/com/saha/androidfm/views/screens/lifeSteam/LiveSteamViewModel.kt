package com.saha.androidfm.views.screens.lifeSteam

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saha.androidfm.data.repo.Repo
import com.saha.androidfm.utils.helpers.SecureImageDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HistoryViewModel"

@HiltViewModel
class LiveSteamViewModel @Inject constructor(
    application: Application,
    private val apiRepo: Repo,
) : AndroidViewModel(application) {

    var selectedTabIndex by mutableStateOf(0)
    var downloadedImageUris by mutableStateOf<List<Uri>>(emptyList())
    var likedImageUris by mutableStateOf<List<Uri>>(emptyList())

    fun loadDownloadedStickers(){
        viewModelScope.launch {
            try {
                // Load all downloaded images from secure storage
                downloadedImageUris = SecureImageDownloader.getAllDownloadedImages(getApplication()).reversed()
                
                // Load liked images from database
                loadLikedImages()
            } catch (e: Exception) {
                Log.e(TAG, "Error loading downloaded stickers", e)
            }
        }
    }
    
    /**
     * Loads liked images from the Room database and assigns their URIs to likedImageUris
     */
    suspend fun loadLikedImages() {

    }
    
    /**
     * Toggles the favorite status of an image in the Room database.
     * After updating, reloads the liked images list.
     * 
     * @param uri The URI of the image to toggle favorite status
     */
    fun toggleFavoriteStatus(uri: Uri) {


    }
    
    /**
     * Marks an image as favorite (sets isLiked to true) in the Room database.
     * After updating, reloads the liked images list.
     * 
     * @param uri The URI of the image to mark as favorite
     */
    fun markAsFavorite(uri: Uri) {

    }
}