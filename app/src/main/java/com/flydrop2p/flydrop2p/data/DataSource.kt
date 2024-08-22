package com.flydrop2p.flydrop2p.data

import com.flydrop2p.flydrop2p.domain.model.contact.Contact
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import com.flydrop2p.flydrop2p.domain.model.message.TextMessage

object DataSource {
    val contacts = listOf(
        // Contact(accountId = 0, profile = Profile(username = "Me")),
        Contact(accountId = 7, profile = Profile(username = "Alice", imageFileName = null)),
        Contact(accountId = 6, profile = Profile(username = "Bob", imageFileName = null)),
        Contact(accountId = 3, profile = Profile(username = "Charlie", imageFileName = null)),
        Contact(accountId = 4, profile = Profile(username = "David", imageFileName = null)),
        Contact(accountId = 5, profile = Profile(username = "Eve", imageFileName = null)),
        Contact(accountId = 15, profile = Profile(username = "Frank", imageFileName = null)),
        Contact(accountId = 12, profile = Profile(username = "Grace", imageFileName = null))
    )


    val placeholderMessages = listOf(
        TextMessage(senderId = 0, receiverId = 7, text = "Hi Alice!", timestamp = System.currentTimeMillis() - 60 * 60 * 1000L), // 1 ora fa
        TextMessage(senderId = 7, receiverId = 0, text = "Hello! How are you?", timestamp = System.currentTimeMillis() - 59 * 60 * 1000L), // 59 minuti fa

        TextMessage(senderId = 0, receiverId = 6, text = "Hey Bob!", timestamp = System.currentTimeMillis() - 2 * 60 * 60 * 1000L), // 2 ore fa
        TextMessage(senderId = 6, receiverId = 0, text = "Hi! What's up?", timestamp = System.currentTimeMillis() - 1 * 60 * 60 * 1000L + 30 * 60 * 1000L), // 1 ora e 30 minuti fa

        TextMessage(senderId = 0, receiverId = 3, text = "Hi Charlie!", timestamp = System.currentTimeMillis() - 3 * 60 * 60 * 1000L), // 3 ore fa
        TextMessage(senderId = 3, receiverId = 0, text = "Hello!", timestamp = System.currentTimeMillis() - 2 * 60 * 60 * 1000L + 45 * 60 * 1000L), // 2 ore e 15 minuti fa

        TextMessage(senderId = 0, receiverId = 4, text = "Hello David!", timestamp = System.currentTimeMillis() - 4 * 60 * 60 * 1000L), // 4 ore fa
        TextMessage(senderId = 4, receiverId = 0, text = "Hola!", timestamp = System.currentTimeMillis() - 3 * 60 * 60 * 1000L + 30 * 60 * 1000L), // 3 ore e 30 minuti fa

        TextMessage(senderId = 0, receiverId = 5, text = "Hi Eve!", timestamp = System.currentTimeMillis() - 5 * 60 * 60 * 1000L), // 5 ore fa
        TextMessage(senderId = 5, receiverId = 0, text = "Bonjour!", timestamp = System.currentTimeMillis() - 4 * 60 * 60 * 1000L + 45 * 60 * 1000L), // 4 ore e 15 minuti fa

        TextMessage( senderId = 0, receiverId = 15, text = "Hi Frank!", timestamp = System.currentTimeMillis() - 6 * 60 * 60 * 1000L), // 6 ore fa
        TextMessage( senderId = 15, receiverId = 0, text = "Hello!", timestamp = System.currentTimeMillis() - 5 * 60 * 60 * 1000L + 30 * 60 * 1000L), // 5 ore e 30 minuti fa

        TextMessage( senderId = 0, receiverId = 12, text = "Hi Grace!", timestamp = System.currentTimeMillis() - 7 * 60 * 60 * 1000L), // 7 ore fa
        TextMessage( senderId = 12, receiverId = 0, text = "Namaste, how are you?", timestamp = System.currentTimeMillis() - 6 * 60 * 60 * 1000L + 45 * 60 * 1000L) // 6 ore e 15 minuti fa
    )
}
