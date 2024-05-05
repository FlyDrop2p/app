package com.flydrop2p.flydrop2p.network

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.setContent
import com.flydrop2p.flydrop2p.MainActivity
import java.net.InetAddress

class WiFiDirectBroadcastReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val activity: MainActivity
) : BroadcastReceiver() {
    var devices = mutableSetOf<WifiP2pDevice>()

    init {
        updateDevices()
    }

    fun discoverPeers() {
        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (activity.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                // Toast.makeText(activity.applicationContext, "discoverPeers() onSuccess()", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(reasonCode: Int) {
                // Toast.makeText(activity.applicationContext, "discoverPeers() onFailure()", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateDevices() {
        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (activity.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        manager.requestPeers(channel) {
            for (device in it.deviceList) {
                devices.add(device)
            }
        }
    }

    fun connectToDevice(device: WifiP2pDevice) {
        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (activity.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        val config = WifiP2pConfig()
        config.deviceAddress = device.deviceAddress

        manager.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Toast.makeText(activity.applicationContext, "Connected to ${device.deviceName}", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(reason: Int) {
                Toast.makeText(activity.applicationContext, "Failed to connect to ${device.deviceName}", Toast.LENGTH_SHORT).show()
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
