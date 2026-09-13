# Lumina Hybrid UI — Viva Guide

## What this project contains

This project intentionally keeps the original polished **Jetpack Compose** application as the normal launcher UI, and also contains a complete **XML Views layout set** under `app/src/main/res/layout` for classic Android Studio Design/Component Tree demonstration.

### Normal app (beautiful runtime UI)
- Launcher: `MainActivity.kt`
- UI: Jetpack Compose
- Navigation: Navigation Compose
- Data: ViewModel -> Repository -> DAO -> Room
- This is the same polished Lumina visual design.

### XML Views demo / viva layer
- Activity: `XmlMainActivity.kt`
- Uses `setContentView(...)`
- Uses `findViewById(...)`
- Uses click listeners
- Uses the same `ServiceLocator`, `LuminaRepository`, and Room database
- XML layouts live in `app/src/main/res/layout`

`XmlMainActivity` is declared in the manifest but is NOT the launcher, so it cannot replace or damage the polished Compose UI.

## How to show the XML layout in Android Studio

1. Expand `app > res > layout`.
2. Open `activity_home.xml`.
3. Click **Design** or **Split** at the top-right of the editor.
4. Android Studio will show the **Palette**, **Component Tree**, and phone layout preview.

Good files to show during viva:
- `activity_welcome.xml`
- `activity_home.xml`
- `activity_plan.xml`
- `activity_habits.xml`
- `activity_mood.xml`
- `activity_breathing.xml`
- `activity_focus.xml`
- `activity_health.xml`
- `activity_progress.xml`
- `activity_profile.xml`
- `dialog_add_habit.xml`
- `dialog_add_deadline.xml`

## Good viva explanation

> Lumina uses a hybrid presentation setup in this project. The main polished application is implemented with Jetpack Compose, while I also implemented equivalent traditional Android XML layouts for the Views approach. The XML demo activity uses setContentView and findViewById, and both presentation approaches share the same Room database and Repository layer.

If asked why Compose is still used:

> Jetpack Compose is Android's modern declarative UI toolkit and is better suited to the richer Lumina UI. I kept XML layouts as a traditional Views implementation/reference because XML layout design, Palette, and Component Tree are also part of the Android development concepts I learned.

## Core data flow

`UI -> ViewModel/Activity -> Repository -> DAO -> Room Database`

Room changes are observed by the Compose application using Flow/StateFlow.

## Important

Do not change the launcher activity to `XmlMainActivity` for the normal demo. Keep `MainActivity` as launcher to retain the polished Lumina design.
