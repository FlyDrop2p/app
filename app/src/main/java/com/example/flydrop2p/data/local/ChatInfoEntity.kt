package com.example.flydrop2p.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flydrop2p.R

@Entity
data class ChatInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val imgId: Int = R.drawable.account_circle_24px
    // val chatMembers: List<Int>
)
