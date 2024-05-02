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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
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


data class Chat(val id: Int, val name: String, val message: String, val time: String)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onChatClick : (Int) -> Unit
){

        val chats = listOf(
            Chat(1, "Alice", "Hello", "10:00 AM"),
            Chat(2, "Bob", "Hi", "10:01 AM"),
            Chat(3, "Charlie", "Hey", "10:02 AM"),
            Chat(4, "David", "Hola", "10:03 AM"),
            Chat(5, "Eve", "Bonjour", "10:04 AM"),
            Chat(6, "Frank", "Ciao", "10:05 AM"),
            Chat(7, "Grace", "Namaste, how are you?", "10:06 AM"),
        )
    ChatList(chats, onChatClick)

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
            .fillMaxWidth().clickable {
                onChatClick(chat.id)
            },
        verticalAlignment = Alignment.CenterVertically
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
        Column {
            Text(
                text = chat.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = chat.message,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = chat.time,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}