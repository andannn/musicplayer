package com.andanana.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme
import com.andanana.musicplayer.ui.SimpleMusicApp
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val runTimePermissions = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
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
                }

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
