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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.flydrop2p.flydrop2p.ui.components.PdfFirstPageViewer
import com.flydrop2p.flydrop2p.ui.components.getMimeType
import com.flydrop2p.flydrop2p.ui.components.getPreviewPainter
import com.flydrop2p.flydrop2p.ui.components.getVideoDuration
import com.flydrop2p.flydrop2p.ui.components.shareFile
import com.flydrop2p.flydrop2p.ui.navigation.NavigationDestination
import com.flydrop2p.flydrop2p.ui.screen.call.CallDestination
import com.flydrop2p.flydrop2p.ui.screen.call.CallState
import java.io.File

object InfoDestination : NavigationDestination {
    override val route = "info"
    override val titleRes = R.string.info_screen
    const val accountIdArg = "accountId"
    val routeWithArgs = "$route/{$accountIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    infoViewModel: InfoViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val callRequest by infoViewModel.networkManager.callRequest.collectAsState()

    LaunchedEffect(callRequest) {
        callRequest?.let {
            navController.navigate("${CallDestination.route}/${it.senderId}/${CallState.RECEIVED_CALL_REQUEST.name}")
        }
    }

    val infoState by infoViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = modifier
                            .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Go back",
                            modifier = Modifier
                                .size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

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
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    contentDescription = "Immagine di default",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = infoState.profile.username,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.05f))

            Text(
                text = "Media",
                fontSize = 18.sp,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    shareFile(context, mediaFile.fileName)
                                },
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
                                .height(128.dp)
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
                            modifier = Modifier
                                .padding(top = 6.dp, start = 6.dp, end = 6.dp)
                                .clickable {
                                    shareFile(context, mediaFile.fileName)
                                },
                        ) {
                            PdfFirstPageViewer(
                                uri = fileUri,
                                imageHeight = 128.dp,
                                imageWidth = 170.dp
                            )
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
}

@Preview(showBackground = true)
@Composable
fun InfoScreenPreview() {
}
