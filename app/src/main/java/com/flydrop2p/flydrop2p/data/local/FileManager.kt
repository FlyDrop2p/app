package com.flydrop2p.flydrop2p.data.local

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
    fun saveProfileImage(imageBase64: String, accountId: Long): String? {
        val tempFile = File.createTempFile("temp_profile_image_${accountId}_${System.currentTimeMillis()}", null, context.cacheDir).apply {
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
        val file = File(context.filesDir, "profile_image_${accountId}_${System.currentTimeMillis()}")

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

    @OptIn(ExperimentalEncodingApi::class)
    fun saveFile(fileBase64: String, accountId: Long): String? {
        val tempFile = File.createTempFile("temp_file_${accountId}_${System.currentTimeMillis()}", null, context.cacheDir).apply {
            deleteOnExit()
        }

        return try {
            FileOutputStream(tempFile).use { outputStream ->
                outputStream.write(Base64.decode(fileBase64))
            }

            saveFile(Uri.fromFile(tempFile), accountId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            tempFile.delete()
        }
    }

    fun saveFile(fileUri: Uri, accountId: Long): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(fileUri)
        val file = File(context.filesDir, "file_${accountId}_${System.currentTimeMillis()}")

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

    fun saveCustomFile(fileUri: Uri, accountId: Long): String? {
        val contentResolver: ContentResolver = context.contentResolver

        val originalFileName: String? = contentResolver.query(fileUri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        }
        val fileExtension = originalFileName?.substringAfterLast(".", "")

        val newFileName = "file_${accountId}_${System.currentTimeMillis()}${if (fileExtension.isNullOrEmpty()) "" else ".$fileExtension"}"
        val file = File(context.filesDir, newFileName)
        val inputStream: InputStream? = contentResolver.openInputStream(fileUri)

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
}