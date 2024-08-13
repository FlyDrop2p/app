package com.flydrop2p.flydrop2p.data

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.domain.model.ChatInfo
import com.flydrop2p.flydrop2p.domain.model.Contact

object DataSource {
    val contacts = listOf<Contact>(
//        Contact(0, "Me"),
//        Contact(1, "Alice"),
//        Contact(2, "Bob"),
//        Contact(3, "Charlie"),
//        Contact(4, "David"),
//        Contact(5, "Eve"),
//        Contact(6, "Frank"),
//        Contact(7, "Grace")
    )

    val chatsInfoListDatasource = listOf<ChatInfo>(
        // ChatInfo(0, "Chat Pubblica", "Benvenuti nella chat pubblica!", 0), // Chat di gruppo
//        ChatInfo(1, "Alice"),
//        ChatInfo(2, "Bob"),
//        ChatInfo(3, "Charlie"),
//        ChatInfo(4, "David"),
//        ChatInfo(5, "Eve"),
//        ChatInfo(6, "Frank"),
//        ChatInfo(7, "Grace")
    )

    val placeholderMessages = mapOf(
        // Messages for the Group Chat
        -1 to listOf(
            MessageEntity(chatId = -1, content = "Welcome to the group chat!", timestamp = 0, senderId = 0),
            MessageEntity(chatId = -1, content = "Hello everyone!", timestamp = 0, senderId = 1)
        ),
        // Messages for the chat with Alice
        1 to listOf(
            MessageEntity(chatId = 1, content = "Hi Alice!", timestamp = 0, senderId = 0),
            MessageEntity(chatId = 1, content = "How are you?", timestamp = 0, senderId = 1),
            MessageEntity(chatId = 1, content = "I'm fine, thank you!", timestamp = 0, senderId = 0)
        ),
        // Messages for the chat with Bob
        2 to listOf(
            MessageEntity(chatId = 2, content = "Hey Bob!", timestamp = 0, senderId = 0),
            MessageEntity(chatId = 2, content = "Hi!", timestamp = 0, senderId = 2),
            MessageEntity(chatId = 2, content = "I'm also fine, thank you!", timestamp = 0, senderId = 0)
        ),
        // Messages for the chat with Charlie
        3 to listOf(
            MessageEntity(chatId = 3, content = "Hi Charlie!", timestamp = 0, senderId = 0),
            MessageEntity(chatId = 3, content = "Hello!", timestamp = 0, senderId = 3)
        ),
        // Messages for the chat with David
        4 to listOf(
            MessageEntity(chatId = 4, content = "Hello David!", timestamp = 0, senderId = 0),
            MessageEntity(chatId = 4, content = "Hola!", timestamp = 0, senderId = 4)
        ),
        // Messages for the chat with Eve
        5 to listOf(
            MessageEntity(chatId = 5, content = "Hi Eve!", timestamp = 0, senderId = 0),
            MessageEntity(chatId = 5, content = "Bonjour!", timestamp = 0, senderId = 5)
        ),
        // Messages for the chat with Frank
        6 to listOf(
            MessageEntity(chatId = 6, content = "Hi Frank!", timestamp = 0, senderId = 0),
            MessageEntity(chatId = 6, content = "Hello!", timestamp = 0, senderId = 6)
        ),
        // Messages for the chat with Grace
        7 to listOf(
            MessageEntity(chatId = 7, content = "Hi Grace!", timestamp = 0, senderId = 0),
            MessageEntity(chatId = 7, content = "Namaste, how are you?", timestamp = 0, senderId = 7),
            MessageEntity(chatId = 7, content = "I'm fine, thank you!", timestamp = 0, senderId = 0)
        )
    )
}
