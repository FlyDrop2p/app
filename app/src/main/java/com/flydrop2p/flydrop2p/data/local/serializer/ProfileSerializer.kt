package com.flydrop2p.flydrop2p.data.local.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.flydrop2p.flydrop2p.domain.model.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object ProfileSerializer : Serializer<Profile> {
    override val defaultValue: Profile = Profile("username")

    override suspend fun readFrom(input: InputStream): Profile {
        try {
            return Json.decodeFromString(Profile.serializer(), input.readBytes().decodeToString())
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read profile", serialization)
        }
    }

    override suspend fun writeTo(t: Profile, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(Profile.serializer(), t).encodeToByteArray())
        }
    }
}