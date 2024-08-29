package com.flydrop2p.flydrop2p.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.chat.ChatPreview
import com.flydrop2p.flydrop2p.domain.model.device.Account
import com.flydrop2p.flydrop2p.domain.model.device.Contact
import com.flydrop2p.flydrop2p.domain.model.device.Profile
import com.flydrop2p.flydrop2p.domain.model.message.AudioMessage
import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import com.flydrop2p.flydrop2p.domain.model.message.MessageState
import com.flydrop2p.flydrop2p.domain.model.message.TextMessage
import com.flydrop2p.flydrop2p.ui.FlyDropTopAppBar
import com.flydrop2p.flydrop2p.ui.components.getMimeType
import com.flydrop2p.flydrop2p.ui.navigation.NavigationDestination
import com.flydrop2p.flydrop2p.ui.screen.call.CallDestination
import com.flydrop2p.flydrop2p.ui.screen.call.CallState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    onChatClick: (Contact) -> Unit,
    onSettingsButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val callRequest by homeViewModel.networkManager.callRequest.collectAsState()

    LaunchedEffect(callRequest) {
        callRequest?.let {
            navController.navigate("${CallDestination.route}/${it.senderId}/${CallState.RECEIVED_CALL_REQUEST.name}")
        }
    }

    val homeState by homeViewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            FlyDropTopAppBar(
                title = "Chat",
                canNavigateBack = false,
                isSettingsScreen = false,
                onConnectionButtonClick = { homeViewModel.connect() },
                onSettingsButtonClick = onSettingsButtonClick,
                modifier = modifier
            )
        },
        content = { innerPadding ->
            ChatList(
                chatPreviews = homeState.chatPreviews,
                onlineChats = homeState.onlineChats,
                onChatClick = onChatClick,
                modifier = Modifier.padding(innerPadding)
            )
        },
    )
}

@Composable
fun ChatList(
    chatPreviews: List<ChatPreview>,
    onlineChats: Set<Long>,
    onChatClick: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(chatPreviews) { chatPreview ->
            ChatItem(
                chatPreview = chatPreview,
                online = onlineChats.contains(chatPreview.contact.accountId),
                onChatClick = onChatClick
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = 82.dp, end = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        }
    }
}

@Composable
fun ChatItem(
    chatPreview: ChatPreview,
    online: Boolean,
    onChatClick: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTime = System.currentTimeMillis()
    val messageTime = chatPreview.lastMessage?.timestamp ?: currentTime
    val calendar = Calendar.getInstance()

    calendar.timeInMillis = currentTime
    val today = calendar.get(Calendar.DAY_OF_YEAR)

    calendar.timeInMillis = messageTime
    val messageDay = calendar.get(Calendar.DAY_OF_YEAR)
    val messageYear = calendar.get(Calendar.YEAR)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val timeString = when {
        today == messageDay && currentYear == messageYear -> {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(messageTime))
        }

        today - 1 == messageDay && currentYear == messageYear -> {
            "Yesterday"
        }

        today - messageDay in 1..6 && currentYear == messageYear -> {
            SimpleDateFormat("EEE", Locale.getDefault()).format(Date(messageTime))
        }

        else -> {
            SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date(messageTime))
        }
    }

    Row(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                onChatClick(chatPreview.contact)
            },
        verticalAlignment = Alignment.Top
    ) {
        val imageModifier = Modifier
            .size(50.dp)
            .clip(CircleShape)

        if (chatPreview.contact.imageFileName != null) {
            Image(
                painter = rememberAsyncImagePainter(model = chatPreview.contact.imageFileName?.let {
                    File(LocalContext.current.filesDir, it)
                }),
                contentDescription = "Immagine profilo",
                modifier = imageModifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.account_circle_24px),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                contentDescription = "Immagine di default",
                modifier = imageModifier
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chatPreview.contact.username ?: "Connecting...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (online) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF16a34a))
                    ) {}
                }
            }

            when (chatPreview.lastMessage) {
                is TextMessage -> {
                    Text(
                        text = chatPreview.lastMessage.text,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                is FileMessage -> {

                    val mimeType =
                        getMimeType(chatPreview.lastMessage.fileName.substringAfterLast(".", ""))

                    if (mimeType.startsWith("video/")) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.play_arrow_24px),
                                contentDescription = "Video",
                                colorFilter = ColorFilter.tint(Color.Gray),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Video",
                                fontSize = 14.sp,
                                color = Color.Gray,
                            )
                        }
                    } else {
                        chatPreview.lastMessage.fileName.let {
                            Text(
                                text = it,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                is AudioMessage -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.mic_24px),
                            contentDescription = "Audio",
                            colorFilter = ColorFilter.tint(Color.Gray),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Audio",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(${chatPreview.lastMessage.formatDuration(LocalContext.current)})",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                null -> {}
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = timeString ?: "",
                fontSize = 10.sp,
                color = Color.Gray
            )

            if (chatPreview.unreadMessagesCount > 0) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B72C0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = chatPreview.unreadMessagesCount.toString(),
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatItemPreview() {
    ChatItem(
        chatPreview = ChatPreview(
            contact = Contact(
                account = Account(
                    accountId = 1, profileUpdateTimestamp = 1
                ), profile = Profile(
                    accountId = 1, 0, username = "Alice", imageFileName = null
                )
            ),
            unreadMessagesCount = 1,
            lastMessage = TextMessage(
                messageId = 1,
                senderId = 1,
                receiverId = 2,
                text = "Ciao!",
                timestamp = System.currentTimeMillis(),
                messageState = MessageState.MESSAGE_READ,
            ),
        ),
        online = true,
        onChatClick = {

        })
}