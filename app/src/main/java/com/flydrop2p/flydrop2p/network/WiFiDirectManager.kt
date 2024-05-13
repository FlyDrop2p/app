package com.flydrop2p.flydrop2p.network

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.widget.Toast
import com.flydrop2p.flydrop2p.MainActivity

class WiFiDirectManager(private val activity: MainActivity) {
    private val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        activity.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    private var channel: WifiP2pManager.Channel? = null

    init {
        channel = manager?.initialize(activity, activity.mainLooper, null)
        checkDeviceCompatibility()
    }

    fun discoverPeers(listener: WifiP2pManager.ActionListener) {
        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (activity.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        manager?.discoverPeers(channel, listener)
    }

    fun requestPeers(listener: WifiP2pManager.PeerListListener) {
        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (activity.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        manager?.requestPeers(channel, listener)
    }

    fun requestGroupInfo(listener: WifiP2pManager.GroupInfoListener) {
        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (activity.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        manager?.requestGroupInfo(channel, listener)
    }

    fun connectToDevice(device: WifiP2pDevice, listener: WifiP2pManager.ActionListener) {
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

        manager?.connect(channel, config, listener)
    }

    private fun checkDeviceCompatibility() {
        if (!activity.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT)) {
            Toast.makeText(activity, "Wi-Fi Direct is not supported by this device.", Toast.LENGTH_SHORT).show()
        }

        val wifiManager = activity.getSystemService(Activity.WIFI_SERVICE) as WifiManager
        if (!wifiManager.isP2pSupported) {
            Toast.makeText(activity, "Wi-Fi Direct is not supported by the hardware or Wi-Fi is off.", Toast.LENGTH_SHORT).show()
        }

        if (manager == null) {
            Toast.makeText(activity, "Cannot get Wi-Fi Direct system service.", Toast.LENGTH_SHORT).show()

        }

        if (channel == null) {
            Toast.makeText(activity, "Cannot initialize Wi-Fi Direct.", Toast.LENGTH_SHORT).show()
        }
    }
}