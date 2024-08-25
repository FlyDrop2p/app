package com.flydrop2p.flydrop2p.data.local

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

class FileManager(private val context: Context) {
    fun loadFile(fileName: String): ByteArray? {
        val file = File(context.filesDir, fileName)

        return try {
            FileInputStream(file).use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveProfileImage(image: ByteArray, accountId: Int): String? {
        val tempFile = File.createTempFile("temp_profile_image_${accountId}", null, context.cacheDir).apply {
            deleteOnExit()
        }

        return try {
            FileOutputStream(tempFile).use { outputStream ->
                outputStream.write(image)
            }

            saveProfileImage(Uri.fromFile(tempFile), accountId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            tempFile.delete()
        }
    }

    fun saveProfileImage(imageUri: Uri, accountId: Int): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
        val file = File(context.filesDir, "profile_image_${accountId}")

        return try {
            FileOutputStream(file).use { outputStream ->
                inputStream?.copyTo(outputStream)
            }

            "profile_image_${accountId}"
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }
}