package com.example.flydrop2p.data

import com.example.flydrop2p.domain.model.Chat
import com.example.flydrop2p.domain.model.ChatInfo
import com.example.flydrop2p.domain.model.Message

object DataSource {
    val chatsList = listOf(
        Chat(1, "Alice", "Hello", "10:00 AM"),
        Chat(2, "Bob", "Hi", "10:01 AM"),
        Chat(3, "Charlie", "Hey", "10:02 AM"),
        Chat(4, "David", "Hola", "10:03 AM"),
        Chat(5, "Eve", "Bonjour", "10:04 AM"),
        Chat(6, "Frank", "Ciao", "10:05 AM"),
        Chat(7, "Grace", "Namaste, how are you?", "10:06 AM"),
    )

    val placeholderMessages = mapOf(
        1 to listOf(
            Message(1, "Ciao!", "10:00 AM", 1, 2),
            Message(2, "Come stai?", "10:05 AM", 1, 2),
            Message(3, "Tutto bene, grazie!", "10:10 AM", 2, 1)
        ),
        2 to listOf(
            Message(4, "Hey!", "10:01 AM", 2, 1),
            Message(5, "Bene, tu?", "10:06 AM", 1, 2),
            Message(6, "Anch'io bene, grazie!", "10:11 AM", 2, 1)
        ),
        3 to listOf(
            Message(7, "Hey", "10:02 AM", 3, 1),
            Message(8, "Salve!", "10:07 AM", 1, 3)
        ),
        4 to listOf(
            Message(9, "Hola", "10:03 AM", 4, 1),
            Message(10, "Hola!", "10:08 AM", 1, 4)
        ),
        5 to listOf(
            Message(11, "Bonjour", "10:04 AM", 5, 1),
            Message(12, "Bonjour!", "10:09 AM", 1, 5)
        ),
        6 to listOf(
            Message(13, "Ciao", "10:05 AM", 6, 1),
            Message(14, "Ciao!", "10:10 AM", 1, 6)
        ),
        7 to listOf(
            Message(15, "Namaste", "10:06 AM", 7, 1),
            Message(16, "Namaste, come stai?", "10:11 AM", 1, 7),
            Message(17, "Tutto bene, grazie!", "10:16 AM", 7, 1)
        )
    )

    val placeholderChatInfos = chatsList.map { chat ->
        ChatInfo(
            chat.id,
            chat.name,
            placeholderMessages[chat.id] ?: emptyList()
        )
    }

    fun getChatInfo(chatId: Int): ChatInfo {
        return placeholderChatInfos.first { it.id == chatId }
    }

    fun getChats(): List<Chat> {
        return chatsList
    }

    fun getMessages(chatId: Int): List<Message> {
        return placeholderMessages[chatId] ?: emptyList()
    }

}
