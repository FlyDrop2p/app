package com.flydrop2p.flydrop2p

import android.content.Context
import android.os.Handler

class HandlerFactory(private val context: Context) {
    fun buildHandler(): Handler = Handler(context.mainLooper)
}