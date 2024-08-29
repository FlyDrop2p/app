package com.flydrop2p.flydrop2p.ui.components

import MessageStatusIndicator
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.message.AudioMessage
import kotlinx.coroutines.delay

@Composable
fun AudioMessageComponent(
    message: AudioMessage,
    currentAccountId: Long,
    startPlayingAudio: (String, Int) -> Unit,
    stopPlayingAudio: () -> Unit,
    getCurrentPlaybackPosition: () -> Int,
    isPlaybackComplete: () -> Boolean,
) {
    val context = LocalContext.current

    val isPlaying = remember { mutableStateOf(false) }
    val playbackPosition = remember { mutableIntStateOf(0) }
    val duration = remember { message.getAudioDuration(message.getFilePath(context)) }

    val formattedDuration = remember { message.formatDuration(context) }

    LaunchedEffect(isPlaying.value) {
        while (isPlaying.value && !isPlaybackComplete()) {
            playbackPosition.intValue = getCurrentPlaybackPosition()
            delay(500)
        }
        if (isPlaybackComplete() || (getCurrentPlaybackPosition().toLong() == duration)) {
            playbackPosition.intValue = 0
            isPlaying.value = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp),
        horizontalArrangement = if (message.senderId == currentAccountId) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                .background(Color(0xFFEFEFEF))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                IconButton(
                    onClick = {
                        if (isPlaying.value) {
                            stopPlayingAudio()
                        } else {
                            startPlayingAudio(message.fileName, playbackPosition.intValue)
                        }
                        isPlaying.value = !isPlaying.value
                    },
                    modifier = Modifier
                        .size(45.dp)
                        .background(Color(0xFF323232), shape = CircleShape)
                        .padding(8.dp)
                        .animateContentSize()
                ) {
                    Icon(
                        painter = painterResource(id = if (isPlaying.value) R.drawable.pause_24px else R.drawable.play_arrow_24px),
                        contentDescription = "Audio Icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
            Text(
                text = "${message.formatDuration(playbackPosition.intValue)} / $formattedDuration",
                fontSize = 10.sp,
                color = Color(0xFF083249),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .align(Alignment.TopEnd)
            )


            MessageStatusIndicator(
                message = message,
                currentAccountId = currentAccountId,
                backgroundColor = Color(0x00EFEFEF),
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
