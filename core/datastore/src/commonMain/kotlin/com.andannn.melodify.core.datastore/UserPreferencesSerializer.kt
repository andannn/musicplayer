package com.andannn.melodify.core.datastore

import androidx.datastore.core.IOException
import androidx.datastore.core.okio.OkioSerializer
import okio.BufferedSink
import okio.BufferedSource

class UserPreferencesSerializer : OkioSerializer<UserPreferences> {
    override val defaultValue: UserPreferences
        get() = UserPreferences()

    override suspend fun readFrom(source: BufferedSource): UserPreferences {
        try {
            return UserPreferences.ADAPTER.decode(source)
        } catch (exception: IOException) {
            throw Exception(exception.message ?: "Serialization Exception")
        }
    }

    override suspend fun writeTo(t: UserPreferences, sink: BufferedSink) {
        sink.write(t.encode())
    }
}