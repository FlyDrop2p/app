package com.flydrop2p.flydrop2p.data.local.serializer

import android.os.Build
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.flydrop2p.flydrop2p.domain.model.contact.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AccountSerializer : Serializer<Account> {
    override val defaultValue: Account = Account(Build.MODEL[4].code.toLong(), 0)

    override suspend fun readFrom(input: InputStream): Account {
        try {
            return Json.decodeFromString(Account.serializer(), input.readBytes().decodeToString())
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read profile", serialization)
        }
    }

    override suspend fun writeTo(t: Account, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(Account.serializer(), t).encodeToByteArray())
        }
    }
}