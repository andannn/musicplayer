package com.andannn.melodify

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.andannn.melodify.core.designsystem.theme.MelodifyTheme
import com.andannn.melodify.ui.MelodifyApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val runTimePermissions =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

//    private val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

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
