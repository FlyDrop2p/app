package com.flydrop2p.flydrop2p.ui.screen.chat

import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatType
import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.domain.model.ChatInfo
import com.flydrop2p.flydrop2p.domain.model.Message
import com.flydrop2p.flydrop2p.network.Device


data class ChatViewState(
    val chatInfo: ChatInfo = ChatInfo(0, ChatType.SINGLE, "", "", 0),
    val messageList: MutableList<Message> = mutableListOf()
)