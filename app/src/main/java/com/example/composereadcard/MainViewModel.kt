package com.example.composereadcard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    val uiState = MutableStateFlow(UiState(false, ""))

    // TODO: PK - Scanner Launcher should be injected so it can be swapped for debug/testing
//    val launcher: ScannerLauncher = WalletScannerLauncher(app)
    val launcher: ScannerLauncher = FakeScannerLauncher()

    init {
        checkEligibility()
    }

    private fun checkEligibility() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(shouldShowButton = false)
            uiState.value = uiState.value.copy(shouldShowButton = launcher.isEligible())
        }
    }

    fun handleResult(result: String) {
        checkEligibility()
        uiState.value = uiState.value.copy(cardNumber = result)
    }

}