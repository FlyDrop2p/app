package com.flydrop2p.flydrop2p.data.local.contact

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDAO {
    @Query("SELECT * FROM ContactEntity")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM ContactEntity WHERE accountId = :contactId")
    suspend fun getContactById(contactId: Int): ContactEntity?

    @Insert
    suspend fun insertContact(contact: ContactEntity)

    @Update
    suspend fun updateContact(contact: ContactEntity)

    @Delete
    suspend fun deleteContact(contactEntity: ContactEntity)

}