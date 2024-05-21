package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.data.local.ContactEntity

data class Contact(
    val id: Int,
    val name: String,
    val imgId: Int = R.drawable.account_circle_24px
)

fun Contact.toContactEntity(): ContactEntity {
    return ContactEntity(
        name = name,
        imgId = imgId
    )
}

fun ContactEntity.toContact(): Contact {
    return Contact(
        id = id,
        name = name,
        imgId = imgId
    )
}