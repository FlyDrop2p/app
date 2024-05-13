package com.flydrop2p.flydrop2p.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import com.flydrop2p.flydrop2p.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WiFiDirectBroadcastReceiver(
    activity: MainActivity
) : BroadcastReceiver() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val manager = WiFiDirectManager(activity)
    private val devices = mutableSetOf<WifiP2pDevice>()
    private val serverService = ServerService()
    private val clientService = ClientService()

    init {
        serverService.startConnection()
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

        manager.requestGroupInfo(object : WifiP2pManager.GroupInfoListener {
            override fun onGroupInfoAvailable(info: WifiP2pGroup?) {
                if(info?.isGroupOwner != true) {
                    coroutineScope.launch {
                        clientService.connectToServer()
                    }
                }
            }
        })
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
                Log.d("WifiDirectBroadcastReceiver", "WIFI_P2P_STATE_CHANGED_ACTION")
                updateDevices()
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                Log.d("WifiDirectBroadcastReceiver", "WIFI_P2P_PEERS_CHANGED_ACTION")
                updateDevices()
            }

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                Log.d("WifiDirectBroadcastReceiver", "WIFI_P2P_CONNECTION_CHANGED_ACTION")
                updateDevices()
            }

            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                Log.d("WifiDirectBroadcastReceiver", "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION")
                updateDevices()
            }
        }
    }
}
