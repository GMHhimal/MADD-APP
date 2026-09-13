# Lumina XML source-matched layouts

The files under `app/src/main/res/layout/` were rebuilt by reading the actual Jetpack Compose screen code under `feature/.../*.kt` and the preview sample data in `preview/PreviewData.kt`.

Each `activity_*.xml` is the static Android Views/XML equivalent of the corresponding Compose screen. Text, section order, major controls, colors, spacing, cards and buttons follow the Compose source. The main app still launches the Compose version; `XmlMainActivity.kt` can load the XML equivalents with `setContentView()` for traditional Android Views demonstrations.

Dynamic Compose-only behavior (animated breathing scale, live charts, state-driven list changes) is represented as the same static preview state in XML because Android Studio's XML Layout Editor is static.
