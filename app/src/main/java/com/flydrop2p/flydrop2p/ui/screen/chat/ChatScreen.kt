package com.flydrop2p.flydrop2p.ui.screen.chat

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.flydrop2p.flydrop2p.ChatTopAppBar
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.message.AudioMessage
import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import com.flydrop2p.flydrop2p.domain.model.message.Message
import com.flydrop2p.flydrop2p.domain.model.message.TextMessage
import com.flydrop2p.flydrop2p.ui.components.AudioMessageComponent
import com.flydrop2p.flydrop2p.ui.components.AudioRecordingControls
import com.flydrop2p.flydrop2p.ui.components.FileMessageComponent
import com.flydrop2p.flydrop2p.ui.components.FileMessageInput
import com.flydrop2p.flydrop2p.ui.components.TextMessageComponent
import com.flydrop2p.flydrop2p.ui.components.TextMessageInput
import com.flydrop2p.flydrop2p.ui.navigation.NavigationDestination

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
    val currentAccount by chatViewModel.ownAccount.collectAsState(initial = null)
    val chatState by chatViewModel.uiState.collectAsState()

    chatViewModel.updateMessagesState(chatState.messages)

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

                SendMessageInput(
                    onStartRecording = {
                        chatViewModel.startRecordingAudio()
                    },
                    onStopRecording = {
                        chatViewModel.stopRecordingAudio()
                    },
                    onCancelRecording = {
                        chatViewModel.stopRecordingAudio()
                    },
                    onSendTextMessage = { messageText ->
                        chatViewModel.sendTextMessage(accountId, messageText)
                    },
                    onSendFileMessage = { fileUri ->
                        chatViewModel.sendFileMessage(accountId, fileUri)
                    },
                    onSendAudioMessage = {
                        chatViewModel.sendAudioMessage(accountId)
                    }
                )
            }
        }
    }
}


@Composable
fun MessagesList(
    messages: List<Message>, chatViewModel: ChatViewModel, accountId: Long, modifier: Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        items(messages) { message ->
            MessageItem(message = message, accountId = accountId, chatViewModel = chatViewModel)
        }
    }
}

@Composable
fun MessageItem(message: Message, accountId: Long, chatViewModel: ChatViewModel) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        when (message) {
            is TextMessage -> {
                TextMessageComponent(message, currentAccountId = accountId)
            }

            is FileMessage -> {
                FileMessageComponent(message, currentAccountId = accountId)
            }

            is AudioMessage -> {
                AudioMessageComponent(message,
                    currentAccountId = accountId,
                    startPlayingAudio = {
                        chatViewModel.startPlayingAudio(it)
                    },
                    stopPlayingAudio = {
                        chatViewModel.stopPlayingAudio()
                    }
                )
            }
        }
    }
}

@Composable
fun SendMessageInput(
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onCancelRecording: () -> Unit,
    onSendTextMessage: (String) -> Unit,
    onSendFileMessage: (Uri) -> Unit,
    onSendAudioMessage: () -> Unit,
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }
    var isRecording by remember { mutableStateOf(false) }
    var isTyping by remember { mutableStateOf(false) }

    var attachedFileUri by remember { mutableStateOf<Uri?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            attachedFileUri = it
        }
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            if (!isTyping && !isRecording) {
                FileMessageInput(
                    fileUri = attachedFileUri,
                    onSendFile = { uri ->
                        onSendFileMessage(uri)
                        attachedFileUri = null
                    },
                    onClick = {
                        filePickerLauncher.launch("*/*")
                    },
                    onDeleteFile = {
                        attachedFileUri = null
                    }
                )
            }

            if (attachedFileUri == null && !isRecording) {
                TextMessageInput(
                    isTyping = isTyping,
                    textFieldValue = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                        isTyping = it.text.isNotEmpty()
                    },
                    onSendTextMessage = onSendTextMessage
                )
            }

            if ((!isTyping && attachedFileUri == null) || isRecording) {
                AudioRecordingControls(
                    isRecording = isRecording,
                    onStartRecording = {
                        onStartRecording()
                        isRecording = true
                    },
                    onStopRecording = {
                        onStopRecording()
                        isRecording = false
                    },
                    onCancelRecording = {
                        onCancelRecording()
                        isRecording = false
                    },
                    onSendAudioMessage = onSendAudioMessage
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    SendMessageInput(
        onStartRecording = {},
        onStopRecording = {},
        onCancelRecording = {},
        onSendTextMessage = {},
        onSendFileMessage = {},
        onSendAudioMessage = {}
    )
}