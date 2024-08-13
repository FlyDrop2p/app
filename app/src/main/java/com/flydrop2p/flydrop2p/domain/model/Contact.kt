package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.data.local.contact.ContactEntity

data class Contact(
    val id: Int,
    val username: String,
    val imageFilePath: String?
)

fun Contact.toContactEntity(): ContactEntity {
    return ContactEntity(
        contactId = id,
        username = username,
        imageFilePath = imageFilePath
    )
}

fun ContactEntity.toContact(): Contact {
    return Contact(
        id = contactId,
        username = username,
        imageFilePath = imageFilePath
    )
}