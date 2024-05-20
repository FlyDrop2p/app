package com.flydrop2p.flydrop2p.data

import com.flydrop2p.flydrop2p.data.local.MessageEntity
import com.flydrop2p.flydrop2p.domain.model.ChatInfo
import com.flydrop2p.flydrop2p.domain.model.Contact

object DataSource {
    val contacts = listOf(
        Contact(0, "Me"),
        Contact(1, "Alice"),
        Contact(2, "Bob"),
        Contact(3, "Charlie"),
        Contact(4, "David"),
        Contact(5, "Eve"),
        Contact(6, "Frank"),
        Contact(7, "Grace")
    )

    val chatsInfoListDatasource = listOf(
        // ChatInfo(0, "Chat Pubblica", "Benvenuti nella chat pubblica!", "10:00 AM"), // Chat di gruppo
        ChatInfo(1, "Alice"),
        ChatInfo(2, "Bob"),
        ChatInfo(3, "Charlie"),
        ChatInfo(4, "David"),
        ChatInfo(5, "Eve"),
        ChatInfo(6, "Frank"),
        ChatInfo(7, "Grace")
    )

    val placeholderMessages = mapOf(
        // Messages for the Group Chat
        -1 to listOf(
            MessageEntity(chatId = -1, message = "Welcome to the group chat!", timestamp = "10:00 AM", senderId = 0),
            MessageEntity(chatId = -1, message = "Hello everyone!", timestamp = "10:05 AM", senderId = 1)
        ),
        // Messages for the chat with Alice
        1 to listOf(
            MessageEntity(chatId = 1, message = "Hi Alice!", timestamp = "10:00 AM", senderId = 0),
            MessageEntity(chatId = 1, message = "How are you?", timestamp = "10:05 AM", senderId = 1),
            MessageEntity(chatId = 1, message = "I'm fine, thank you!", timestamp = "10:10 AM", senderId = 0)
        ),
        // Messages for the chat with Bob
        2 to listOf(
            MessageEntity(chatId = 2, message = "Hey Bob!", timestamp = "10:01 AM", senderId = 0),
            MessageEntity(chatId = 2, message = "Hi!", timestamp = "10:06 AM", senderId = 2),
            MessageEntity(chatId = 2, message = "I'm also fine, thank you!", timestamp = "10:11 AM", senderId = 0)
        ),
        // Messages for the chat with Charlie
        3 to listOf(
            MessageEntity(chatId = 3, message = "Hi Charlie!", timestamp = "10:02 AM", senderId = 0),
            MessageEntity(chatId = 3, message = "Hello!", timestamp = "10:07 AM", senderId = 3)
        ),
        // Messages for the chat with David
        4 to listOf(
            MessageEntity(chatId = 4, message = "Hello David!", timestamp = "10:03 AM", senderId = 0),
            MessageEntity(chatId = 4, message = "Hola!", timestamp = "10:08 AM", senderId = 4)
        ),
        // Messages for the chat with Eve
        5 to listOf(
            MessageEntity(chatId = 5, message = "Hi Eve!", timestamp = "10:04 AM", senderId = 0),
            MessageEntity(chatId = 5, message = "Bonjour!", timestamp = "10:09 AM", senderId = 5)
        ),
        // Messages for the chat with Frank
        6 to listOf(
            MessageEntity(chatId = 6, message = "Hi Frank!", timestamp = "10:05 AM", senderId = 0),
            MessageEntity(chatId = 6, message = "Hello!", timestamp = "10:10 AM", senderId = 6)
        ),
        // Messages for the chat with Grace
        7 to listOf(
            MessageEntity(chatId = 7, message = "Hi Grace!", timestamp = "10:06 AM", senderId = 0),
            MessageEntity(chatId = 7, message = "Namaste, how are you?", timestamp = "10:11 AM", senderId = 7),
            MessageEntity(chatId = 7, message = "I'm fine, thank you!", timestamp = "10:16 AM", senderId = 0)
        )
    )



}
