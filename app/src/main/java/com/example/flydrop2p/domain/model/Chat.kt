package com.example.flydrop2p.domain.model

import kotlinx.coroutines.flow.Flow

data class Chat(
    val id: Int,
    val name: String,
    var messages: List<Message>,
)
