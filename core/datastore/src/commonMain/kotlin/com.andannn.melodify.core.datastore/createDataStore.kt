package com.andannn.melodify.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import okio.FileSystem
import okio.Path

fun createDataStore(
    producePath: () -> Path,
    fileSystem: FileSystem,
    ): DataStore<UserPreferences> =
    DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = fileSystem,
            producePath = producePath,
            serializer = UserPreferencesSerializer,
        ),
    )

internal const val dataStoreFileName = "dice.preferences_pb"