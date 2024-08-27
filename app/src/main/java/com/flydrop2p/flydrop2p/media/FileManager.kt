package com.flydrop2p.flydrop2p.media

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.flydrop2p.flydrop2p.network.model.call.NetworkCall
import com.flydrop2p.flydrop2p.network.model.device.NetworkProfile
import com.flydrop2p.flydrop2p.network.model.message.NetworkAudioMessage
import com.flydrop2p.flydrop2p.network.model.message.NetworkFileMessage
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

    private fun saveFile(inputFileUri: Uri, outputFileName: String): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(inputFileUri)
        val file = File(context.filesDir, outputFileName)

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
    private fun saveFile(inputFileBase64: String, outputFileName: String): String? {
        val tempFile = File.createTempFile("file", null, context.cacheDir).apply {
            deleteOnExit()
        }

        return try {
            FileOutputStream(tempFile).use { outputStream ->
                outputStream.write(Base64.decode(inputFileBase64))
            }

            saveFile(Uri.fromFile(tempFile), outputFileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            tempFile.delete()
        }
    }

    fun saveProfileImage(imageUri: Uri, accountId: Long): String? {
        return saveFile(imageUri, "profile_image_${accountId}.jpg")
    }

    fun saveNetworkProfileImage(networkProfile: NetworkProfile): String? {
        networkProfile.apply {
            return imageBase64?.let { saveFile(it, "profile_image_${accountId}.jpg") }
        }
    }

    fun saveMessageFile(originalFileUri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver

        val fileName: String? = contentResolver.query(originalFileUri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        }

        return fileName?.let { saveFile(originalFileUri, it) }
    }

    fun saveNetworkFile(networkFileMessage: NetworkFileMessage): String? {
        networkFileMessage.apply {
            return saveFile(networkFileMessage.fileBase64, networkFileMessage.fileName)
        }
    }

    fun saveMessageAudio(tempFileUri: Uri, senderId: Long, timestamp: Long): String? {
        val fileName = saveFile(tempFileUri, "audio_${senderId}_${timestamp}.3gp")
        tempFileUri.path?.let { File(it).delete() }
        return fileName
    }

    fun saveNetworkAudio(networkAudioMessage: NetworkAudioMessage): String? {
        networkAudioMessage.apply {
            return saveFile(audioBase64, "audio_${senderId}_${timestamp}.3gp")
        }
    }

    fun saveNetworkCall(networkCall: NetworkCall): String? {
        networkCall.apply {
            return saveFile(audioBase64, "call_audio_${System.currentTimeMillis()}.3gp")
        }
    }
}