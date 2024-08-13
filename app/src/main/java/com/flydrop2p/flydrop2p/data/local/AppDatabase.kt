package com.flydrop2p.flydrop2p.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.flydrop2p.flydrop2p.data.local.chatcontacts.ChatContactsDAO
import com.flydrop2p.flydrop2p.data.local.chatcontacts.ChatContactsEntity
import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatInfoDAO
import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatInfoEntity
import com.flydrop2p.flydrop2p.data.local.contact.ContactDAO
import com.flydrop2p.flydrop2p.data.local.contact.ContactEntity
import com.flydrop2p.flydrop2p.data.local.message.MessageDAO
import com.flydrop2p.flydrop2p.data.local.message.MessageEntity

@Database(entities = [ChatContactsEntity::class, ChatInfoEntity::class, ContactEntity::class, MessageEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chatContactsDao(): ChatContactsDAO

    abstract fun chatInfoDao(): ChatInfoDAO

    abstract fun contactDao(): ContactDAO

    abstract fun messageDao(): MessageDAO

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flydrop_database"
                ).build()
                Instance = instance
                instance
            }
        }
    }

}