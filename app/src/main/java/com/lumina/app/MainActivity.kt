package com.lumina.app

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.lumina.app.navigation.LuminaApp
import com.lumina.app.notifications.LuminaNotifications
import com.lumina.app.ui.theme.LuminaTheme

class MainActivity : ComponentActivity() {

    /** Route carried by a tapped notification. Read by the composable tree. */
    private var pendingRoute by mutableStateOf<String?>(null)

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* result ignored */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        pendingRoute = intent?.getStringExtra(LuminaNotifications.EXTRA_ROUTE)

        setContent {
            LuminaTheme {
                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        !LuminaNotifications.hasPermission(this@MainActivity)
                    ) {
                        requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
                LuminaApp(
                    startRouteFromNotification = pendingRoute,
                    onNotificationRouteHandled = { pendingRoute = null }
                )
            }
        }
    }

    /** Tapping a notification while the app is already open lands on the right screen. */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingRoute = intent.getStringExtra(LuminaNotifications.EXTRA_ROUTE)
    }
}
