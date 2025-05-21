package com.rockduck.kiwibus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rockduck.kiwibus.screen.MainScreen
import com.rockduck.kiwibus.ui.theme.KiwiBusTheme

/**
 * @author Ricky Chen
 * This is entry point when first enter.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KiwiBusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    MainScreen(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainPreview() {
    KiwiBusTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
            MainScreen(modifier = Modifier.padding(padding))
        }
    }
}