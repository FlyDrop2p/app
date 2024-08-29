package com.flydrop2p.flydrop2p.ui.components

import MessageStatusIndicator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flydrop2p.flydrop2p.domain.model.message.TextMessage

@Composable
fun TextMessageComponent(message: TextMessage, currentAccountId: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp),
        horizontalArrangement = if (message.senderId == currentAccountId) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(min = 100.dp, max = 300.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                .background(if (message.senderId == currentAccountId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row (
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 22.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 16.sp,
                    color = if (message.senderId == currentAccountId) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                )
            }

            MessageStatusIndicator(
                message = message,
                currentAccountId = currentAccountId,
                backgroundColor = Color(0x00EFEFEF),
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
