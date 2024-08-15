package com.flydrop2p.flydrop2p.data

import com.flydrop2p.flydrop2p.domain.model.Contact
import com.flydrop2p.flydrop2p.domain.model.Message
import com.flydrop2p.flydrop2p.domain.model.Profile

object DataSource {
    val contacts = listOf(
        // Contact(accountId = 0, profile = Profile(username = "Me")),
        Contact(accountId = 7, profile = Profile(username = "Alice")),
        Contact(accountId = 6, profile = Profile(username = "Bob")),
        Contact(accountId = 3, profile = Profile(username = "Charlie")),
        Contact(accountId = 4, profile = Profile(username = "David")),
        Contact(accountId = 5, profile = Profile(username = "Eve")),
        Contact(accountId = 15, profile = Profile(username = "Frank")),
        Contact(accountId = 12, profile = Profile(username = "Grace"))
    )


    val placeholderMessages = listOf(
        Message(messageId = 0, senderId = 0, receiverId = 7, content = "Hi Alice!", timestamp = System.currentTimeMillis() - 60 * 60 * 1000L), // 1 ora fa
        Message(messageId = 1, senderId = 7, receiverId = 0, content = "Hello! How are you?", timestamp = System.currentTimeMillis() - 59 * 60 * 1000L), // 59 minuti fa

        Message(messageId = 2, senderId = 0, receiverId = 6, content = "Hey Bob!", timestamp = System.currentTimeMillis() - 2 * 60 * 60 * 1000L), // 2 ore fa
        Message(messageId = 3, senderId = 6, receiverId = 0, content = "Hi! What's up?", timestamp = System.currentTimeMillis() - 1 * 60 * 60 * 1000L + 30 * 60 * 1000L), // 1 ora e 30 minuti fa

        Message(messageId = 4, senderId = 0, receiverId = 3, content = "Hi Charlie!", timestamp = System.currentTimeMillis() - 3 * 60 * 60 * 1000L), // 3 ore fa
        Message(messageId = 5, senderId = 3, receiverId = 0, content = "Hello!", timestamp = System.currentTimeMillis() - 2 * 60 * 60 * 1000L + 45 * 60 * 1000L), // 2 ore e 15 minuti fa

        Message(messageId = 6, senderId = 0, receiverId = 4, content = "Hello David!", timestamp = System.currentTimeMillis() - 4 * 60 * 60 * 1000L), // 4 ore fa
        Message(messageId = 7, senderId = 4, receiverId = 0, content = "Hola!", timestamp = System.currentTimeMillis() - 3 * 60 * 60 * 1000L + 30 * 60 * 1000L), // 3 ore e 30 minuti fa

        Message(messageId = 8, senderId = 0, receiverId = 5, content = "Hi Eve!", timestamp = System.currentTimeMillis() - 5 * 60 * 60 * 1000L), // 5 ore fa
        Message(messageId = 9, senderId = 5, receiverId = 0, content = "Bonjour!", timestamp = System.currentTimeMillis() - 4 * 60 * 60 * 1000L + 45 * 60 * 1000L), // 4 ore e 15 minuti fa

        Message(messageId = 10, senderId = 0, receiverId = 15, content = "Hi Frank!", timestamp = System.currentTimeMillis() - 6 * 60 * 60 * 1000L), // 6 ore fa
        Message(messageId = 11, senderId = 15, receiverId = 0, content = "Hello!", timestamp = System.currentTimeMillis() - 5 * 60 * 60 * 1000L + 30 * 60 * 1000L), // 5 ore e 30 minuti fa

        Message(messageId = 12, senderId = 0, receiverId = 12, content = "Hi Grace!", timestamp = System.currentTimeMillis() - 7 * 60 * 60 * 1000L), // 7 ore fa
        Message(messageId = 13, senderId = 12, receiverId = 0, content = "Namaste, how are you?", timestamp = System.currentTimeMillis() - 6 * 60 * 60 * 1000L + 45 * 60 * 1000L) // 6 ore e 15 minuti fa
    )
}
