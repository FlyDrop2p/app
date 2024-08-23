package com.flydrop2p.flydrop2p.data.local.profile

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDAO {
    @Query("SELECT * FROM ProfileEntity")
    fun getAllProfilesAsFlow(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM ProfileEntity WHERE accountId = :accountId")
    fun getProfileByAccountIdAsFlow(accountId: Int): Flow<ProfileEntity?>

    @Query("SELECT * FROM ProfileEntity")
    fun getAllProfiles(): List<ProfileEntity>

    @Query("SELECT * FROM ProfileEntity WHERE accountId = :accountId")
    fun getProfileByAccountId(accountId: Int): ProfileEntity?

    @Insert
    suspend fun insertProfile(profile: ProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: ProfileEntity)

    @Update
    suspend fun updateProfile(profile: ProfileEntity)

    @Delete
    suspend fun deleteProfile(profile: ProfileEntity)
}