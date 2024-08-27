package com.flydrop2p.flydrop2p.network.wifidirect

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
import androidx.annotation.RequiresApi

class WiFiDirectManager(private val context: Context) {
    private val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    private var channel: WifiP2pManager.Channel? = null

    init {
        channel = manager?.initialize(context, context.mainLooper, null)
        checkDeviceCompatibility()
    }

    fun discoverPeers(listener: WifiP2pManager.ActionListener) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        manager?.discoverPeers(channel, listener)
    }

    fun requestConnectionInfo(listener: WifiP2pManager.ConnectionInfoListener) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        manager?.requestConnectionInfo(channel, listener)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestDeviceInfo(listener: WifiP2pManager.DeviceInfoListener) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        channel?.let { manager?.requestDeviceInfo(it, listener) }
    }

    fun requestGroupInfo(listener: WifiP2pManager.GroupInfoListener) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        manager?.requestGroupInfo(channel, listener)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestNetworkInfo(listener: WifiP2pManager.NetworkInfoListener) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        channel?.let { manager?.requestNetworkInfo(it, listener) }
    }

    fun requestPeers(listener: WifiP2pManager.PeerListListener) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        manager?.requestPeers(channel, listener)
    }

    fun connectToDevice(device: WifiP2pDevice, listener: WifiP2pManager.ActionListener) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        val config = WifiP2pConfig()
        config.deviceAddress = device.deviceAddress

        manager?.connect(channel, config, listener)
    }

    private fun checkDeviceCompatibility() {
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT)) {
            Toast.makeText(context, "Wi-Fi Direct is not supported by this device.", Toast.LENGTH_SHORT).show()
        }

        val wifiManager = context.getSystemService(Activity.WIFI_SERVICE) as WifiManager
        if (!wifiManager.isP2pSupported) {
            Toast.makeText(context, "Wi-Fi Direct is not supported by the hardware or Wi-Fi is off.", Toast.LENGTH_SHORT).show()
        }

        if (manager == null) {
            Toast.makeText(context, "Cannot get Wi-Fi Direct system service.", Toast.LENGTH_SHORT).show()

        }

        if (channel == null) {
            Toast.makeText(context, "Cannot initialize Wi-Fi Direct.", Toast.LENGTH_SHORT).show()
        }
    }
}