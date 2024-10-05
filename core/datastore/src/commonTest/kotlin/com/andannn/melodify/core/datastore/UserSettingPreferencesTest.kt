package com.andannn.melodify.core.datastore

import kotlin.test.BeforeTest
import kotlin.test.Test

class UserSettingPreferencesTest {

    private lateinit var preferences: UserSettingPreferences

    @BeforeTest
    fun setUp() {
        val dataStore = createDataStore {
            "./temp/$dataStoreFileName"
        }
        preferences = UserSettingPreferences(dataStore);
    }

    @Test
    fun `set and get layout type`() {

    }
}