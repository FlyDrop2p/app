package com.flydrop2p.flydrop2p.ui.components

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun PdfFirstPageViewer(
    modifier: Modifier = Modifier,
    uri: Uri,
    imageWidth: Dp = 150.dp,
    imageHeight: Dp = 200.dp
) {
    val coroutineScope = rememberCoroutineScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val density = LocalDensity.current

    LaunchedEffect(uri) {
        coroutineScope.launch(Dispatchers.IO) {
            val input = ParcelFileDescriptor.open(uri.toFile(), ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(input)
            try {
                if (renderer.pageCount > 0) {
                    val page = renderer.openPage(0)

                    val widthPx = with(density) { imageWidth.toPx().toInt() }
                    val heightPx = with(density) { imageHeight.toPx().toInt() }

                    val destinationBitmap = Bitmap.createBitmap(
                        widthPx,
                        heightPx,
                        Bitmap.Config.ARGB_8888
                    )
                    page.render(destinationBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    bitmap = destinationBitmap
                    page.close()
                }
                renderer.close()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                input.close()
            }
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = "First Page of PDF",
            modifier = modifier
                .size(imageWidth, imageHeight)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = modifier
                .size(imageWidth, imageHeight)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
        )
    }
}

