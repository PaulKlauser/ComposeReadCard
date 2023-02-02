package com.example.composereadcard

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import com.google.android.gms.wallet.PaymentCardRecognitionIntentRequest
import com.google.android.gms.wallet.PaymentCardRecognitionResult
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Google Play Services-backed [ScannerLauncher]. Responsible for checking if we're eligible to scan
 * a card, as well as exposing a result contract for launching and parsing the result of doing so.
 */
class WalletScannerLauncher(private val app: Application) : ScannerLauncher {

    override val resultContract = object : ActivityResultContract<Nothing, String>() {
        override fun createIntent(context: Context, input: Nothing?): Intent {
            val intent = requireNotNull(paymentIntent) { "Make sure to call isEligible() first!" }
            return Intent(StartIntentSenderForResult.ACTION_INTENT_SENDER_REQUEST)
                .putExtra(
                    StartIntentSenderForResult.EXTRA_INTENT_SENDER_REQUEST,
                    IntentSenderRequest.Builder(intent).build()
                )
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String {
            Log.d("WalletScannerLauncher", "$intent")
            return intent?.let { PaymentCardRecognitionResult.getFromIntent(it) }?.pan ?: ""
        }

    }
    private var paymentIntent: PendingIntent? = null

    override suspend fun isEligible(): Boolean {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
            .build()

        val client = Wallet.getPaymentsClient(app, walletOptions)

        val request = PaymentCardRecognitionIntentRequest.getDefaultInstance()
        // Wrapping the Play callback API in a coroutine so we don't have to pass a callback into
        // isEligible, though doing so would be just as valid.
        return suspendCancellableCoroutine { continuation ->
            client
                .getPaymentCardRecognitionIntent(request)
                .addOnSuccessListener { intentResponse ->
                    paymentIntent = intentResponse.paymentCardRecognitionPendingIntent
                    continuation.resume(true)
                }
                .addOnFailureListener { e ->
                    // The API is not available either because the feature is not enabled on the device
                    // or because your app is not registered.
                    Log.e("WalletScannerLauncher", "Payment card ocr not available.", e)
                    continuation.resume(false)
                }
        }
    }

}