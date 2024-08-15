package com.flydrop2p.flydrop2p.ui.screen.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flydrop2p.flydrop2p.FlyDropTopAppBar
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.ChatPreview
import com.flydrop2p.flydrop2p.domain.model.Contact
import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import com.flydrop2p.flydrop2p.domain.model.message.TextMessage
import com.flydrop2p.flydrop2p.ui.navigation.NavigationDestination
import com.flydrop2p.flydrop2p.ui.screen.chat.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    chatViewModel: ChatViewModel,
    onChatClick: (Contact) -> Unit,
    onConnectionButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            FlyDropTopAppBar(
                title = "Chat",
                canNavigateBack = false,
                onConnectionButtonClick = onConnectionButtonClick,
                onSettingsButtonClick = onSettingsButtonClick,
                modifier = modifier
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.d("ChatScreen", "FloatingActionButton onClick")
                    chatViewModel.populateDatabase()
                    homeViewModel.populateDatabase()
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                )
            }
        },
        content = { innerPadding ->
            ChatList(
                chatPreviews = uiState.chatPreviews,
                onChatClick = onChatClick,
                modifier = Modifier.padding(innerPadding)
            )
        }
    )
}

@Composable
fun ChatList(chatPreviews: List<ChatPreview>, onChatClick: (Contact) -> Unit, modifier: Modifier = Modifier) { // Aggiungi il parametro modifier qui
    LazyColumn(modifier = modifier) {
        items(chatPreviews) { chatPreview ->
            ChatItem(chatPreview = chatPreview, onChatClick = onChatClick)
            HorizontalDivider(
                modifier = Modifier.padding(start = 82.dp, end = 16.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun ChatItem(chatPreview: ChatPreview, onChatClick: (Contact) -> Unit, modifier: Modifier = Modifier) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = chatPreview.lastMessage?.let { Date(it.timestamp) }
        ?.let { timeFormat.format(it) }

    Row(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                onChatClick(chatPreview.contact)
            },
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.account_circle_24px),
            colorFilter = ColorFilter.tint(Color.Black),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chatPreview.contact.username,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            when(chatPreview.lastMessage) {
                is TextMessage -> {
                    Text(
                        text = chatPreview.lastMessage.text,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                is FileMessage -> {
                    chatPreview.lastMessage.file.let {
                        Text(
                            text = it.name,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                null -> {}
            }
        }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = timeString ?: "",
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}