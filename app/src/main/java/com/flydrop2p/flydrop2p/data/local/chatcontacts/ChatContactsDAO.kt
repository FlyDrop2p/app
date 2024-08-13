package com.flydrop2p.flydrop2p.data.local.chatcontacts

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ChatContactsDAO {
    @Query("SELECT contactId FROM ChatContactsEntity WHERE chatId = :chatId")
    suspend fun getAllContactIdsByChatId(chatId: Int): List<Int>

    @Query("SELECT chatId FROM ChatContactsEntity WHERE contactId = :contactId")
    suspend fun getAllChatIdsByContactId(contactId: Int): List<Int>
}