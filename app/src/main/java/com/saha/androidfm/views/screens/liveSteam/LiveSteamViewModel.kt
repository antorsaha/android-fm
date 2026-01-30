package com.saha.androidfm.views.screens.liveSteam

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the Live Stream screen.
 * 
 * Currently, this ViewModel is minimal as the Live Stream screen manages
 * its own ExoPlayer instance directly. This ViewModel can be extended in the
 * future if state management or business logic is needed.
 */
@HiltViewModel
class LiveSteamViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    /**
     * Currently selected tab index (for future use if tabs are added).
     * This is kept for potential future features but is not currently used.
     */
    var selectedTabIndex by mutableStateOf(0)
}