package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://flydrop.riccardobenevelli.com/api/"


@Serializable
data class BackupRequestBody(
    val userId: Long,
    val messages: List<MessageEntity>
)

data class BackupResponse(
    val message: String
)

interface BackupApiService {
    @GET("register")
    suspend fun register(): Long

    @POST("backup")
    suspend fun backupMessages(@Body request: BackupRequestBody): BackupResponse

    @GET("backup/{userId}")
    suspend fun retrieveMessages(@Path("userId") userId: Long): List<MessageEntity>
}

object BackupApi {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: BackupApiService by lazy {
        retrofit.create(BackupApiService::class.java)
    }
}