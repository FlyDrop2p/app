package com.flydrop2p.flydrop2p

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.flydrop2p.flydrop2p.network.NetworkManager
import com.flydrop2p.flydrop2p.ui.theme.FlyDrop2pTheme

class MainActivity : ComponentActivity() {
    private val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    private lateinit var networkManager: NetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()

        (application as App).initializeContainer(this, this)
        networkManager = (application as App).container.networkManager

        networkManager.startConnections()

        startKeepaliveHandler()

        setContent {
            FlyDrop2pTheme {
                FlyDropApp(
                    onConnectionButtonClick = {

                    }
                )
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

        registerReceiver(networkManager.receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause")

        unregisterReceiver(networkManager.receiver)
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy")
    }

    private fun startKeepaliveHandler() {
        val handler = Handler(mainLooper)
        val runnable = object : Runnable {
            override fun run() {
                networkManager.sendKeepalive()
                handler.postDelayed(this, 5000)
            }
        }

        handler.post(runnable)
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
