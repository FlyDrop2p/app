package com.flydrop2p.flydrop2p.ui.screen.home

import com.flydrop2p.flydrop2p.domain.model.Contact

data class HomeViewState(
    val contacts: List<Contact> = listOf()
)
