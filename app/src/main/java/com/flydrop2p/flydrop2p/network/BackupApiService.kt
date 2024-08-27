package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Base URL del backend
private const val BASE_URL = "https://flydrop.riccardobenevelli.com/api/"


private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

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
