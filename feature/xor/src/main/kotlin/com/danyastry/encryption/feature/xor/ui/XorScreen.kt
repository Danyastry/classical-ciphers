package com.danyastry.encryption.feature.xor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.danyastry.encryption.core.ui.components.ModeSelector
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.SegmentedSelector
import com.danyastry.encryption.core.ui.components.inputLabelRes
import com.danyastry.encryption.core.ui.components.outputLabelRes
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.xor.ByteEncoding
import com.danyastry.encryption.feature.xor.R

@Composable
fun XorRoute(
    modifier: Modifier = Modifier,
    viewModel: XorViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    XorScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onKeyChange = viewModel::onKeyChange,
        onKeyFormatChange = viewModel::onKeyFormatChange,
        onEncodingChange = viewModel::onEncodingChange,
        onModeChange = viewModel::onModeChange,
        onCribChange = viewModel::onCribChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun XorScreen(
    state: XorUiState,
    onInputChange: (String) -> Unit,
    onKeyChange: (String) -> Unit,
    onKeyFormatChange: (KeyFormat) -> Unit,
    onEncodingChange: (ByteEncoding) -> Unit,
    onModeChange: (CipherMode) -> Unit,
    onCribChange: (String) -> Unit,
    onUseOutputAsInput: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ModeSelector(selected = state.mode, onSelect = onModeChange)
        SegmentedSelector(
            options = ByteEncoding.entries,
            selected = state.encoding,
            label = { stringResource(it.labelRes) },
            onSelect = onEncodingChange,
        )
        SegmentedSelector(
            options = KeyFormat.entries,
            selected = state.keyFormat,
            label = { stringResource(it.labelRes) },
            onSelect = onKeyFormatChange,
        )
        OutlinedTextField(
            value = state.key,
            onValueChange = onKeyChange,
            label = { Text(stringResource(R.string.xor_key_label)) },
            singleLine = true,
            isError = state.isKeyInvalid,
            supportingText = {
                Text(stringResource(if (state.isKeyInvalid) state.keyFormat.errorRes else R.string.xor_key_hint))
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
            modifier = Modifier.fillMaxWidth(),
        )
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
            isError = state.isInputInvalid,
            supportingText = when {
                state.isInputInvalid -> stringResource(state.encoding.errorRes)
                state.mode == CipherMode.DECRYPT -> stringResource(state.encoding.inputHintRes)
                else -> null
            },
        )
        ResultCard(
            label = stringResource(state.mode.outputLabelRes),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
        if (!state.isKeyInvalid && !state.isInputInvalid && state.input.isNotEmpty()) {
            KnownPlaintextCard(state, onCribChange)
        }
    }
}

@Composable
private fun KnownPlaintextCard(state: XorUiState, onCribChange: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.xor_attack_title),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = stringResource(R.string.xor_attack_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            CipherTextField(
                value = state.crib,
                onValueChange = onCribChange,
                label = stringResource(R.string.xor_attack_crib),
            )
            if (state.recoveredKeyHex.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.xor_attack_result),
                    style = MaterialTheme.typography.labelLarge,
                )
                SelectionContainer {
                    Column {
                        Text(text = state.recoveredKeyText, fontFamily = FontFamily.Monospace)
                        Text(
                            text = state.recoveredKeyHex,
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

private val ByteEncoding.labelRes: Int
    get() = when (this) {
        ByteEncoding.HEX -> R.string.xor_encoding_hex
        ByteEncoding.BASE64 -> R.string.xor_encoding_base64
    }

private val ByteEncoding.errorRes: Int
    get() = when (this) {
        ByteEncoding.HEX -> R.string.xor_error_hex
        ByteEncoding.BASE64 -> R.string.xor_error_base64
    }

private val ByteEncoding.inputHintRes: Int
    get() = when (this) {
        ByteEncoding.HEX -> R.string.xor_input_hint_hex
        ByteEncoding.BASE64 -> R.string.xor_input_hint_base64
    }

private val KeyFormat.labelRes: Int
    get() = when (this) {
        KeyFormat.TEXT -> R.string.xor_key_format_text
        KeyFormat.HEX -> R.string.xor_key_format_hex
    }

private val KeyFormat.errorRes: Int
    get() = when (this) {
        KeyFormat.TEXT -> R.string.xor_error_key_empty
        KeyFormat.HEX -> R.string.xor_error_key_hex
    }

@Preview(showBackground = true)
@Composable
private fun XorScreenPreview() {
    EncryptionTheme {
        XorScreen(
            state = XorUiState(input = "Burning 'em, if you ain't quick and nimble", crib = "Burning").recalculated(),
            onInputChange = {},
            onKeyChange = {},
            onKeyFormatChange = {},
            onEncodingChange = {},
            onModeChange = {},
            onCribChange = {},
            onUseOutputAsInput = {},
        )
    }
}
