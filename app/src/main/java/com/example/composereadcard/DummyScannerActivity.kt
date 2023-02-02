package com.example.composereadcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composereadcard.ui.theme.ComposeReadCardTheme

class DummyScannerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Screen(onDone = { finish() })
        }
    }
}

@Composable
fun Screen(onDone: () -> Unit) {
    ComposeReadCardTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Button(
                onClick = { onDone() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "Done!")
            }
        }
    }
}

@Composable
@Preview
fun Preview() {
    Screen {}
}