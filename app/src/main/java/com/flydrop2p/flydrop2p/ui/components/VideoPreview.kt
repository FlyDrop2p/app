package com.flydrop2p.flydrop2p.ui.components

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.flydrop2p.flydrop2p.R

@Composable
fun VideoThumbnail(
    videoUri: Uri,
    modifier: Modifier = Modifier,
    thumbnailHeight: Dp = 200.dp
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .build()
    }

    val imageRequest = remember {
        ImageRequest.Builder(context)
            .data(videoUri)
            .videoFrameMillis(1000)
            .build()
    }

    val videoDuration = remember { getVideoDuration(context, videoUri) }

    val playIcon: Painter = painterResource(id = R.drawable.play_arrow_24px)

    Box(
        modifier = modifier
            .height(thumbnailHeight)
            .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
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
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                    .padding(4.dp)
            )
        }
    }
}

fun getVideoDuration(context: Context, uri: Uri): String {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, uri)
    val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
    retriever.release()

    val durationInMillis = durationStr?.toLongOrNull() ?: 0L
    val seconds = (durationInMillis / 1000) % 60
    val minutes = (durationInMillis / (1000 * 60)) % 60
    val hours = (durationInMillis / (1000 * 60 * 60)) % 24

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}
