# Lumina — Smart Wellness & Lifestyle Assistant

Android app built from the Lumina Figma prototype. Jetpack Compose, Material 3, Room.

Package: `com.lumina.app` · minSdk 26 · targetSdk 35 · Kotlin 2.1.21 · AGP 8.9.0 · Gradle 8.14

---

## Opening the project

1. **Android Studio → Open** → select this `Lumina` folder (the one with `settings.gradle.kts`).
2. Let Gradle sync. It downloads the wrapper, AGP and the libraries — first sync needs internet.
3. Run on a device or emulator with **API 26 or higher**.

If Studio offers to upgrade AGP, you can accept it; nothing here depends on a specific AGP version.

### If sync fails with `Unknown command-line option '--jvm-vendor'`

That is Studio's "Configure Daemon JVM Criteria" task, not your code. It means the Gradle
wrapper is older than your Studio. Either use the wrapper shipped here (Gradle 8.14+), or
set **Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK** to a
concrete JDK (JetBrains Runtime 21 or 17) instead of "Daemon JVM criteria".

There is no `gradle-wrapper.jar` in the zip (binaries do not travel well). Android Studio
regenerates it on first sync. If you prefer the command line first, run `gradle wrapper`
once with a local Gradle install, or open the project in Studio once and then use `./gradlew`.

---

## What is implemented

**All 12 prototype screens**, plus focus mode and the dialogs and sheets:

| Screen | Route | File |
|---|---|---|
| Welcome | `welcome` | `feature/welcome/WelcomeScreen.kt` |
| Onboarding 1 — lifestyle | `onboarding/lifestyle` | `feature/onboarding/OnboardingScreens.kt` |
| Onboarding 2 — interests | `onboarding/interests` | same file |
| Onboarding 3 — routine | `onboarding/routine` | same file |
| Home — Smart Today | `home` | `feature/home/HomeScreen.kt` |
| Plan — deadlines | `plan` | `feature/plan/PlanScreen.kt` |
| Free-time discovery | `freetime` | `feature/freetime/FreeTimeScreen.kt` |
| Mood check-in | `mood` | `feature/mood/MoodScreen.kt` |
| Guided breathing | `breathing` | `feature/breathing/BreathingScreen.kt` |
| Habits, meals & fitness | `habits` | `feature/habits/HabitsScreen.kt` |
| Health reminders | `health` | `feature/health/HealthScreen.kt` |
| Progress & insights | `progress` | `feature/progress/ProgressScreen.kt` |
| Profile & settings | `profile` | `feature/profile/ProfileScreen.kt` |
| Smart focus mode | `focus/{task}` | `feature/focus/FocusModeScreen.kt` |

Empty states are built into the screens themselves and appear when you delete all
habits, deadlines or health reminders.

---

## Architecture

```
ui/theme      design tokens ported 1:1 from the Figma variable collections
ui/icons      every glyph as an ImageVector, same path data as the Figma components
ui/components the reusable component library (buttons, chips, cards, rings, mood faces)
data/local    Room entities, DAOs, converters, database
data/repository  LuminaRepository (single source of truth) + SmartEngine (the rules)
feature/*     one package per screen: Composable + ViewModel
notifications channels, builders, receiver, alarm scheduling
navigation    routes, bottom bar destinations, NavHost
```

State flows one way: Room emits `Flow`s → the repository exposes them → a ViewModel
combines them into a `StateFlow` of UI state → the Composable renders it. Writing
anywhere refreshes every screen watching that data.

Dependencies are wired through a small `ServiceLocator` rather than a DI framework —
there is one database and one repository, so the extra machinery would not earn its place.

### SmartEngine

`data/repository/SmartEngine.kt` holds the logic behind everything the app "notices":
which insight to show, which activities fit a gap, and when to warn about a busy day.
They are pure functions with no Android dependencies, which is why they have unit tests
in `app/src/test/`. Run them with `./gradlew test` — no emulator needed.

Free-slot detection lives in `LuminaRepository.findFreeSlots()`: it walks the day's
events between wake-up and sleep and returns every gap over a threshold.

---

## Data

Room database `lumina.db`, version 1, seven entities. On first launch
`LuminaRepository.seedIfEmpty()` populates the same sample content used in the prototype
(Himal, the SE Assignment due tomorrow, 30 days of mood history) so the app looks alive
immediately. Everything after that is real user data.

Nothing leaves the device.

---

## Notifications

Seven channels, one per alert type, defined by `NotificationType`. Each notification
carries two inline actions and a route so tapping it lands on the right screen.

- Runtime `POST_NOTIFICATIONS` permission is requested on Android 13+.
- **Profile → Preview a notification** fires any of the seven so you can demo them.
- **Profile → Turn on routine reminders** schedules the recurring ones from the user's
  own routine times via `AlarmManager`.

Inexact alarms are used deliberately: a water reminder does not justify asking for the
exact-alarm permission, and Android batches inexact alarms to save battery.

---

## Design fidelity

Colours, type scale, radii, spacing and motion timings are taken directly from the
handoff board in the Figma file. Two deliberate differences:

1. **Typography.** The design uses Inter. The app ships with the platform sans so it
   builds with no extra files. To match exactly, drop the Inter `.ttf` files into
   `res/font` and follow the commented block at the top of `ui/theme/Type.kt`.
2. **Profile tab.** In the prototype the Profile tab opened the Progress screen. Here it
   is a separate screen holding the settings list, which is the natural implementation of
   that section.

### Accessibility

- Every interactive element is at least 44×44dp (`Dimens.minTouchTarget`).
- Tertiary text was darkened to `#6B7889` so small labels clear WCAG AA (≈4.5:1).
- State is never carried by colour alone: selected chips add a tick, completed habits
  change icon and border, mood faces carry text labels, priority badges pair colour with
  a word and an icon.
- All sizes are in `sp`/`dp` and layouts hug their content, so system font scaling works.

---

## Known gaps

Things a next iteration would add:

- Habits reset at midnight only via **Profile → Reset today's habits**; a `WorkManager`
  job would do it automatically.
- Drag-to-reschedule exists in the repository (`moveEvent` returns the clashing event so
  a conflict dialog can be shown) but the timeline gesture is not attached yet.
- Deadline progress is bumped programmatically; there is no manual progress editor.
- No authentication — "Sign In" goes straight to Home, matching the prototype.
