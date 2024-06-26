package com.flydrop2p.flydrop2p.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MessageEntity::class, ChatInfoEntity::class, ContactEntity::class], version = 1, exportSchema = false)
abstract class FlyDropDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDAO

    abstract fun chatInfoDao(): ChatInfoDAO

    abstract fun contactDao(): ContactDAO

    companion object {
        @Volatile
        private var Instance: FlyDropDatabase? = null

        fun getDatabase(context: Context): FlyDropDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlyDropDatabase::class.java,
                    "flydrop_database"
                ).build()
                Instance = instance
                instance
            }
        }
    }

}