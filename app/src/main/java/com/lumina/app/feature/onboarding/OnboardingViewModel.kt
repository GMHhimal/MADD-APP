package com.lumina.app.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumina.app.data.local.UserProfileEntity
import com.lumina.app.data.repository.LuminaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Onboarding writes straight to the profile row as the user taps.
 *
 * There is no separate draft object: answers survive a back press, an app kill or a
 * rotation for free, and the "smart" defaults are available to the rest of the app the
 * moment they are chosen.
 */
class OnboardingViewModel(private val repository: LuminaRepository) : ViewModel() {

    val profile: StateFlow<UserProfileEntity?> = repository.profile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun setRole(role: String) = update { it.copy(role = role) }

    fun toggleGoal(goal: String) = update { profile ->
        profile.copy(goals = profile.goals.toggle(goal))
    }

    fun toggleInterest(interest: String) = update { profile ->
        profile.copy(interests = profile.interests.toggle(interest))
    }

    fun setPreferredFreeTime(value: String) = update { it.copy(preferredFreeTime = value) }

    fun setSmartSuggestions(enabled: Boolean) = update { it.copy(smartSuggestions = enabled) }

    fun setWakeUp(minutes: Int) = update { it.copy(wakeUpMinutes = minutes) }
    fun setSleep(minutes: Int) = update { it.copy(sleepMinutes = minutes) }
    fun setBreakfast(minutes: Int) = update { it.copy(breakfastMinutes = minutes) }
    fun setLunch(minutes: Int) = update { it.copy(lunchMinutes = minutes) }
    fun setDinner(minutes: Int) = update { it.copy(dinnerMinutes = minutes) }
    fun setExercise(minutes: Int) = update { it.copy(exerciseMinutes = minutes) }

    fun toggleExerciseDay(day: String) = update { profile ->
        profile.copy(exerciseDays = profile.exerciseDays.toggle(day))
    }

    fun finish(onDone: () -> Unit) {
        viewModelScope.launch {
            repository.completeOnboarding()
            onDone()
        }
    }

    private fun update(transform: (UserProfileEntity) -> UserProfileEntity) {
        viewModelScope.launch { repository.updateProfile(transform) }
    }

    private fun List<String>.toggle(value: String): List<String> =
        if (contains(value)) filterNot { it == value } else this + value
}
