package com.flydrop2p.flydrop2p.ui.screen.chat

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.flydrop2p.flydrop2p.FlyDropTopAppBar
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.Message
import com.flydrop2p.flydrop2p.ui.components.PrivateMessage
import com.flydrop2p.flydrop2p.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object ChatDestination : NavigationDestination {
    override val route = "chat"
    override val titleRes = R.string.chat_screen
    const val itemIdArg = "chatId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun ChatScreen(
    accountId: Int,
    chatViewModel: ChatViewModel,
    navController: NavHostController,
    onConnectionButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chatState by chatViewModel.uiState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        topBar = {
            FlyDropTopAppBar(
                title = chatState.contact.username,
                canNavigateBack = true,
                onConnectionButtonClick = onConnectionButtonClick,
                onSettingsButtonClick = onSettingsButtonClick,
                modifier = modifier,
                navigateUp = {
                    navController.navigateUp()
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MessagesList(
                messages = chatState.messages,
                chatViewModel,
                modifier = Modifier.weight(1f)
            )

            SendMessageInput(onSendMessage = { messageText ->
                val currentTimeMillis = System.currentTimeMillis()
                val message = Message(
                    messageId = 0,
                    senderId = 0,
                    receiverId = 0,
                    timestamp = currentTimeMillis,
                    content = messageText
                )

                chatViewModel.addMessage(message)
                Log.d("New message", "Message added to chat ${chatState}")

                val receiverIp = chatState.contact.accountId.toString() // TODO: change this to the actual receiver IP

                chatViewModel.sendMessage(receiverIp, messageText)
            })
        }
    }
}

@Composable
fun MessagesList(messages: List<Message>, chatViewModel: ChatViewModel, modifier: Modifier) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        items(messages) { message ->
            MessageItem(message = message, chatViewModel = chatViewModel)
        }

    }
}

@Composable
fun MessageItem(message: Message, chatViewModel: ChatViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        PrivateMessage(message, true)
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

        TextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
            },
            placeholder = { Text(text = "Scrivi un messaggio...") },
            modifier = Modifier.weight(1f)
        )

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