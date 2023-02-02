package com.example.composereadcard

import androidx.activity.result.contract.ActivityResultContract

interface ScannerLauncher {

    val resultContract: ActivityResultContract<Nothing, String>

    suspend fun isEligible(): Boolean
}