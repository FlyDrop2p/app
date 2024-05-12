package com.flydrop2p.flydrop2p.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import com.flydrop2p.flydrop2p.MainActivity

class WiFiDirectBroadcastReceiver(
    activity: MainActivity
) : BroadcastReceiver() {
    private val manager = WiFiDirectManager(activity)
    private val devices = mutableSetOf<WifiP2pDevice>()
    val serverService = ServerService()
    val clientService = ClientService()

    init {
        updateDevices()
    }

    fun discoverPeers() {
        manager.discoverPeers(object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d("WifiDirectBroadcastReceiver", "discoverPeers() onSuccess()")
            }

            override fun onFailure(reasonCode: Int) {
                Log.d("WifiDirectBroadcastReceiver", "discoverPeers() onFailure()")
            }
        })
    }

    private fun updateDevices() {
        manager.requestPeers {
            for (device in it.deviceList) {
                devices.add(device)
            }
        }
    }

    fun connectToDevice(device: WifiP2pDevice) {
        manager.connectToDevice(device, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d("WifiDirectBroadcastReceiver", "Connected to ${device.deviceName}")

            }

            override fun onFailure(reason: Int) {
                Log.d("WifiDirectBroadcastReceiver", "Failed to connected to ${device.deviceName}")
            }
        })
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        when (action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                updateDevices()
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                updateDevices()
            }

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                updateDevices()
            }

            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                updateDevices()
            }
        }
    }
}
