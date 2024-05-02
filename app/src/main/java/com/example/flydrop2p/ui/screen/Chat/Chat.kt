package com.example.flydrop2p.ui.screen.Chat

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flydrop2p.data.DataSource
import com.example.flydrop2p.domain.model.ChatInfo
import com.example.flydrop2p.domain.model.Message


@Composable
fun ChatScreen(chatViewModel: ChatViewModel, modifier: Modifier = Modifier) {
    // fetch the chat info from the repository

    val chatInfoState by chatViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ChatHeader(chatInfo = chatInfoState.chatInfo)

        MessagesList(messages = chatInfoState.chatInfo.messages, modifier = Modifier.weight(1f))

        SendMessageInput(onSendMessage = { message ->
            // Aggiorna la lista dei messaggi della chat con il nuovo messaggio
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHeader(chatInfo: ChatInfo) {
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                // torna alla schermata delle chats


            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Go back"
                )
            }
        },
        title = {
            Text(
                text = chatInfo.name,
                fontSize = 26.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(16.dp)
            )
        },
        colors =  TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
        ),
    )
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
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = message.senderId.toString(),
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
