package com.flydrop2p.flydrop2p.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flydrop2p.flydrop2p.R

@Entity
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val imgId: Int = R.drawable.account_circle_24px
)
