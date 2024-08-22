package com.flydrop2p.flydrop2p.data.local

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import com.flydrop2p.flydrop2p.MainActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class FileManager(private val activity: MainActivity) {
    fun imagePathToByteArray(imagePath: String?): ByteArray? {
        if (imagePath == null) return null

        val uri = Uri.fromFile(File(imagePath))
        val contentResolver = activity.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)

        return inputStream?.use {
            val byteBuffer = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len: Int
            while (inputStream.read(buffer).also { len = it } != -1) {
                byteBuffer.write(buffer, 0, len)
            }
            byteBuffer.toByteArray()
        }
    }


    fun byteArrayToImagePath(image: ByteArray?, fileName: String): String? {
        if (image == null) return null

        val contentResolver = activity.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val uri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                outputStream.write(image)
            }
        }

        return uri?.toString()
    }
}