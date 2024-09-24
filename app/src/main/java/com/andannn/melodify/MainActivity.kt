package com.andannn.melodify

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.andannn.melodify.core.designsystem.theme.MelodifyTheme
import com.andannn.melodify.ui.MelodifyApp
import dagger.hilt.android.AndroidEntryPoint
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
    private lateinit var intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        intentSenderLauncher = registerForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
        ) { result ->
            Log.d(TAG, "activity result: $result")
        }

        lifecycleScope.launch {
            mainViewModel.deleteMediaItemEventFlow.collect { uris ->
                Log.d(TAG, "Requesting delete media items: $uris")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val editPendingIntent = MediaStore.createTrashRequest(
                        /* resolver = */ contentResolver,
                        /* uris = */ uris.map { Uri.parse(it) },
                        /* value = */ true,
                    )
                    val request = IntentSenderRequest.Builder(editPendingIntent.intentSender).build()

                    intentSenderLauncher.launch(request)
                }
            }
        }

        setContent {
            var permissionGranted by remember {
                mutableStateOf(isPermissionGranted())
            }
            val launcher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = {
                        it.forEach { (_, granted) ->
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

            MelodifyTheme(darkTheme = true, isDynamicColor = true) {
                if (permissionGranted) {
                    MelodifyApp()
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
