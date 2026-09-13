package com.lumina.app

import android.app.Application
import com.lumina.app.data.local.LuminaDatabase
import com.lumina.app.data.repository.LuminaRepository
import com.lumina.app.notifications.LuminaNotifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LuminaApplication : Application() {

    /** Lives as long as the process; used only for one-off startup work. */
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
        LuminaNotifications.createChannels(this)
        applicationScope.launch { ServiceLocator.repository.seedIfEmpty() }
    }
}

/**
 * A deliberately small dependency graph.
 *
 * The app has one database and one repository, so a service locator keeps wiring visible
 * in a single place without pulling in a DI framework.
 */
object ServiceLocator {
    private lateinit var database: LuminaDatabase
    lateinit var repository: LuminaRepository
        private set

    fun init(application: Application) {
        if (::repository.isInitialized) return
        database = LuminaDatabase.get(application)
        repository = LuminaRepository(database)
    }
}
