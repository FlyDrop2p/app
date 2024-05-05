package com.flydrop2p.flydrop2p.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDAO {
    @Query("SELECT * FROM ContactEntity")
    fun getContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM ContactEntity WHERE id = :contactId")
    fun getContactById(contactId: Int): Flow<ContactEntity>

    @Insert
    suspend fun insertContact(contact: ContactEntity)

    @Update
    suspend fun updateContact(contact: ContactEntity)

    @Query("DELETE FROM ContactEntity WHERE id = :contactId")
    suspend fun deleteContact(contactId: Int)

}