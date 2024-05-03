package com.example.flydrop2p.domain.model

import com.example.flydrop2p.R

data class ChatInfo(
    val id: Int,
    val name: String,
    val lastMessage: String,
    val timestamp: String,
    val imgId: Int = R.drawable.account_circle_24px
)
