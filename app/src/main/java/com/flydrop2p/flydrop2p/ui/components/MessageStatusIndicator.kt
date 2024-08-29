
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.message.Message
import com.flydrop2p.flydrop2p.domain.model.message.MessageState
import com.flydrop2p.flydrop2p.domain.model.message.TextMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageStatusIndicator(
    message: Message,
    currentAccountId: Long,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFEFEFEF),
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(Date(message.timestamp))

    Surface (
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)
    ){
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = backgroundColor)
                .padding(horizontal = 6.dp, vertical = 0.dp)
        ) {
            Text(
                text = timeString,
                fontSize = 10.sp,
                color = Color(0xFF083249)
            )

            if (message.senderId == currentAccountId) {
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

@Preview(showBackground = true)
@Composable
fun MessageStatusIndicatorPreview() {
    MessageStatusIndicator(
        message = TextMessage(
            messageId = 1,
            senderId = 1,
            receiverId = 2,
            timestamp = System.currentTimeMillis(),
            messageState = MessageState.MESSAGE_READ,
            text = "Ciao!"
        ),
        currentAccountId = 1
    )
}
