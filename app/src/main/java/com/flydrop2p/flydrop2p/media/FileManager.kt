package com.flydrop2p.flydrop2p.media

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class FileManager(private val context: Context) {
    @OptIn(ExperimentalEncodingApi::class)
    fun getFileBase64(fileName: String): String? {
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
    fun saveProfileImage(imageBase64: String, accountId: Long): String? {
        val tempFile = File.createTempFile("img", null, context.cacheDir).apply {
            deleteOnExit()
        }

        return try {
            FileOutputStream(tempFile).use { outputStream ->
                outputStream.write(Base64.decode(imageBase64))
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
        val file = File(context.filesDir, "profile_image_${accountId}_${System.currentTimeMillis()}.jpg")

        return try {
            FileOutputStream(file).use { outputStream ->
                inputStream?.copyTo(outputStream)
            }

            file.name
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }

    fun saveFile(fileName: String, fileUri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(fileUri)
        val file = File(context.filesDir, fileName)

        return try {
            FileOutputStream(file).use { outputStream ->
                inputStream?.copyTo(outputStream)
            }

            file.name
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }

    fun saveFile(fileUri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver

        val fileName: String? = contentResolver.query(fileUri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        }

        return fileName?.let { saveFile(it, fileUri) }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun saveFile(fileName: String, fileBase64: String): String? {
        val tempFile = File.createTempFile("file", null, context.cacheDir).apply {
            deleteOnExit()
        }

        return try {
            FileOutputStream(tempFile).use { outputStream ->
                outputStream.write(Base64.decode(fileBase64))
            }

            saveFile(fileName, Uri.fromFile(tempFile))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            tempFile.delete()
        }
    }

}