package com.flydrop2p.flydrop2p.ui.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SentFileMessageComponent(
    message: FileMessage,
    visualized: Boolean
) {
    val context = LocalContext.current
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(Date(message.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.widthIn(min = 150.dp, max = 300.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .clickable {
                        val file = File(message.file.path)
                        val uri = Uri.fromFile(file)
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, "*/*")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(intent)
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "File Thumbnail",
                        modifier = Modifier
                            .size(45.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = message.file.name,
                            fontSize = 16.sp,
                            color = Color(0xFF075985),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = message.file.length().toString(),
                            fontSize = 14.sp,
                            color = Color(0xFF075985)
                        )
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
                    Image(
                        painter = painterResource(id = R.drawable.done_all_24px),
                        colorFilter = if (visualized) ColorFilter.tint(Color(0xFF0e9de9)) else ColorFilter.tint(Color(0xFFADADAD)),
                        contentDescription = "Visualizzato",
                        modifier = Modifier.size(20.dp)
                    )
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
                        val file = File(message.file.path)
                        val uri = Uri.fromFile(file)
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, "*/*")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(intent)
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "File Thumbnail",
                        modifier = Modifier
                            .size(45.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = message.file.name,
                            fontSize = 16.sp,
                            color = Color(0xFF075985),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = message.file.length().toString(),
                            fontSize = 14.sp,
                            color = Color(0xFF075985)
                        )
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
    visualized: Boolean,
    currentAccountId: Int
) {
    if (message.senderId == currentAccountId) {
        SentFileMessageComponent(
            message = message,
            visualized = visualized
        )
    } else {
        ReceivedFileMessageComponent(
            message = message,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FileMessageComponentPreview() {
    FileMessageComponent(
        message = FileMessage(
            senderId = 0,
            receiverId = 1,
            file = File(""),
            timestamp = System.currentTimeMillis() / 1000
        ),
        visualized = true,
        currentAccountId = 0
    )
}
