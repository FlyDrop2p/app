package com.example.flydrop2p.data

import com.example.flydrop2p.domain.model.ChatInfo
import com.example.flydrop2p.domain.model.Contact
import com.example.flydrop2p.domain.model.Message

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
            Message(0, -1, "Welcome to the group chat!", "10:00 AM", 0),
            Message(1, -1, "Hello everyone!", "10:05 AM", 1)
        ),
        // Messages for the chat with Alice
        1 to listOf(
            Message(2, 1, "Hi Alice!", "10:00 AM", 0),
            Message(3, 1, "How are you?", "10:05 AM", 1),
            Message(4, 1, "I'm fine, thank you!", "10:10 AM", 0)
        ),
        // Messages for the chat with Bob
        2 to listOf(
            Message(5, 2, "Hey Bob!", "10:01 AM", 0),
            Message(6, 2, "Hi!", "10:06 AM", 2),
            Message(7, 2, "I'm also fine, thank you!", "10:11 AM", 0)
        ),
        // Messages for the chat with Charlie
        3 to listOf(
            Message(8, 3, "Hi Charlie!", "10:02 AM", 0),
            Message(9, 3, "Hello!", "10:07 AM", 3)
        ),
        // Messages for the chat with David
        4 to listOf(
            Message(10, 4, "Hello David!", "10:03 AM", 0),
            Message(11, 4, "Hola!", "10:08 AM", 4)
        ),
        // Messages for the chat with Eve
        5 to listOf(
            Message(12, 5, "Hi Eve!", "10:04 AM", 0),
            Message(13, 5, "Bonjour!", "10:09 AM", 5)
        ),
        // Messages for the chat with Frank
        6 to listOf(
            Message(14, 6, "Hi Frank!", "10:05 AM", 0),
            Message(15, 6, "Hello!", "10:10 AM", 6)
        ),
        // Messages for the chat with Grace
        7 to listOf(
            Message(16, 7, "Hi Grace!", "10:06 AM", 0),
            Message(17, 7, "Namaste, how are you?", "10:11 AM", 7),
            Message(18, 7, "I'm fine, thank you!", "10:16 AM", 0)
        )
    )


}
