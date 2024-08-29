package com.flydrop2p.flydrop2p.ui.components

import android.content.Context
import android.net.Uri
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.flydrop2p.flydrop2p.R
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextMessageInput(
    isTyping: Boolean,
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSendTextMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isTyping) {
        Spacer(modifier = Modifier.size(2.dp))
    }

    TextField(
        value = textFieldValue,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                "Scrivi un messaggio...",
                fontSize = 14.sp,
            )
        },
        textStyle = TextStyle(
            fontSize = 14.sp
        ),
        shape = RoundedCornerShape(70.dp),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Black,
            disabledLabelColor = Color.Transparent,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
    )

//    BasicTextField(
//        value = textFieldValue,
//        onValueChange = onValueChange,
//        textStyle = TextStyle(
//            fontSize = 14.sp
//        ),
//        decorationBox = { innerTextField ->
//            Column(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(8.dp))
//                    .background(Color.White)
//                    .padding(horizontal = 8.dp, vertical = 10.dp),
//                verticalArrangement = Arrangement.Center
//            ) {
//                if (textFieldValue.text.isEmpty()) {
//                    Text(
//                        text = "Scrivi un messaggio...",
//                        style = TextStyle(
//                            fontSize = 14.sp,
//                            lineHeight = 14.sp,
//                            color = Color.Gray
//                        )
//                    )
//                } else {
//                    innerTextField()
//                }
//            }
//        }
//    )

    if (isTyping) {
        SendButton(onClick = {
            onSendTextMessage(textFieldValue.text)
            onValueChange(TextFieldValue())
        })
    }
}

@Composable
fun AudioRecordingControls(
    isRecording: Boolean,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onCancelRecording: () -> Unit,
    onSendAudioMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    var recordingTime by remember { mutableStateOf(0L) }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    if (isRecording) {
        LaunchedEffect(Unit) {
            while (isRecording) {
                delay(1000L)
                recordingTime += 1000L
            }
        }
    } else {
        recordingTime = 0L
    }

    val formattedTime = remember(recordingTime) {
        val minutes = (recordingTime / 60000).toString().padStart(2, '0')
        val seconds = ((recordingTime / 1000) % 60).toString().padStart(2, '0')
        "$minutes:$seconds"
    }

    if (isRecording) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.Red.copy(alpha = alpha))
            ) {}
            Spacer(modifier = Modifier.size(16.dp))
            Text(formattedTime, color = Color.Red)
            Spacer(modifier = Modifier.size(2.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onCancelRecording()

                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Cancel recording",
                    tint = Color.Red
                )
            }

            SendButton(
                onClick = {
                    onSendAudioMessage()
                }
            )
        }

    } else {
        IconButton(
            onClick = { onStartRecording() },
            modifier = modifier
                .size(48.dp)
//                .background(
//                    color = MaterialTheme.colorScheme.primary,
//                    shape = CircleShape
//                )
                .padding(2.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.mic_24px),
                contentDescription = "Start recording",
                modifier = Modifier
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}


@Composable
fun FileMessageInput(
    fileUri: Uri?,
    onClick: () -> Unit,
    onSendFile: (Uri) -> Unit,
    onDeleteFile: (Uri) -> Unit
) {
    if (fileUri == null) {
        IconButton(
            onClick = { onClick() },
            modifier = Modifier
//                .size(48.dp)
//                .background(
//                    color = MaterialTheme.colorScheme.primary,
//                    shape = CircleShape
//                )
                .padding(2.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add),
                contentDescription = "Attach file",
                modifier = Modifier
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    } else {
        val context = LocalContext.current
        val mimeType = context.contentResolver.getType(fileUri) ?: "application/octet-stream"

        val fileName =
            context.contentResolver.query(fileUri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(nameIndex)
            } ?: "Unknown"

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            when {
                mimeType.startsWith("image/") -> ImagePreview(fileUri)
                mimeType.startsWith("video/") -> VideoPreview(fileUri)
                mimeType.startsWith("application/pdf") -> {
                    val tempFile = getFileFromContentUri(context, fileUri)
                    val fileUriNewUri = Uri.fromFile(tempFile)
                    PdfPreview(
                        fileUriNewUri,
                        filename = fileName,
                        modifier = Modifier.weight(1f)
                    )
                }

                else -> GenericFilePreview(fileUri)
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

                SendButton(onClick = { onSendFile(fileUri) })
            }
        }
    }
}

@Composable
fun getPreviewPainter(fileUri: Uri): Painter {
    return rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = fileUri).apply(block = fun ImageRequest.Builder.() {
            crossfade(true)
            error(R.drawable.error_24px)
        }).build()
    )
}

@Composable
fun ImagePreview(fileUri: Uri) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = fileUri).apply(block = fun ImageRequest.Builder.() {
            crossfade(true)
            error(R.drawable.error_24px)
        }).build()
    )
    Image(
        painter = painter,
        contentDescription = "Image Preview",
        modifier = Modifier
            .size(70.dp)
            .padding(end = 16.dp)
    )
}

@Composable
fun VideoPreview(videoUri: Uri) {
    VideoThumbnail(
        videoUri = videoUri,
        modifier = Modifier.size(70.dp),
        thumbnailHeight = 70.dp
    )
}

@Composable
fun GenericFilePreview(fileUri: Uri) {
    Image(
        painter = painterResource(id = R.drawable.description_24px),
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
    )
}

@Composable
fun PdfPreview(
    fileUri: Uri,
    modifier: Modifier = Modifier,
    mine: Boolean? = false,
    filename: String? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PdfFirstPageViewer(
            uri = fileUri,
            imageWidth = 50.dp,
            imageHeight = 50.dp
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = filename ?: fileUri.lastPathSegment ?: "Unknown File",
            color = if (mine == true) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

fun getFileFromContentUri(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File(context.cacheDir, "tempfile.pdf")
    inputStream?.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}


@Composable
fun SendButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier
            .size(48.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.arrow_up),
            contentDescription = "Send",
            modifier = Modifier
                .size(24.dp),
            tint = Color.White,
        )
    }
}
