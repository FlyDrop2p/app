package com.flydrop2p.flydrop2p.ui.screen.call

import com.flydrop2p.flydrop2p.domain.model.device.Account
import com.flydrop2p.flydrop2p.domain.model.device.Contact
import com.flydrop2p.flydrop2p.domain.model.device.Profile

data class CallViewState(
    val contact: Contact = Contact(Account(0, 0), Profile(0, 0, "", null)),
    val isSpeakerOn: Boolean = false
)

