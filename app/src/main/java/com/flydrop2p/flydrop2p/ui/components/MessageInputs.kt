package com.flydrop2p.flydrop2p.ui.components

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import coil.compose.rememberImagePainter
import com.flydrop2p.flydrop2p.R
import java.io.File
import java.io.FileOutputStream

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
        placeholder = { Text("Scrivi un messaggio...") },
        // modifier = Modifier.weight(1f)
    )

    if (isTyping) {
        IconButton(
            onClick = {
                onSendTextMessage(textFieldValue.text)
                onValueChange(TextFieldValue())
            },
            modifier = Modifier.padding(start = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
                tint = Color.Black
            )
        }
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
    if (isRecording) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
            ) {}
            Spacer(modifier = Modifier.size(16.dp))
            Text("Recording...", color = Color.Red)
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

            IconButton(
                onClick = {
                    onSendAudioMessage()
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send audio",
                    tint = Color.Black
                )
            }
        }

    } else {
        IconButton(
            onClick = { onStartRecording() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.mic_24px),
                contentDescription = "Start recording",
                tint = Color.Black
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
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Attach file",
                tint = Color.Black
            )
        }
    } else {
        val context = LocalContext.current
        val mimeType = context.contentResolver.getType(fileUri) ?: "application/octet-stream"
        Log.d("FilePreview", "MIME type: $mimeType")

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
                mimeType.startsWith("application/pdf") -> PdfPreview(context, fileUri)
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
}

@Composable
fun getPreviewPainter(fileUri: Uri): Painter {
    return rememberImagePainter(
        data = fileUri,
        builder = {
            crossfade(true)
            error(R.drawable.error_24px)
        }
    )
}

@Composable
fun ImagePreview(fileUri: Uri) {
    val painter = rememberImagePainter(
        data = fileUri,
        builder = {
            crossfade(true)
            error(R.drawable.error_24px)
        }
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
fun PdfPreview(context: Context, fileUri: Uri) {
    val tempFile = getFileFromContentUri(context, fileUri)
    val fileUriNewUri = Uri.fromFile(tempFile)

    PdfFirstPageViewer(
        uri = fileUriNewUri,
        imageWidth = 50.dp,
        imageHeight = 50.dp
    )
    Spacer(modifier = Modifier.size(16.dp))
    Text(
        text = fileUri.lastPathSegment ?: "Unknown File",
        color = Color.Black,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
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
