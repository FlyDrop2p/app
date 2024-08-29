package com.flydrop2p.flydrop2p.ui.components

import MessageStatusIndicator
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import com.flydrop2p.flydrop2p.domain.model.message.MessageState
import java.io.File


@Composable
fun ImageMessageComponent(
    message: FileMessage,
    currentAccountId: Long
) {
    val context = LocalContext.current

    val fileUri = Uri.fromFile(File(context.filesDir, message.fileName))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp),
        horizontalArrangement = if (message.senderId == currentAccountId) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(min = 150.dp, max = 300.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                .clickable {
                    shareFile(context, message.fileName)
                }
        ) {
            Image(
                painter = getPreviewPainter(fileUri),
                contentDescription = "Media Preview",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            MessageStatusIndicator(
                message = message,
                currentAccountId = currentAccountId,
                backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align( Alignment.BottomEnd )
            )
        }
    }
}

@Composable
fun VideoMessageComponent(
    message: FileMessage,
    currentAccountId: Long,
    modifier: Modifier = Modifier,
    thumbnailHeight: Dp = 170.dp
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .build()
    }

    val videoUri = Uri.fromFile(File(context.filesDir, message.fileName))

    val imageRequest = remember {
        ImageRequest.Builder(context)
            .data(videoUri)
            .videoFrameMillis(1000)
            .build()
    }

    val videoDuration = remember { getVideoDuration(context, videoUri) }

    val playIcon: Painter = painterResource(id = R.drawable.play_arrow_24px)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp),
        horizontalArrangement = if (message.senderId == currentAccountId) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                // .widthIn(min = 150.dp, max = 300.dp)
                .width(300.dp)
                .height(thumbnailHeight)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
//                .border(
//                    width = 2.dp,
//                    color = MaterialTheme.colorScheme.primary,
//                    shape = RoundedCornerShape(corner = CornerSize(16.dp))
//                )
                .clickable {
                    shareFile(context, message.fileName)
                }
        ) {
            AsyncImage(
                model = imageRequest,
                contentDescription = "Video Thumbnail",
                modifier = Modifier
                    .fillMaxSize()
                    .matchParentSize(),
                contentScale = ContentScale.Crop,
                imageLoader = imageLoader,
            )

            Icon(
                painter = playIcon,
                contentDescription = "Play Icon",
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center),
                tint = Color.White,
            )

            if (videoDuration.isNotEmpty()) {
                Text(
                    text = videoDuration,
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(Color(0x8A000000), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp)
                )
            }

            MessageStatusIndicator(
                message = message,
                currentAccountId = currentAccountId,
                backgroundColor = Color(0xFFEFEFEF),
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun PdfMessageComponent(
    message: FileMessage,
    currentAccountId: Long
) {
    val context = LocalContext.current

    val fileUri = Uri.fromFile(File(context.filesDir, message.fileName))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp),
        horizontalArrangement = if (message.senderId == currentAccountId) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (message.senderId == currentAccountId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .widthIn(min = 150.dp, max = 300.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                .clickable {
                    shareFile(context, message.fileName)
                }
        ) {
            Column (
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(top = 2.dp, start = 2.dp, end = 2.dp)
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 6.dp, start = 6.dp, end = 6.dp)
                ){
                    PdfPreview(fileUri, mine = message.senderId == currentAccountId)
                }

                MessageStatusIndicator(
                    message = message,
                    currentAccountId = currentAccountId,
                    backgroundColor = Color(0x00EFEFEF),
                )
            }
        }
    }
}

@Composable
fun GenericFileMessageComponent(
    message: FileMessage,
    currentAccountId: Long
){
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp),
        horizontalArrangement = if (message.senderId == currentAccountId) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (message.senderId == currentAccountId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .widthIn(min = 150.dp, max = 300.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                .clickable {
                    shareFile(context, message.fileName)
                }
        ) {
            Column (
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(top = 2.dp, start = 2.dp, end = 2.dp)
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 6.dp, start = 6.dp, end = 6.dp)
                ){
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
                            color = if (message.senderId == currentAccountId) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                MessageStatusIndicator(
                    message = message,
                    currentAccountId = currentAccountId,
                    backgroundColor = Color(0x00EFEFEF),
                )
            }
        }
    }
}

@Composable
fun FileMessageComponent(
    message: FileMessage,
    currentAccountId: Long
) {
    val mimeType = getMimeType(message.fileName.substringAfterLast(".", ""))

    if (mimeType.startsWith("image/")) {
        ImageMessageComponent(
            message = message,
            currentAccountId = currentAccountId
        )
    } else if (mimeType.startsWith("video/")) {
        VideoMessageComponent(
            message = message,
            currentAccountId = currentAccountId
        )
    } else if (mimeType.startsWith("application/pdf")) {
        PdfMessageComponent(
            message = message,
            currentAccountId = currentAccountId
        )
    } else {
        GenericFileMessageComponent(
            message = message,
            currentAccountId = currentAccountId
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

    val fileUri: Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

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