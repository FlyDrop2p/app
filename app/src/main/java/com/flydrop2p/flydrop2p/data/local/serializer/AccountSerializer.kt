package com.flydrop2p.flydrop2p.data.local.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.flydrop2p.flydrop2p.data.local.account.AccountEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AccountSerializer : Serializer<AccountEntity> {
    override val defaultValue = AccountEntity(0, 0)

    override suspend fun readFrom(input: InputStream): AccountEntity {
        try {
            return Json.decodeFromString(AccountEntity.serializer(), input.readBytes().decodeToString())
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read account", serialization)
        }
    }

    override suspend fun writeTo(t: AccountEntity, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(AccountEntity.serializer(), t).encodeToByteArray())
        }
    }
}