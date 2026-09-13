package com.lumina.app.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumina.app.data.local.UserProfileEntity
import com.lumina.app.data.repository.LuminaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: LuminaRepository) : ViewModel() {

    val profile: StateFlow<UserProfileEntity?> = repository.profile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun setSmartSuggestions(enabled: Boolean) {
        viewModelScope.launch { repository.updateProfile { it.copy(smartSuggestions = enabled) } }
    }

    /** Sends the user back through setup without destroying their data. */
    fun restartSetup() {
        viewModelScope.launch { repository.updateProfile { it.copy(onboardingComplete = false) } }
    }

    fun resetHabitsForNewDay() {
        viewModelScope.launch { repository.resetHabitsForNewDay() }
    }
}
