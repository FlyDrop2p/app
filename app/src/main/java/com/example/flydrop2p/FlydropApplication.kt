package com.example.flydrop2p

import android.app.Application
import com.example.flydrop2p.data.AppContainer
import com.example.flydrop2p.data.AppDataContainer

class FlydropApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
