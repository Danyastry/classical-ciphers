package com.danyastry.encryption.feature.onetimepad.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.core.ui.components.CipherTextField
import com.danyastry.encryption.core.ui.components.LettersField
import com.danyastry.encryption.core.ui.components.ModeSelector
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.inputLabelRes
import com.danyastry.encryption.core.ui.components.outputLabelRes
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.onetimepad.OneTimePadCipher
import com.danyastry.encryption.feature.onetimepad.R

@Composable
fun OneTimePadRoute(
    modifier: Modifier = Modifier,
    viewModel: OneTimePadViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    OneTimePadScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onKeyChange = viewModel::onKeyChange,
        onGenerateKey = viewModel::onGenerateKey,
        onModeChange = viewModel::onModeChange,
        onAlternativeChange = viewModel::onAlternativeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun OneTimePadScreen(
    state: OneTimePadUiState,
    onInputChange: (String) -> Unit,
    onKeyChange: (String) -> Unit,
    onGenerateKey: () -> Unit,
    onModeChange: (CipherMode) -> Unit,
    onAlternativeChange: (String) -> Unit,
    onUseOutputAsInput: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.otp_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        ModeSelector(selected = state.mode, onSelect = onModeChange)
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
        )
        LettersField(
            value = state.key,
            onValueChange = onKeyChange,
            label = stringResource(R.string.otp_key_label),
            isError = state.isKeyTooShort,
            supportingText = stringResource(
                if (state.isKeyTooShort) R.string.otp_key_too_short else R.string.otp_key_hint,
                state.lettersNeeded,
                state.key.length,
            ),
        )
        OutlinedButton(onClick = onGenerateKey, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.otp_generate_key))
        }
        ResultCard(
            label = stringResource(state.mode.outputLabelRes),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
        if (state.ciphertext.isNotBlank() && !state.isKeyTooShort) {
            PerfectSecrecyCard(state, onAlternativeChange)
        }
    }
}

@Composable
private fun PerfectSecrecyCard(state: OneTimePadUiState, onAlternativeChange: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.otp_secrecy_title),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = stringResource(R.string.otp_secrecy_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            CipherTextField(
                value = state.alternativePlaintext,
                onValueChange = onAlternativeChange,
                label = stringResource(R.string.otp_secrecy_label),
                isError = state.isAlternativeLengthMismatch,
                supportingText = if (state.isAlternativeLengthMismatch) {
                    stringResource(R.string.otp_secrecy_length_mismatch, OneTimePadCipher.lettersIn(state.ciphertext))
                } else {
                    null
                },
            )
            if (state.alternativeKey.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.otp_secrecy_result),
                    style = MaterialTheme.typography.labelLarge,
                )
                SelectionContainer {
                    Text(text = state.alternativeKey, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OneTimePadScreenPreview() {
    EncryptionTheme {
        OneTimePadScreen(
            state = OneTimePadUiState(input = "Hello", alternativePlaintext = "Later").recalculated(),
            onInputChange = {},
            onKeyChange = {},
            onGenerateKey = {},
            onModeChange = {},
            onAlternativeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
