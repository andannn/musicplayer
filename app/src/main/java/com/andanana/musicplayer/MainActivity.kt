package com.andanana.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme
import com.andanana.musicplayer.ui.SimpleMusicApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val runTimePermissions =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    private val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var mainUiState by mutableStateOf<MainUiState>(MainUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.mainUiState
                    .onEach { state ->
                        mainUiState = state
                    }
                    .collect()
            }
        }

//        splashScreen.setKeepOnScreenCondition {
//            mainUiState is MainUiState.Loading
//        }

        setContent {
            var permissionGranted by remember {
                mutableStateOf(isPermissionGranted())
            }
            val launcher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = {
                        it.forEach { (permission, granted) ->
                            if (!granted) {
                                finish()
                            }
                        }
                        permissionGranted = true
                    },
                )

            if (!permissionGranted) {
                LaunchedEffect(Unit) {
                    runTimePermissions.filter {
                        ContextCompat.checkSelfPermission(
                            // context =
                            this@MainActivity,
                            // permission =
                            it,
                        ) == PackageManager.PERMISSION_DENIED
                    }.let {
                        launcher.launch(it.toTypedArray())
                    }
                }
            }

            LaunchedEffect(permissionGranted) {
                mainViewModel.onPermissionStateChanged(permissionGranted)
            }

            MusicPlayerTheme {
                if (permissionGranted) {
                    SimpleMusicApp()
                }
            }
        }
    }

    private fun isPermissionGranted(): Boolean {
        runTimePermissions.forEach { permission ->
            when (ContextCompat.checkSelfPermission(this, permission)) {
                PackageManager.PERMISSION_DENIED -> return false
            }
        }
        return true
    }
}
