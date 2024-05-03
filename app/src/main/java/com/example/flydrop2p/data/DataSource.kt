package com.example.flydrop2p.data

import com.example.flydrop2p.domain.model.Chat
import com.example.flydrop2p.domain.model.ChatInfo
import com.example.flydrop2p.domain.model.Message

object DataSource {
    private val chatsInfoList = listOf(
        // ChatInfo(0, "Chat Pubblica", "Benvenuti nella chat pubblica!", "10:00 AM"), // Chat di gruppo
        ChatInfo(1, "Alice", "Sembra una bella giornata!", "10:00 AM"),
        ChatInfo(2, "Bob", "Hai sentito le ultime notizie?", "10:01 AM"),
        ChatInfo(3, "Charlie", "Domani abbiamo una riunione importante.", "10:02 AM"),
        ChatInfo(4, "David", "Ho appena finito di cucinare la cena.", "10:03 AM"),
        ChatInfo(5, "Eve", "Guarda questo meme divertente!", "10:04 AM"),
        ChatInfo(6, "Frank", "Ho bisogno del tuo aiuto con il progetto.", "10:05 AM"),
        ChatInfo(7, "Grace", "Sto pensando di fare una vacanza.", "10:06 AM")
    )

    private val placeholderMessages = mapOf(
        // Messaggi per la Chat Pubblica
        -1 to listOf(
            Message(0, "Benvenuti nella chat pubblica!", "10:00 AM", 0, 1),
            Message(1, "Ciao a tutti!", "10:05 AM", 1, 2)
        ),
        // Messaggi per la chat con Alice
        1 to listOf(
            Message(2, "Ciao Alice!", "10:00 AM", 0, 1),
            Message(3, "Come stai?", "10:05 AM", 1, 0),
            Message(4, "Tutto bene, grazie!", "10:10 AM", 0, 1)
        ),
        // Messaggi per la chat con Bob
        2 to listOf(
            Message(5, "Hey Bob!", "10:01 AM", 0, 2),
            Message(6, "Ciao!", "10:06 AM", 2, 0),
            Message(7, "Anch'io bene, grazie!", "10:11 AM", 0, 2)
        ),
        // Messaggi per la chat con Charlie
        3 to listOf(
            Message(8, "Ciao Charlie!", "10:02 AM", 0, 3),
            Message(9, "Salve!", "10:07 AM", 3, 0)
        ),
        // Messaggi per la chat con David
        4 to listOf(
            Message(10, "Ciao David!", "10:03 AM", 0, 4),
            Message(11, "Hola!", "10:08 AM", 4, 0)
        ),
        // Messaggi per la chat con Eve
        5 to listOf(
            Message(12, "Ciao Eve!", "10:04 AM", 0, 5),
            Message(13, "Bonjour!", "10:09 AM", 5, 0)
        ),
        // Messaggi per la chat con Frank
        6 to listOf(
            Message(14, "Ciao Frank!", "10:05 AM", 0, 6),
            Message(15, "Ciao!", "10:10 AM", 6, 0)
        ),
        // Messaggi per la chat con Grace
        7 to listOf(
            Message(16, "Ciao Grace!", "10:06 AM", 0, 7),
            Message(17, "Namaste, come stai?", "10:11 AM", 7, 0),
            Message(18, "Tutto bene, grazie!", "10:16 AM", 0, 7)
        )
    )

    fun getChatsInfoList(): List<ChatInfo> {
        return chatsInfoList
    }

    fun getChat(chatId: Int): Chat {
        val chatInfo = chatsInfoList.firstOrNull { it.id == chatId }
        val messages = placeholderMessages[chatId] ?: emptyList()
        return chatInfo?.let { Chat(chatInfo.id, chatInfo.name, messages) } ?: Chat(0, "Group Chat", messages) // FIXME: Group Chat
    }
    fun getSenderNameById(senderId: Int): String {
        if (senderId == 0) return "Me"
        val sender = chatsInfoList.firstOrNull { it.id == senderId }
        return sender?.name ?: "Unknown sender"
    }

}
