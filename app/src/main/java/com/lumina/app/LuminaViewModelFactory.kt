package com.lumina.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lumina.app.feature.breathing.BreathingViewModel
import com.lumina.app.feature.focus.FocusViewModel
import com.lumina.app.feature.freetime.FreeTimeViewModel
import com.lumina.app.feature.habits.HabitsViewModel
import com.lumina.app.feature.health.HealthViewModel
import com.lumina.app.feature.home.HomeViewModel
import com.lumina.app.feature.mood.MoodViewModel
import com.lumina.app.feature.onboarding.OnboardingViewModel
import com.lumina.app.feature.plan.PlanViewModel
import com.lumina.app.feature.profile.ProfileViewModel
import com.lumina.app.feature.progress.ProgressViewModel

/**
 * One factory for every screen. Each ViewModel takes the same repository, so a `when`
 * over the requested class is clearer here than a generic reflection-based factory.
 */
object LuminaViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = ServiceLocator.repository
        return when {
            modelClass.isAssignableFrom(OnboardingViewModel::class.java) -> OnboardingViewModel(repo)
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repo)
            modelClass.isAssignableFrom(PlanViewModel::class.java) -> PlanViewModel(repo)
            modelClass.isAssignableFrom(FreeTimeViewModel::class.java) -> FreeTimeViewModel(repo)
            modelClass.isAssignableFrom(MoodViewModel::class.java) -> MoodViewModel(repo)
            modelClass.isAssignableFrom(BreathingViewModel::class.java) -> BreathingViewModel()
            modelClass.isAssignableFrom(HabitsViewModel::class.java) -> HabitsViewModel(repo)
            modelClass.isAssignableFrom(HealthViewModel::class.java) -> HealthViewModel(repo)
            modelClass.isAssignableFrom(ProgressViewModel::class.java) -> ProgressViewModel(repo)
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repo)
            modelClass.isAssignableFrom(FocusViewModel::class.java) -> FocusViewModel(repo)
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        } as T
    }
}
