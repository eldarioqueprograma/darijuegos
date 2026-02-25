package com.zdari.darijuegos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.zdari.darijuegos.navigation.AppNavigation
import com.zdari.darijuegos.ui.theme.DarijuegosTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DarijuegosTheme {
                AppNavigation()
            }
        }
    }
}
