package com.flydrop2p.flydrop2p

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.flydrop2p.flydrop2p.network.ClientService
import com.flydrop2p.flydrop2p.network.ServerService
import com.flydrop2p.flydrop2p.network.ServicesManager
import com.flydrop2p.flydrop2p.network.WiFiDirectBroadcastReceiver
import com.flydrop2p.flydrop2p.ui.theme.FlyDrop2pTheme
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    private lateinit var receiver: WiFiDirectBroadcastReceiver
    private lateinit var servicesManager: ServicesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()

        receiver = WiFiDirectBroadcastReceiver(this)
        servicesManager = ServicesManager()

        setContent {
            FlyDrop2pTheme {
                FlydropApp()
            }
        }

        servicesManager.serverService.isHandshakeSocketOpen.onEach {
            if (!it) {
                servicesManager.serverService.startConnection()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume")

        registerReceiver(receiver, intentFilter)
        receiver.discoverPeers()
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause")

        unregisterReceiver(receiver)
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy")
    }

    private fun requestPermissions() {
        val permissions = mutableListOf<String>()

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

            }

            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.NEARBY_WIFI_DEVICES)) {

                }

                permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
            }
        }

        if (permissions.isNotEmpty()) {
            val permissionsResultLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

            }

            permissionsResultLauncher.launch(permissions.toTypedArray())
        }
    }
}
