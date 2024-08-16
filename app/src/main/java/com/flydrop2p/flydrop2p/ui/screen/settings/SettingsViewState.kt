package com.flydrop2p.flydrop2p.ui.screen.settings

import com.flydrop2p.flydrop2p.domain.model.contact.Profile

data class SettingsViewState(
    val profile: Profile = Profile("username", null)
)