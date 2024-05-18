package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.data.local.MessageEntity

data class Chat(
    val id: Int,
    val name: String,
    var messages: MutableList<MessageEntity>,
)
