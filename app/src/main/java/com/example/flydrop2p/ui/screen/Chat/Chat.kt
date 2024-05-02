package com.example.flydrop2p.ui.screen.Chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flydrop2p.ui.screen.Home.Chat

data class Message(val sender: String, val text: String)

data class SingleChat(val id: Int, val name: String)

@Composable
fun ChatScreen(chatId: Int, modifier: Modifier = Modifier) {
    // Lista di messaggi placeholder
    val placeholderMessages = listOf(
        Message("Alice", "Ciao!"),
        Message("Bob", "Hey! Come va?"),
        Message("Alice", "Bene, grazie! E tu?"),
        Message("Bob", "Anche io bene, grazie!")
    )
    val singleChat = listOf(
        SingleChat(1, "Alice"),
        SingleChat(2, "Bob"),
        SingleChat(3, "Charlie"),
        SingleChat(4, "David"),
        SingleChat(5, "Eve"),
        SingleChat(6, "Frank"),
        SingleChat(7, "Grace")
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // Header con nome della chat
        ChatHeader(singleChat[chatId])

        // Lista di messaggi (messaggi placeholder)
        MessagesList(messages = placeholderMessages, modifier = Modifier.weight(1f))

        // Campo di input per inviare messaggi
        SendMessageInput(onSendMessage = { message ->
            // Gestisci l'invio del messaggio
            // Aggiorna la lista dei messaggi della chat con il nuovo messaggio
        })
    }
}

@Composable
fun ChatHeader(chatInfo: SingleChat) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = chatInfo.name,
            fontSize = 24.sp,
            color = Color.Black
        )
    }
}

@Composable
fun MessagesList(messages: List<Message>, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        messages.forEach { message ->
            MessageItem(message = message)
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    // Singolo messaggio
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = message.sender,
            fontSize = 16.sp,
            color = Color.Gray
        )
        Text(
            text = message.text,
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}

@Composable
fun SendMessageInput(onSendMessage: (String) -> Unit) {
    // Campo di input per inviare messaggi
    // Puoi implementare questo secondo le tue esigenze
}
