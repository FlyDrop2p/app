package com.flydrop2p.flydrop2p.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flydrop2p.flydrop2p.R


@Composable
fun GroupMessage() {
    Row(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Image(
            painter = painterResource(id = R.drawable.account_circle_24px),
            colorFilter = ColorFilter.tint(Color.Black),
            contentDescription = "Immagine del mittente",
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        PrivateMessage("Alice", "Testo del messaggio un po' corto", "15:30", true)
    }
}

@Composable
fun PrivateMessage(name: String, message: String, time: String, visualized: Boolean) {
    Surface(
        shape = RoundedCornerShape(16.dp), color = Color.LightGray,
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier.padding(
                start = 14.dp, top = 8.dp, bottom = 8.dp, end = 14.dp
            )
        ) {
            Text(
                text = name, fontSize = 16.sp, color = Color.Gray
            )
            Text(
                text = message,
                fontSize = 18.sp,
                color = Color.Black
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = time, // Orario del messaggio
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.done_all_24px),
                    colorFilter = if (visualized) ColorFilter.tint(Color(0xFF037971)) else ColorFilter.tint(Color(0xFFADADAD)),
                    contentDescription = "Visualizzato",
                    modifier = Modifier.size(20.dp) // Dimensioni dell'icona
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MessagePreview() {
    GroupMessage()
    // PrivateMessage()
}