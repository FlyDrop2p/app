package com.flydrop2p.flydrop2p.data.local.serializer

import android.os.Build
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.flydrop2p.flydrop2p.data.local.profile.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object ProfileSerializer : Serializer<ProfileEntity> {
    override val defaultValue = ProfileEntity(0, 0, Build.MODEL.toString(), null)

    override suspend fun readFrom(input: InputStream): ProfileEntity {
        try {
            return Json.decodeFromString(ProfileEntity.serializer(), input.readBytes().decodeToString())
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read profile", serialization)
        }
    }

    override suspend fun writeTo(t: ProfileEntity, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(ProfileEntity.serializer(), t).encodeToByteArray())
        }
    }
}