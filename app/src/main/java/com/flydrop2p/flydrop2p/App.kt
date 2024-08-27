package com.flydrop2p.flydrop2p

import android.app.Application
import com.flydrop2p.flydrop2p.data.AppContainer
import com.flydrop2p.flydrop2p.data.AppDataContainer

class App : Application() {
    lateinit var container: AppContainer

    fun initializeContainer(activity: MainActivity) {
        container = AppDataContainer(activity)
    }
}
