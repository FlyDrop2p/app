package com.flydrop2p.flydrop2p.ui.screen.chat

import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.flydrop2p.flydrop2p.ChatTopAppBar
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import com.flydrop2p.flydrop2p.domain.model.message.Message
import com.flydrop2p.flydrop2p.domain.model.message.TextMessage
import com.flydrop2p.flydrop2p.ui.components.FileMessageComponent
import com.flydrop2p.flydrop2p.ui.components.FilePreview
import com.flydrop2p.flydrop2p.ui.components.TextMessageComponent
import com.flydrop2p.flydrop2p.ui.navigation.NavigationDestination
import java.io.File
import java.io.FileOutputStream

object ChatDestination : NavigationDestination {
    override val route = "chat"
    override val titleRes = R.string.chat_screen
    const val itemIdArg = "chatId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun ChatScreen(
    accountId: Long,
    chatViewModel: ChatViewModel,
    navController: NavHostController,
    onConnectionButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentAccount by chatViewModel.ownAccount.collectAsState(initial = null)
    val chatState by chatViewModel.uiState.collectAsState()

    var attachedFile by remember { mutableStateOf<File?>(null) }

    Scaffold(
        topBar = {
            ChatTopAppBar(
                title = chatState.contact.username ?: "Connecting...",
                canNavigateBack = true,
                onConnectionButtonClick = onConnectionButtonClick,
                onSettingsButtonClick = onSettingsButtonClick,
                modifier = modifier,
                navigateUp = { navController.navigateUp() },
                contactImageFileName = chatState.contact.imageFileName
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            currentAccount?.let { account ->
                MessagesList(
                    messages = chatState.messages,
                    chatViewModel = chatViewModel,
                    accountId = account.accountId,
                    modifier = Modifier.weight(1f)
                )

                if (attachedFile != null) {
                    // Mostra la preview del file se è stato allegato
                    FilePreview(
                        file = attachedFile!!,
                        onSendFile = {
                            chatViewModel.sendFileMessage(accountId, it)
                            attachedFile = null
                        },
                        onDeleteFile = {
                            attachedFile = null
                        }
                    )
                } else {
                    SendMessageInput(
                        onSendMessage = { messageText ->
                            chatViewModel.sendTextMessage(accountId, messageText)
                        },
                        onAttachFile = { uri ->
                            uri?.let {
                                Log.d("ChatScreen", "File attached: $uri")

                                val fileName =
                                    context.contentResolver.query(uri, null, null, null, null)
                                        ?.use { cursor ->
                                            val nameIndex =
                                                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                            if (cursor.moveToFirst()) {
                                                cursor.getString(nameIndex)
                                            } else {
                                                null
                                            }
                                        }

                                Log.d("ChatScreen", "File name: $fileName")

                                val file = File(
                                    context.filesDir,
                                    fileName ?: "${System.currentTimeMillis()}_file"
                                )

                                val inputStream = context.contentResolver.openInputStream(uri)
                                inputStream?.use { input ->
                                    val outputStream = FileOutputStream(file)
                                    outputStream.use { output ->
                                        input.copyTo(output)
                                    }
                                }

                                attachedFile = file
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MessagesList(
    messages: List<Message>,
    chatViewModel: ChatViewModel,
    accountId: Long,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        items(messages) { message ->
            MessageItem(message = message, accountId = accountId, chatViewModel = chatViewModel)
        }
    }
}

@Composable
fun MessageItem(message: Message, accountId: Long, chatViewModel: ChatViewModel) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        when (message) {
            is TextMessage -> {
                TextMessageComponent(message, true, currentAccountId = accountId)
            }

            is FileMessage -> {
                FileMessageComponent(message, true, currentAccountId = accountId)
            }
        }
    }
}

@Composable
fun SendMessageInput(
    onSendMessage: (String) -> Unit,
    onAttachFile: (Uri?) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onAttachFile(uri)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { filePickerLauncher.launch("*/*") },
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Attach file",
                tint = Color.Black
            )
        }

        TextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            placeholder = { Text("Scrivi un messaggio...") },
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