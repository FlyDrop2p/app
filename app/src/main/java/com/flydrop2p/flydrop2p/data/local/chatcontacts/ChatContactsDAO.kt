package com.flydrop2p.flydrop2p.data.local.chatcontacts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ChatContactsDAO {
    @Query("SELECT contactId FROM ChatContactsEntity WHERE chatId = :chatId")
    suspend fun getAllContactIdsByChatId(chatId: Int): List<Int>

    @Query("SELECT chatId FROM ChatContactsEntity WHERE contactId = :contactId")
    suspend fun getAllChatIdsByContactId(contactId: Int): List<Int>

    @Insert
    suspend fun insertChatContacts(chatContactsEntity: ChatContactsEntity): Long

    @Update
    suspend fun updateChatContacts(chatContactsEntity: ChatContactsEntity)

    @Delete
    suspend fun deleteChatContacts(chatContactsEntity: ChatContactsEntity)
}