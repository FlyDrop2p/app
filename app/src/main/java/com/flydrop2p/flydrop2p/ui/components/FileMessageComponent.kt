package com.flydrop2p.flydrop2p.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import com.flydrop2p.flydrop2p.domain.model.message.MessageState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SentFileMessageComponent(
    message: FileMessage
) {
    val context = LocalContext.current
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(Date(message.timestamp))

    val fileUri = Uri.fromFile(File(context.filesDir, message.fileName))
    val mimeType = getMimeType(fileUri, context)
    val isImageOrVideo = mimeType.startsWith("image/") || mimeType.startsWith("video/")

    Log.d(
        "SentFileMessageComponent",
        "MIME type: $mimeType, isImageOrVideo: $isImageOrVideo, fileUri: $fileUri"
    )

    @Composable
    fun getPreviewPainter(): Painter {
        return rememberImagePainter(
            data = fileUri,
            builder = {
                crossfade(true)
                error(R.drawable.error_24px)
            }
        )
    }

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
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(fileUri, mimeType)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(intent)
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                if (isImageOrVideo) {
                    Image(
                        painter = getPreviewPainter(),
                        contentDescription = "Media Preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.description_24px),
                            contentDescription = "File Icon",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = message.fileName,
                                fontSize = 16.sp,
                                color = Color(0xFF075985),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timeString,
                        fontSize = 12.sp,
                        color = Color(0xFF083249)
                    )

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

@Composable
fun ReceivedFileMessageComponent(
    message: FileMessage,
) {
    val context = LocalContext.current
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(Date(message.timestamp))

    val fileUri = Uri.fromFile(File(context.filesDir, message.fileName))
    val mimeType = context.contentResolver.getType(fileUri)
    val isImageOrVideo =
        mimeType?.startsWith("image/") == true || mimeType?.startsWith("video/") == true

    Log.d(
        "ReceivedFileMessageComponent",
        "MIME type: $mimeType, isImageOrVideo: $isImageOrVideo, fileUri: $fileUri"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.widthIn(min = 150.dp, max = 300.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(fileUri, "*/*")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(intent)
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isImageOrVideo) {
                        Image(
                            painter = rememberImagePainter(data = fileUri),
                            contentDescription = "Media Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp) // Adjust the height as needed
                                .clip(RoundedCornerShape(16.dp))
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.description_24px),
                            contentDescription = "File Icon",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = message.fileName,
                                fontSize = 16.sp,
                                color = Color(0xFF075985),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
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

@Composable
fun FileMessageComponent(
    message: FileMessage,
    currentAccountId: Long
) {
    if (message.senderId == currentAccountId) {
        SentFileMessageComponent(
            message = message,
        )
    } else {
        ReceivedFileMessageComponent(
            message = message,
        )
    }
}

@Composable
fun FilePreview(fileUri: Uri, onSendFile: (Uri) -> Unit, onDeleteFile: (Uri) -> Unit) {
    val context = LocalContext.current
    val mimeType = context.contentResolver.getType(fileUri) ?: "application/octet-stream"
    Log.d("FilePreview", "MIME type: $mimeType")

    @Composable
    fun getPreviewPainter(): Painter {
        return rememberImagePainter(
            data = fileUri,
            builder = {
                crossfade(true)
                error(R.drawable.error_24px)
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (mimeType.startsWith("image/")) {
            Image(
                painter = getPreviewPainter(),
                contentDescription = "Image Preview",
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 16.dp)
            )
        } else {
            Image(
                painter = getPreviewPainter(),
                contentDescription = "File Preview",
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 16.dp)
            )
            Text(
                text = fileUri.lastPathSegment ?: "Unknown File",
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onDeleteFile(fileUri)
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }

            IconButton(
                onClick = {
                    onSendFile(fileUri)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileMessageComponentPreview() {
    FileMessageComponent(
        message = FileMessage(
            messageId = 0,
            senderId = 0,
            receiverId = 1,
            timestamp = System.currentTimeMillis(),
            messageState = MessageState.MESSAGE_RECEIVED,
            fileName = "example.jpg"
        ),
        currentAccountId = 0
    )
}

fun getMimeType(fileUri: Uri, context: Context): String {
    val mimeType = context.contentResolver.getType(fileUri)
    if (mimeType != null) return mimeType

    val fileExtension = fileUri.lastPathSegment?.substringAfterLast(".", "")
    return when (fileExtension?.toLowerCase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "mp4" -> "video/mp4"
        "avi" -> "video/x-msvideo"
        "pdf" -> "application/pdf"
        else -> "application/octet-stream" // Tipo generico
    }
}
