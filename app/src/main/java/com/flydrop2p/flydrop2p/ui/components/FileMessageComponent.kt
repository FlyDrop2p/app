package com.flydrop2p.flydrop2p.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
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

    // fileUri.lastPathSegment == message.fileName
    val mimeType = getMimeType(message.fileName.substringAfterLast(".", ""))

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
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                if (mimeType.startsWith("image/")) {
                    Image(
                        painter = getPreviewPainter(fileUri),
                        contentDescription = "Media Preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                } else if (mimeType.startsWith("video/")) {
                    VideoThumbnail(
                        videoUri = fileUri,
                        modifier = Modifier
                            .fillMaxWidth(),
                        thumbnailHeight = 150.dp
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (mimeType.startsWith("application/pdf")) {
                            PdfPreview(context, fileUri)
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
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
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

@Composable
fun ReceivedFileMessageComponent(
    message: FileMessage,
) {
    val context = LocalContext.current
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(Date(message.timestamp))

    // fileUri.lastPathSegment == message.fileName
    val fileUri = Uri.fromFile(File(context.filesDir, message.fileName))
    val mimeType = getMimeType(message.fileName.substringAfterLast(".", ""))
    val isImageOrVideo = mimeType.startsWith("image/") || mimeType.startsWith("video/")

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
                        shareFile(context, message.fileName)
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (mimeType.startsWith("image/")) {
                        Image(
                            painter = getPreviewPainter(fileUri),
                            contentDescription = "Media Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    } else if (mimeType.startsWith("video/")) {
                        VideoThumbnail(
                            videoUri = fileUri,
                            modifier = Modifier
                                .fillMaxWidth(),
                            thumbnailHeight = 150.dp
                        )
                    } else {
                        if (mimeType.startsWith("application/pdf")) {
                            PdfPreview(context, fileUri)
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


fun getMimeType(extension: String): String {
    return when (extension) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "mp4" -> "video/mp4"
        "avi" -> "video/x-msvideo"
        "pdf" -> "application/pdf"
        else -> "application/octet-stream" // Tipo generico
    }
}


fun shareFile(context: Context, fileName: String) {
    val file = File(context.filesDir, fileName)

    val fileUri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(fileUri, context.contentResolver.getType(fileUri) ?: "*/*")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(intent)
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