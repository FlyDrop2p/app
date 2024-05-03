package com.example.flydrop2p.ui.screen.Chat

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flydrop2p.domain.model.Message


@Composable
fun ChatScreen(chatViewModel: ChatViewModel, modifier: Modifier = Modifier) {

    val chatState by chatViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        MessagesList(messages = chatState.chat.messages, chatViewModel, modifier = Modifier.weight(1f))

        SendMessageInput(onSendMessage = { message ->
            val currentTimeMillis = System.currentTimeMillis()
            chatViewModel.addMessage(Message(
                messageId = 0,
                senderId = 0,
                timestamp = currentTimeMillis.toString(),
                receiverId = 0, // ID del destinatario
                message = message
            ))
        })
    }
}

@Composable
fun MessagesList(messages: List<Message>, chatViewModel: ChatViewModel, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        messages.forEach { message ->
            MessageItem(message = message, chatViewModel = chatViewModel)
        }
    }
}

@Composable
fun MessageItem(message: Message, chatViewModel: ChatViewModel) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = chatViewModel.getSenderName(message.senderId),
            fontSize = 16.sp,
            color = Color.Gray
        )
        Text(
            text = message.message,
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}

@Composable
fun SendMessageInput(onSendMessage: (String) -> Unit) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Casella di input per il testo del messaggio
        TextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
            },
            placeholder = { Text(text = "Scrivi un messaggio...") },
            modifier = Modifier.weight(1f)
        )

        // Pulsante per inviare il messaggio
        IconButton(
            onClick = {
                onSendMessage(textFieldValue.text)
                textFieldValue = TextFieldValue()
            },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
                tint = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun ChatScreenPreview() {

}