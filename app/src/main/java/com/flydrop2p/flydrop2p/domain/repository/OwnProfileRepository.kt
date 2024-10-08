package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.device.Profile
import kotlinx.coroutines.flow.Flow

interface OwnProfileRepository {
    fun getProfileAsFlow(): Flow<Profile>
    suspend fun getProfile(): Profile

    suspend fun setAccountId(accountId: Long)

    suspend fun setUpdateTimestamp(updateTimestamp: Long)

    suspend fun setUsername(username: String)

    suspend fun setImageFileName(imageFileName: String)
}