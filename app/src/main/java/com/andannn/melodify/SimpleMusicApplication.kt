package com.andannn.melodify

import android.app.Application
import com.andannn.melodify.core.data.di.commonDataModule
import com.andannn.melodify.core.data.di.platformDataModule
import com.andannn.melodify.core.database.di.databaseModule
import com.andannn.melodify.core.datastore.di.userPreferencesModule
import com.andannn.melodify.core.network.di.serviceModule
import com.andannn.melodify.core.player.di.playerModule
import com.andannn.melodify.di.appModule
import com.andannn.melodify.feature.common.di.globalUiControllerModule
import com.andannn.melodify.feature.home.di.homeFeatureModule
import com.andannn.melodify.feature.playList.navigation.di.playListModule
import com.andannn.melodify.feature.player.di.playerFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber
import timber.log.Timber.Forest.plant

class SimpleMusicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@SimpleMusicApplication)

            modules(
                listOf(
                    appModule,
                    commonDataModule,
                    platformDataModule,
                    playerModule,
                    serviceModule,
                    userPreferencesModule,
                    databaseModule,
                    globalUiControllerModule,
                    homeFeatureModule,
                    playListModule,
                    playerFeatureModule
                )
            )
        }
    }
}