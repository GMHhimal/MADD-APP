# Lumina Compose Preview Guide

The project has been made preview-safe without connecting Android Studio Preview to Room.
Runtime behavior is unchanged: the real app still uses ViewModels + Room Database.
Android Studio Preview detects inspection mode and uses static sample data from `PreviewData.kt`.

## Fastest way to see every screen

Open:

`app/src/main/java/com/lumina/app/preview/PreviewGallery.kt`

Then choose **Split** or **Design** in the editor. Android Studio will render the main screens as separate Compose previews.

## Individual screen previews

Each main UI file also has its own `@Preview`, including Welcome, all 3 onboarding screens, Home, Plan, Habits, Mood, Free Time, Breathing, Focus, Health, Progress and Profile.
Dialogs/sheets also have previews: Add Habit, Add Deadline, Add Health Reminder, Time Picker and Onboarding Scaffold.

## If preview is blank

1. Wait for Gradle Sync/indexing to finish.
2. Open **Build > Rebuild Project** if Android Studio says preview is out of date.
3. In the preview pane click **Refresh** / **Build & Refresh**.
4. Keep `room = "2.7.1"` in `gradle/libs.versions.toml` for the Kotlin/KSP setup used by this project.

## Viva explanation

> I used Jetpack Compose Preview with sample preview-only state for design-time rendering, and I tested the complete data/navigation flow on the Android emulator. The real runtime screens still obtain state from ViewModels and Room; Preview mode does not read or change the database.
