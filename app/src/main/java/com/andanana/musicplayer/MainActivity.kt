package com.andanana.musicplayer

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme
import com.andanana.musicplayer.ui.SimpleMusicApp
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val runTimePermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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

        splashScreen.setKeepOnScreenCondition {
            when (mainUiState) {
                MainUiState.Loading -> true
                MainUiState.Ready -> false
            }
        }

        setContent {
            var permissionGranted by remember {
                mutableStateOf(isPermissionGranted())
            }
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = {
                    it.forEach { (permission, granted) ->
                        if (!granted) {
                            finish()
                        }
                    }
                    mainViewModel.syncMediaStore()
                    permissionGranted = true
                }
            )

            if (!permissionGranted) {
                SideEffect {
                    runTimePermissions.filter {
                        ContextCompat.checkSelfPermission(
                            /* context = */ this,
                            /* permission = */ it
                        ) == PackageManager.PERMISSION_DENIED
                    }.let {
                        launcher.launch(it.toTypedArray())
                    }
                }
            } else {
                LaunchedEffect(Unit) {
                    val context = this@MainActivity
//                    val intent = Intent(context, PlayerService::class.java)
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        context.startForegroundService(intent)
//                    }
                    val sessionToken = SessionToken(this@MainActivity, ComponentName(this@MainActivity, PlayerService::class.java))
                    val controllerFuture = MediaController.Builder(this@MainActivity, sessionToken).buildAsync()
                    controllerFuture.addListener(
                        {
                            // Call controllerFuture.get() to retrieve the MediaController.
                            // MediaController implements the Player interface, so it can be
                            // attached to the PlayerView UI component.
//                            playerView.setPlayer(controllerFuture.get())
                            Log.d(TAG, "onCreate:  controllerFuture ${controllerFuture.get()}")
                        },
                        MoreExecutors.directExecutor()
                    )
                }
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
