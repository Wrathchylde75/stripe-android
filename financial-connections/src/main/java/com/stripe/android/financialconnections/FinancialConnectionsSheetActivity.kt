package com.stripe.android.financialconnections

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.stripe.android.financialconnections.FinancialConnectionsSheetViewEffect.FinishWithResult
import com.stripe.android.financialconnections.FinancialConnectionsSheetViewEffect.OpenAuthFlowWithUrl
import com.stripe.android.financialconnections.FinancialConnectionsSheetViewEffect.OpenNativeAuthFlow
import com.stripe.android.financialconnections.launcher.FinancialConnectionsSheetActivityResult
import com.stripe.android.financialconnections.presentation.CreateBrowserIntentForUrl
import com.stripe.android.financialconnections.ui.FinancialConnectionsSheetNativeActivity

internal class FinancialConnectionsSheetActivity :
    AppCompatActivity(R.layout.activity_financialconnections_sheet), MavericksView {

    val viewModel: FinancialConnectionsSheetViewModel by viewModel()

    private val startForResult = registerForActivityResult(StartActivityForResult()) {
        viewModel.onActivityResult()
    }

    private val startNativeAuthFlowForResult = registerForActivityResult(StartActivityForResult()) {
        viewModel.onNativeAuthFlowResult()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onEach { postInvalidate() }
        if (savedInstanceState != null) viewModel.onActivityRecreated()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishWithResult(FinancialConnectionsSheetActivityResult.Canceled)
    }

    /**
     * Handles new intents in the form of the redirect from the custom tab hosted auth flow
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewModel.handleOnNewIntent(intent)
    }

    /**
     * handle state changes here.
     */
    override fun invalidate() {
        withState(viewModel) { state ->
            state.viewEffect?.let { viewEffect ->
                when (viewEffect) {
                    is OpenAuthFlowWithUrl -> startForResult.launch(
                        CreateBrowserIntentForUrl(
                            context = this,
                            uri = Uri.parse(viewEffect.url),
                        )
                    )
                    is FinishWithResult -> finishWithResult(
                        viewEffect.result
                    )
                    OpenNativeAuthFlow -> startNativeAuthFlowForResult.launch(
                        Intent(this, FinancialConnectionsSheetNativeActivity::class.java)
                    )
                }
                viewModel.onViewEffectLaunched()
            }
        }
    }

    private fun finishWithResult(result: FinancialConnectionsSheetActivityResult) {
        setResult(RESULT_OK, Intent().putExtras(result.toBundle()))
        finish()
    }
}
