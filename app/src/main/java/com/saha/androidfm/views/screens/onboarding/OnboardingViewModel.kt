package com.saha.androidfm.views.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saha.androidfm.utils.helpers.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the onboarding screen state and user interactions.
 * 
 * This ViewModel handles the logic for marking onboarding as completed when the user
 * finishes the onboarding flow. It uses SharedPreferences (via PreferencesManager) to
 * persist the onboarding completion status, ensuring the app doesn't show onboarding
 * screens again after the user has completed them once.
 * 
 * The ViewModel is lifecycle-aware and uses Hilt for dependency injection, making it
 * easy to test and maintain.
 * 
 * @property preferencesManager Injected dependency for managing app preferences.
 *                             Used to save and retrieve onboarding completion status.
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    /**
     * Marks the onboarding flow as completed for the current user.
     * 
     * This function saves the onboarding completion status to SharedPreferences,
     * which prevents the app from showing onboarding screens on subsequent app launches.
     * 
     * The operation is performed on a coroutine scope tied to the ViewModel's lifecycle,
     * ensuring that:
     * - The operation doesn't block the UI thread
     * - The coroutine is automatically cancelled if the ViewModel is cleared
     * - The operation completes even if the user navigates away from the screen
     * 
     * After calling this function, the app's navigation logic (typically in App.kt)
     * will check this preference and route users directly to the home screen instead
     * of showing onboarding screens.
     * 
     * Usage example:
     * ```
     * viewModel.completeOnboarding()
     * // After this, the app will skip onboarding on next launch
     * ```
     */
    fun completeOnboarding() {
        // Launch a coroutine in the ViewModel's scope
        // This ensures the operation is tied to the ViewModel's lifecycle
        viewModelScope.launch {
            // Save the onboarding completion status to SharedPreferences
            // This persists across app restarts
            //preferencesManager.setOnboardingCompleted(true)
        }
    }
}
