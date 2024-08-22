package com.flydrop2p.flydrop2p.data.local.account

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDAO {
    @Query("SELECT * FROM AccountEntity")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM AccountEntity WHERE accountId = :accountId")
    fun getAccountByAccountId(accountId: Int): Flow<AccountEntity?>

    @Insert
    suspend fun insertAccount(account: AccountEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAccount(account: AccountEntity)

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Delete
    suspend fun deleteAccount(account: AccountEntity)

}