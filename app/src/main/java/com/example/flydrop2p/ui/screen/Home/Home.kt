package com.example.flydrop2p.ui.screen.Home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.example.flydrop2p.R
import com.example.flydrop2p.domain.model.ChatInfo
import com.example.flydrop2p.ui.screen.Chat.ChatViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    chatViewModel: ChatViewModel,
    onChatClick: (ChatInfo) -> Unit
) {

    val chatsState by homeViewModel.chatsInfoState.collectAsState()

    Scaffold(
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
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

            ChatItem(
                chatInfo = ChatInfo(-1, "Group Chat", R.drawable.campaign_24px),
                onChatClick = onChatClick
            )
            Divider()
            ChatList(chatsState.chats, onChatClick)
        }

    }

}

@Composable
fun ChatList(chatsInfo: List<ChatInfo>, onChatClick: (ChatInfo) -> Unit) {
    LazyColumn {
        items(chatsInfo) { chatInfo ->
            ChatItem(chatInfo = chatInfo, onChatClick = onChatClick)
            Divider()
        }
    }
}


@Composable
fun ChatItem(chatInfo: ChatInfo, onChatClick: (ChatInfo) -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                onChatClick(chatInfo)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = chatInfo.imgId),
            colorFilter = ColorFilter.tint(Color.Black),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = chatInfo.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ultimo messaggio",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "10:00",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}