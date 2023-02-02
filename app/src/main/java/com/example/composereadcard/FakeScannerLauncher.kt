package com.example.composereadcard

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

/**
 * Fake implementation of [ScannerLauncher], useful for testing scenarios where we don't want to depend
 * on Google Play
 */
class FakeScannerLauncher : ScannerLauncher {
    override val resultContract: ActivityResultContract<Nothing, String> =
        object : ActivityResultContract<Nothing, String>() {
            override fun createIntent(context: Context, input: Nothing?): Intent {
                return Intent(context, DummyScannerActivity::class.java)
            }

            override fun parseResult(resultCode: Int, intent: Intent?): String {
                return "1234"
            }
        }

    override suspend fun isEligible(): Boolean {
        return true
    }
}