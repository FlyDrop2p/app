package com.flydrop2p.flydrop2p.ui.screen.Chat

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.flydrop2p.flydrop2p.FlydropTopAppBar
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.Message
import com.flydrop2p.flydrop2p.ui.navigation.NavigationDestination


object ChatDestination : NavigationDestination {
    override val route = "chat"
    override val titleRes = R.string.chat_screen
    const val itemIdArg = "chatId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatViewModel: ChatViewModel, navController: NavHostController, modifier: Modifier = Modifier) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val chatId = navBackStackEntry?.arguments?.getInt(ChatDestination.itemIdArg)

    Log.d("ChatScreen", "ChatScreen chatId: ${chatId}")
    if (chatId != null) {
        chatViewModel.getChat(chatId)
    }
    val chatState by chatViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            FlydropTopAppBar(
                title = chatState.chat.name,
                canNavigateBack = true,
                navigateUp = {
                    navController.navigateUp()
                },
                modifier = modifier,
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            MessagesList(
                messages = chatState.chat.messages,
                chatViewModel,
                modifier = Modifier.weight(1f)
            )

            SendMessageInput(onSendMessage = { message ->
                val currentTimeMillis = System.currentTimeMillis()
                chatViewModel.addMessage(
                    Message(
                        messageId = 0,
                        chatState.chat.id,
                        senderId = 0,
                        timestamp = currentTimeMillis.toString(),
                        message = message
                    )
                )
            })
        }
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

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {

}