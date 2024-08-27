package com.flydrop2p.flydrop2p.ui.components

import android.net.Uri
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.message.AudioMessage
import com.flydrop2p.flydrop2p.domain.model.message.MessageState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SentAudioMessageComponent(
    message: AudioMessage,
    startPlayingAudio: (String) -> Unit,
    stopPlayingAudio: () -> Unit
) {
    val context = LocalContext.current
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(Date(message.timestamp))

    val fileUri = Uri.fromFile(File(context.filesDir, message.fileName))
    val isPlaying = remember { mutableStateOf(false) }

    val formattedDuration = remember { message.formatDuration(context) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
                .widthIn(min = 150.dp, max = 300.dp)
                .clickable {
                    shareFile(context, message.fileName)
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (isPlaying.value) {
                            stopPlayingAudio()
                        } else {
                            startPlayingAudio(message.fileName)
                        }
                        isPlaying.value = !isPlaying.value
                    },
                ) {
                    Icon(
                        painter = painterResource(id = if (isPlaying.value) R.drawable.stop_circle_24px else R.drawable.play_arrow_24px),
                        contentDescription = "Audio Icon",
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))


                Column {
                    Text(
                        text = message.fileName,
                        fontSize = 16.sp,
                        color = Color(0xFF075985),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = formattedDuration, fontSize = 12.sp, color = Color(0xFF083249))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Text(
                                text = timeString,
                                fontSize = 12.sp,
                                color = Color(0xFF083249)
                            )
                            Spacer(modifier = Modifier.width(4.dp))

                            when (message.messageState) {
                                MessageState.MESSAGE_READ -> Image(
                                    painter = painterResource(id = R.drawable.done_all_24px),
                                    colorFilter = ColorFilter.tint(Color(0xFF037971)),
                                    contentDescription = "Visualizzato",
                                    modifier = Modifier.size(16.dp)
                                )

                                MessageState.MESSAGE_RECEIVED -> Image(
                                    painter = painterResource(id = R.drawable.done_all_24px),
                                    colorFilter = ColorFilter.tint(Color(0xFFADADAD)),
                                    contentDescription = "Ricevuto",
                                    modifier = Modifier.size(16.dp)
                                )

                                MessageState.MESSAGE_SENT -> Image(
                                    painter = painterResource(id = R.drawable.check_24px),
                                    colorFilter = ColorFilter.tint(Color(0xFFADADAD)),
                                    contentDescription = "Inviato",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReceivedAudioMessageComponent(
    message: AudioMessage,
    startPlayingAudio: (String) -> Unit,
    stopPlayingAudio: () -> Unit
) {
    val context = LocalContext.current
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(Date(message.timestamp))

    val isPlaying = remember { mutableStateOf(false) }

    val formattedDuration = remember { message.formatDuration(context) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .widthIn(min = 150.dp, max = 300.dp)
                .clickable {
                    shareFile(context, message.fileName)
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (isPlaying.value) {
                            stopPlayingAudio()
                        } else {
                            startPlayingAudio(message.fileName)
                        }
                        isPlaying.value = !isPlaying.value
                    },
                ) {
                    Icon(
                        painter = painterResource(id = if (isPlaying.value) R.drawable.stop_circle_24px else R.drawable.play_arrow_24px),
                        contentDescription = "Audio Icon",
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = message.fileName,
                        fontSize = 16.sp,
                        color = Color(0xFF075985),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = formattedDuration, fontSize = 12.sp, color = Color(0xFF083249))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Text(
                                text = timeString,
                                fontSize = 12.sp,
                                color = Color(0xFF083249)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AudioMessageComponent(
    message: AudioMessage,
    currentAccountId: Long,
    startPlayingAudio: (String) -> Unit,
    stopPlayingAudio: () -> Unit
) {
    if (message.senderId == currentAccountId) {
        SentAudioMessageComponent(
            message = message,
            startPlayingAudio = startPlayingAudio,
            stopPlayingAudio = stopPlayingAudio
        )
    } else {
        ReceivedAudioMessageComponent(
            message = message,
            startPlayingAudio = startPlayingAudio,
            stopPlayingAudio = stopPlayingAudio
        )
    }
}



@Preview(showBackground = true)
@Composable
fun AudioMessageComponentPreview() {
    AudioMessageComponent(
        message = AudioMessage(
            messageId = 1,
            senderId = 1,
            receiverId = 2,
            timestamp = System.currentTimeMillis(),
            messageState = MessageState.MESSAGE_SENT,
            fileName = "audio.mp3"
        ),
        currentAccountId = 1,
        startPlayingAudio = {},
        stopPlayingAudio = {}
    )
}
