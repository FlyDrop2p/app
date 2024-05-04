package com.example.flydrop2p.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MessageEntity::class, ChatInfoEntity::class, ContactEntity::class], version = 1, exportSchema = false)
abstract class FlydropDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDAO

    abstract fun chatInfoDao(): ChatInfoDAO

    abstract fun contactDao(): ContactDAO

    companion object {
        @Volatile
        private var Instance: FlydropDatabase? = null

        fun getDatabase(context: Context): FlydropDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlydropDatabase::class.java,
                    "flydrop_database"
                ).build()
                Instance = instance
                instance
            }
        }
    }

}