package com.flydrop2p.flydrop2p.ui.screen.settings

import com.flydrop2p.flydrop2p.domain.model.device.Profile

data class SettingsViewState(
    val profile: Profile = Profile(0, 0, "username", null),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)