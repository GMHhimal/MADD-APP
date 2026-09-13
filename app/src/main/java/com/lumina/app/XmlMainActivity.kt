package com.lumina.app

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.lifecycle.lifecycleScope
import com.lumina.app.data.local.DeadlineEntity
import com.lumina.app.data.local.HabitEntity
import com.lumina.app.data.local.HealthReminderEntity
import com.lumina.app.data.model.Mood
import com.lumina.app.data.model.Priority
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Traditional Android Views/XML entry point used for the university/viva version.
 *
 * The original Jetpack Compose screens are intentionally kept in the project, while this
 * activity demonstrates the classic XML approach taught in basic Android labs:
 * setContentView(...) + findViewById(...) + click listeners.
 *
 * Important: this version still uses the SAME Room database and LuminaRepository.
 */
class XmlMainActivity : ComponentActivity() {

    private val repository get() = ServiceLocator.repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        show(R.layout.activity_welcome)
    }

    private fun show(@LayoutRes layout: Int) {
        setContentView(layout)
        bindBottomNavigation()
        bindScreenActions()
        loadDatabaseSummary(layout)
    }

    private fun bindBottomNavigation() {
        click(R.id.navHome) { show(R.layout.activity_home) }
        click(R.id.navPlan) { show(R.layout.activity_plan) }
        click(R.id.navWellness) { show(R.layout.activity_habits) }
        click(R.id.navProgress) { show(R.layout.activity_progress) }
        click(R.id.navProfile) { show(R.layout.activity_profile) }
    }

    private fun bindScreenActions() {
        click(R.id.btnGetStarted) { show(R.layout.activity_onboarding_lifestyle) }
        click(R.id.btnSignIn) { show(R.layout.activity_home) }
        click(R.id.btnLifestyleContinue) { show(R.layout.activity_onboarding_interests) }
        click(R.id.btnInterestsContinue) { show(R.layout.activity_onboarding_routine) }
        click(R.id.btnRoutineFinish) {
            lifecycleScope.launch {
                repository.completeOnboarding()
                show(R.layout.activity_home)
            }
        }

        click(R.id.btnLogMood) { show(R.layout.activity_mood) }
        click(R.id.btnFindFreeTime) { show(R.layout.activity_freetime) }
        click(R.id.btnStartFocus) { show(R.layout.activity_focus) }
        click(R.id.btnBreathing) { show(R.layout.activity_breathing) }
        click(R.id.btnPlanFocus) { show(R.layout.activity_focus) }
        click(R.id.btnHealth) { show(R.layout.activity_health) }
        click(R.id.btnProfileHealth) { show(R.layout.activity_health) }
        click(R.id.btnFreeFocus) { show(R.layout.activity_focus) }
        click(R.id.btnBack) { show(R.layout.activity_home) }

        click(R.id.btnAddDeadline) { showAddDeadlineDialog() }

        click(R.id.btnNewHabit) { showAddHabitDialog() }

        click(R.id.btnHabitWater) {
            lifecycleScope.launch {
                val water = repository.habits.first().firstOrNull {
                    it.title.contains("water", ignoreCase = true)
                }
                if (water != null) repository.advanceHabit(water.id)
                toast(if (water != null) "Water habit updated in Room" else "Water habit not found")
                show(R.layout.activity_habits)
            }
        }

        click(R.id.btnHabitStudy) {
            lifecycleScope.launch {
                val habit = repository.habits.first().firstOrNull { !it.isDone }
                if (habit != null) repository.advanceHabit(habit.id)
                toast(if (habit != null) "Habit updated in Room" else "All habits are complete")
                show(R.layout.activity_habits)
            }
        }

        click(R.id.btnMoodSave) {
            val selected = findViewById<RadioGroup>(R.id.groupMood)?.checkedRadioButtonId
            val mood = when (selected) {
                R.id.moodGreat -> Mood.GREAT
                R.id.moodOkay -> Mood.OKAY
                R.id.moodLow -> Mood.LOW
                R.id.moodStressed -> Mood.STRESSED
                else -> Mood.GOOD
            }
            lifecycleScope.launch {
                repository.logMood(mood, listOf("Studies / Work"))
                toast("Mood saved to Room database")
                show(R.layout.activity_home)
            }
        }

        click(R.id.btnFreeWalk) { toast("Activity added to today’s plan") }
        click(R.id.btnBreathingStart) { startSimpleCountdown(R.id.txtBreathingCircle, 5 * 60, "BREATHE") }
        click(R.id.btnFocusStart) { startSimpleCountdown(R.id.txtFocusTimer, 25 * 60, null) }
        click(R.id.btnFocusDone) {
            lifecycleScope.launch {
                repository.logFocusSession("XML Focus Session", 25, completed = true)
                toast("Focus session stored in Room")
                show(R.layout.activity_home)
            }
        }

        click(R.id.btnAddReminder) { showAddHealthDialog() }

        click(R.id.btnHealthDone) {
            lifecycleScope.launch {
                val reminder = repository.healthReminders.first().firstOrNull()
                if (reminder != null) repository.markHealthReminderDone(reminder)
                toast(if (reminder != null) "Reminder updated in Room" else "No reminder found")
            }
        }

        click(R.id.btnResetHabits) {
            lifecycleScope.launch {
                repository.resetHabitsForNewDay()
                toast("Today’s habits reset in Room")
            }
        }
        click(R.id.btnRestartSetup) { show(R.layout.activity_onboarding_lifestyle) }
        click(R.id.btnDashboardBack) { show(R.layout.activity_welcome) }

        findViewById<Switch>(R.id.switchProfileSmart)?.setOnCheckedChangeListener { _, checked ->
            lifecycleScope.launch {
                repository.updateProfile { it.copy(smartSuggestions = checked) }
                toast("Preference saved")
            }
        }
    }

    /** Reads real Room data and places a short summary into the current XML layout. */
    private fun loadDatabaseSummary(@LayoutRes layout: Int) {
        lifecycleScope.launch {
            when (layout) {
                R.layout.activity_home -> {
                    val habits = repository.habits.first()
                    val deadlines = repository.deadlines.first()
                    val done = habits.count { it.isDone }
                    text(R.id.txtHomeSummary)?.text =
                        "$done/${habits.size} habits complete • ${deadlines.size} active deadlines"
                }
                R.layout.activity_habits -> {
                    val habits = repository.habits.first()
                    val done = habits.count { it.isDone }
                    text(R.id.txtHabitsSummary)?.text = "$done of ${habits.size} habits complete today"
                }
                R.layout.activity_plan -> {
                    val deadlines = repository.deadlines.first()
                    text(R.id.txtPlanSummary)?.text = "${deadlines.size} deadlines saved in Room database"
                }
                R.layout.activity_progress -> {
                    val habits = repository.habits.first()
                    val focus = repository.focusSessions.first()
                    val minutes = focus.sumOf { it.durationMinutes }
                    text(R.id.txtProgressSummary)?.text =
                        "${habits.count { it.isDone }} habits complete • $minutes focus minutes logged"
                }
                R.layout.activity_profile -> {
                    val profile = repository.getProfile()
                    text(R.id.txtProfileName)?.text = profile?.displayName ?: "Lumina User"
                    findViewById<Switch>(R.id.switchProfileSmart)?.isChecked = profile?.smartSuggestions ?: true
                }
            }
        }
    }

    private fun showAddHabitDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_habit)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.findViewById<View>(R.id.btnHabitCancel).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<View>(R.id.btnHabitSave).setOnClickListener {
            val title = dialog.findViewById<EditText>(R.id.etHabitTitle).text.toString().ifBlank { "New Daily Habit" }
            val detail = dialog.findViewById<EditText>(R.id.etHabitDetail).text.toString()
            lifecycleScope.launch {
                repository.addHabit(
                    HabitEntity(title = title, iconKey = "star", detail = detail, scheduledMinutes = null, sortOrder = 99)
                )
                dialog.dismiss()
                toast("Habit saved to Room database")
                show(R.layout.activity_habits)
            }
        }
        dialog.show()
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun showAddDeadlineDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_deadline)
        val spinner = dialog.findViewById<Spinner>(R.id.spinnerPriority)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("HIGH", "MEDIUM", "LOW"))
        dialog.findViewById<View>(R.id.btnDeadlineCancel).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<View>(R.id.btnDeadlineSave).setOnClickListener {
            val title = dialog.findViewById<EditText>(R.id.etDeadlineTitle).text.toString().ifBlank { "New Deadline" }
            val course = dialog.findViewById<EditText>(R.id.etDeadlineCourse).text.toString().ifBlank { "University" }
            val priority = Priority.valueOf(spinner.selectedItem.toString())
            lifecycleScope.launch {
                repository.addDeadline(
                    DeadlineEntity(
                        title = title, course = course,
                        dueAt = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2),
                        progress = 0, priority = priority
                    )
                )
                dialog.dismiss()
                toast("Deadline saved to Room database")
                show(R.layout.activity_plan)
            }
        }
        dialog.show()
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun showAddHealthDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_health_reminder)
        dialog.findViewById<View>(R.id.btnHealthCancel).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<View>(R.id.btnHealthSave).setOnClickListener {
            val title = dialog.findViewById<EditText>(R.id.etHealthTitle).text.toString().ifBlank { "General health check" }
            val months = dialog.findViewById<EditText>(R.id.etHealthMonths).text.toString().toIntOrNull() ?: 6
            val notify = dialog.findViewById<Switch>(R.id.switchHealthNotify).isChecked
            lifecycleScope.launch {
                repository.addHealthReminder(
                    HealthReminderEntity(
                        title = title, iconKey = "health", lastDoneAt = null,
                        nextDueAt = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30),
                        repeatMonths = months, note = "Added from XML screen", notifyEnabled = notify
                    )
                )
                dialog.dismiss()
                toast("Health reminder saved to Room database")
            }
        }
        dialog.show()
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun click(@IdRes id: Int, action: () -> Unit) {
        findViewById<View>(id)?.setOnClickListener { action() }
    }

    private fun text(@IdRes id: Int): TextView? = findViewById(id)

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startSimpleCountdown(@IdRes textId: Int, totalSeconds: Int, label: String?) {
        val target = findViewById<TextView>(textId) ?: return
        val startedAt = System.currentTimeMillis()
        val runner = object : Runnable {
            override fun run() {
                val elapsed = ((System.currentTimeMillis() - startedAt) / 1000).toInt()
                val remaining = (totalSeconds - elapsed).coerceAtLeast(0)
                val mm = remaining / 60
                val ss = remaining % 60
                target.text = if (label == null) {
                    "%02d:%02d".format(mm, ss)
                } else {
                    "$label\n%02d:%02d".format(mm, ss)
                }
                if (remaining > 0) target.postDelayed(this, 1000)
            }
        }
        target.post(runner)
    }
}
