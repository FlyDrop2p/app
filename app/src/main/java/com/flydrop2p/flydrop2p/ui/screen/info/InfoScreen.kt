package com.flydrop2p.flydrop2p.ui.screen.info

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.ui.components.PdfPreview
import com.flydrop2p.flydrop2p.ui.components.getMimeType
import com.flydrop2p.flydrop2p.ui.components.getPreviewPainter
import com.flydrop2p.flydrop2p.ui.components.getVideoDuration
import com.flydrop2p.flydrop2p.ui.components.shareFile
import com.flydrop2p.flydrop2p.ui.navigation.NavigationDestination
import java.io.File

object InfoDestination : NavigationDestination {
    override val route = "info"
    override val titleRes = R.string.info_screen
    const val itemIdArg = "chatId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun InfoScreen(
    infoViewModel: InfoViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val infoState by infoViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Sezione Immagine del profilo
        if (infoState.profile.imageFileName != null) {
            Image(
                painter = rememberAsyncImagePainter(model = infoState.profile.imageFileName?.let {
                    File(LocalContext.current.filesDir, it)
                }),
                contentDescription = "Immagine profilo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.account_circle_24px),
                colorFilter = ColorFilter.tint(Color.Black),
                contentDescription = "Immagine di default",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sezione Nome utente
        Text(
            text = infoState.profile.username ?: "Sconosciuto",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.2f))

        // Sezione File multimediali
        Text(
            text = "Media",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp)
        ) {
            items(infoState.mediaMessages.size) { index ->
                val mediaFile = infoState.mediaMessages[index]
                val mimeType = getMimeType(mediaFile.fileName.substringAfterLast(".", ""))
                val context = LocalContext.current

                val fileUri = Uri.fromFile(File(context.filesDir, mediaFile.fileName))

                if (mimeType.startsWith("image/")) {

                    Image(
                        painter = getPreviewPainter(fileUri),
                        contentDescription = "Image Preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(128.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                    )

                } else if (mimeType.startsWith("video/")) {
                    val imageLoader = remember {
                        ImageLoader.Builder(context)
                            .components {
                                add(VideoFrameDecoder.Factory())
                            }
                            .build()
                    }

                    val imageRequest = remember {
                        ImageRequest.Builder(context)
                            .data(fileUri)
                            .videoFrameMillis(1000)
                            .build()
                    }

                    val videoDuration = remember { getVideoDuration(context, fileUri) }

                    val playIcon: Painter = painterResource(id = R.drawable.play_arrow_24px)

                    Box(
                        modifier = Modifier
                            .width(300.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                            .clickable {
                                shareFile(context, mediaFile.fileName)
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
                                fontSize = 8.sp,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(8.dp)
                                    .background(Color(0x8A000000), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp)
                            )
                        }
                    }

                } else if (mimeType.startsWith("application/pdf")) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 6.dp, start = 6.dp, end = 6.dp)
                    ) {
                        PdfPreview(context, fileUri)
                    }

                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 6.dp, start = 6.dp, end = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.description_24px),
                            contentDescription = "File Icon",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun InfoScreenPreview() {
}
