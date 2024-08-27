package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Base URL del backend
private const val BASE_URL = "https://flydrop.riccardobenevelli.com/api/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
    .build()

@Serializable
data class BackupRequestBody(
    val userId: Long,
    val messages: List<MessageEntity>
)

data class BackupResponse(
    val message: String
)


interface BackupApiService {

    @POST("backup")
    suspend fun backupMessages(@Body request: BackupRequestBody): BackupResponse

    @GET("backup/{userId}")
    suspend fun getBackup(@Path("userId") userId: Long): List<MessageEntity>
}


object BackupInstance {
    val api: BackupApiService by lazy {
        retrofit.create(BackupApiService::class.java)
    }
}
