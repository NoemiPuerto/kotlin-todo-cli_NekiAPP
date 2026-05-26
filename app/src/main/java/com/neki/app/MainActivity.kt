package com.neki.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.neki.app.core.navigation.NekiNavigation
import com.neki.app.ui.theme.NekiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NekiTheme {
                NekiNavigation()
            }
        }
    }
}