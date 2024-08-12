package com.flydrop2p.flydrop2p

import android.app.Application
import android.content.Context
import com.flydrop2p.flydrop2p.data.AppContainer
import com.flydrop2p.flydrop2p.data.AppDataContainer

class FlyDropApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
    }


    fun initializeContainer(context: Context, activity: MainActivity) {
        container = AppDataContainer(context, activity)
    }
}
