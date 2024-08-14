package com.flydrop2p.flydrop2p.data

import com.flydrop2p.flydrop2p.domain.model.Account
import com.flydrop2p.flydrop2p.domain.model.Message

object DataSource {
    val contacts = listOf<Account>(
//        Account(accountId = 0, profile = Profile(username = "Me", imageFilePath = null)),
//        Account(accountId = 1, profile = Profile(username = "Alice", imageFilePath = null)),
//        Account(accountId = 2, profile = Profile(username = "Bob", imageFilePath = null)),
//        Account(accountId = 3, profile = Profile(username = "Charlie", imageFilePath = null)),
//        Account(accountId = 4, profile = Profile(username = "David", imageFilePath = null)),
//        Account(accountId = 5, profile = Profile(username = "Eve", imageFilePath = null)),
//        Account(accountId = 6, profile = Profile(username = "Frank", imageFilePath = null)),
//        Account(accountId = 7, profile = Profile(username = "Grace", imageFilePath = null))
    )

    val placeholderMessages = mapOf(
        0 to listOf( // Messaggi per la Chat di Gruppo
            Message(messageId = 0, chatId = 0, content = "Welcome to the group chat!", timestamp = System.currentTimeMillis(), senderId = 0),
            Message(messageId = 1, chatId = 0, content = "Hello everyone!", timestamp = System.currentTimeMillis(), senderId = 1)
        ),
        1 to listOf( // Messaggi per la chat con Alice
            Message(messageId = 2, chatId = 1, content = "Hi Alice!", timestamp = System.currentTimeMillis(), senderId = 0),
            Message(messageId = 3, chatId = 1, content = "How are you?", timestamp = System.currentTimeMillis(), senderId = 1),
            Message(messageId = 4, chatId = 1, content = "I'm fine, thank you!", timestamp = System.currentTimeMillis(), senderId = 0)
        ),
        2 to listOf( // Messaggi per la chat con Bob
            Message(messageId = 5, chatId = 2, content = "Hey Bob!", timestamp = System.currentTimeMillis(), senderId = 0),
            Message(messageId = 6, chatId = 2, content = "Hi!", timestamp = System.currentTimeMillis(), senderId = 2),
            Message(messageId = 7, chatId = 2, content = "I'm also fine, thank you!", timestamp = System.currentTimeMillis(), senderId = 0)
        ),
        3 to listOf( // Messaggi per la chat con Charlie
            Message(messageId = 8, chatId = 3, content = "Hi Charlie!", timestamp = System.currentTimeMillis(), senderId = 0),
            Message(messageId = 9, chatId = 3, content = "Hello!", timestamp = System.currentTimeMillis(), senderId = 3)
        ),
        4 to listOf( // Messaggi per la chat con David
            Message(messageId = 10, chatId = 4, content = "Hello David!", timestamp = System.currentTimeMillis(), senderId = 0),
            Message(messageId = 11, chatId = 4, content = "Hola!", timestamp = System.currentTimeMillis(), senderId = 4)
        ),
        5 to listOf( // Messaggi per la chat con Eve
            Message(messageId = 12, chatId = 5, content = "Hi Eve!", timestamp = System.currentTimeMillis(), senderId = 0),
            Message(messageId = 13, chatId = 5, content = "Bonjour!", timestamp = System.currentTimeMillis(), senderId = 5)
        ),
        6 to listOf( // Messaggi per la chat con Frank
            Message(messageId = 14, chatId = 6, content = "Hi Frank!", timestamp = System.currentTimeMillis(), senderId = 0),
            Message(messageId = 15, chatId = 6, content = "Hello!", timestamp = System.currentTimeMillis(), senderId = 6)
        ),
        7 to listOf( // Messaggi per la chat con Grace
            Message(messageId = 16, chatId = 7, content = "Hi Grace!", timestamp = System.currentTimeMillis(), senderId = 0),
            Message(messageId = 17, chatId = 7, content = "Namaste, how are you?", timestamp = System.currentTimeMillis(), senderId = 7),
            Message(messageId = 18, chatId = 7, content = "I'm fine, thank you!", timestamp = System.currentTimeMillis(), senderId = 0)
        )
    )
}
