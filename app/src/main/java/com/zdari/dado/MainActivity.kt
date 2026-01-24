package com.zdari.dado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.zdari.dado.navigation.AppNavigation
import com.zdari.dado.ui.theme.DadoTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DadoTheme {
                AppNavigation()
            }
        }
    }
}







