package com.example.composereadcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composereadcard.ui.theme.ComposeReadCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeReadCardTheme {
                Screen()
            }
        }
    }
}

@Composable
fun Screen(viewModel: MainViewModel = viewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val uiState = viewModel.uiState.collectAsState()
            if (uiState.value.shouldShowButton) {
                ScanCard(viewModel)
            }
            CardNumber(cardNumber = uiState.value.cardNumber)
        }
    }
}

@Composable
fun ScanCard(viewModel: MainViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = viewModel.launcher.resultContract,
        onResult = { result ->
            viewModel.handleResult(result)
        }
    )
    Button(onClick = {
        launcher.launch(null)
    }, modifier = Modifier.wrapContentSize()) {
        Text(text = "Scan Card")
    }
}

@Composable
fun CardNumber(cardNumber: String) {
    Text(text = "Scanned Card Number: $cardNumber")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeReadCardTheme {
        Screen()
    }
}