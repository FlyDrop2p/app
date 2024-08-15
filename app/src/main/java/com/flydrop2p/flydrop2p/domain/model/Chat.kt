package com.flydrop2p.flydrop2p.domain.model

import kotlinx.coroutines.flow.MutableStateFlow

data class Chat(
    val contact: MutableStateFlow<Contact>,
    var messages: MutableStateFlow<List<TextMessage>>,
)
