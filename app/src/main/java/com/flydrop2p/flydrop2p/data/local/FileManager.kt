package com.flydrop2p.flydrop2p.data.local

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class FileManager(private val context: Context) {
    @OptIn(ExperimentalEncodingApi::class)
    fun loadFile(fileName: String): String? {
        val file = File(context.filesDir, fileName)

        return try {
            val byteArray = FileInputStream(file).use { inputStream ->
                inputStream.readBytes()
            }

            Base64.encode(byteArray)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun saveProfileImage(image: String, accountId: Long): String? {
        val tempFile = File.createTempFile("temp_profile_image_${accountId}_${System.currentTimeMillis()}", null, context.cacheDir).apply {
            deleteOnExit()
        }

        return try {
            FileOutputStream(tempFile).use { outputStream ->
                outputStream.write(Base64.decode(image))
            }

            saveProfileImage(Uri.fromFile(tempFile), accountId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            tempFile.delete()
        }
    }

    fun saveProfileImage(imageUri: Uri, accountId: Long): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
        val file = File(context.filesDir, "profile_image_${accountId}_${System.currentTimeMillis()}")

        return try {
            FileOutputStream(file).use { outputStream ->
                inputStream?.copyTo(outputStream)
            }

            "profile_image_${accountId}_${System.currentTimeMillis()}"
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }
}