package com.flydrop2p.flydrop2p.ui.screen.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    private val _userName = mutableStateOf("Giovanni Rossi")
    val userName = _userName

    private val _deviceName = mutableStateOf("Iphone di Giovanni")
    val deviceName = _deviceName

    private val _profilePicture = mutableStateOf("URL o risorsa per l'immagine di profilo")
    val profilePicture = _profilePicture
}