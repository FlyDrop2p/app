package com.example.flydrop2p.ui.screen.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.flydrop2p.data.DataSource
import com.example.flydrop2p.domain.model.Chat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    // viewModel: HomeViewModel = viewModel(),
    onChatClick : (Int) -> Unit
){

    val chats = DataSource.getChats()

    Column {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chat",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { /* Action for custom icon */ }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.wifi_tethering_24px),
                            contentDescription = "Settings",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    IconButton(
                        onClick = { /* Action for settings icon */ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            },
            colors =  TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
            ),
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        ChatItem(chat = Chat(0, "Group Chat", "Ciao a tutti", "now", R.drawable.campaign_24px), onChatClick = onChatClick)
        Divider()
        ChatList(chats, onChatClick)
    }

}

@Composable
fun ChatList(chats: List<Chat>, onChatClick : (Int) -> Unit) {
    LazyColumn{
        items(chats) { chat ->
            ChatItem(chat = chat, onChatClick = onChatClick)
            Divider()
        }
    }
}


@Composable
fun ChatItem(chat: Chat, onChatClick : (Int) -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                onChatClick(chat.id)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = chat.imgId),
            colorFilter = ColorFilter.tint(Color.Black),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = chat.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = chat.lastMessage,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = chat.timestamp,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}