package com.flydrop2p.flydrop2p.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import java.io.File

@Composable
fun FileMessage(
    fileName: String,
    fileSize: String,
    filePath: String,
    time: String,
    visualized: Boolean
) {
    val context = LocalContext.current

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFbae6fd),
        modifier = Modifier
            .widthIn(min = 150.dp, max = 300.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .clickable {
                    val file = File(filePath)
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
                        text = fileName,
                        fontSize = 16.sp,
                        color = Color(0xFF075985),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = fileSize,
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
                    text = time,
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


@Preview(showBackground = true)
@Composable
fun FileMessagePreview() {
    FileMessage(
        fileName = "Orale ASD.pdf",
        fileSize = "104.5 KB",
        filePath = "/path/to/file.pdf",
        time = "08:35",
        visualized = true
    )
}
