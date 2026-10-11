package com.danyastry.encryption.feature.bacon.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.danyastry.encryption.feature.bacon.BaconAlphabet
import com.danyastry.encryption.feature.bacon.R

@Composable
fun BaconRoute(
    modifier: Modifier = Modifier,
    viewModel: BaconViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BaconScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onAlphabetChange = viewModel::onAlphabetChange,
        onModeChange = viewModel::onModeChange,
        onDecodeSourceChange = viewModel::onDecodeSourceChange,
        onCoverChange = viewModel::onCoverChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        onUseStegotextAsInput = viewModel::onUseStegotextAsInput,
        modifier = modifier,
    )
}

@Composable
fun BaconScreen(
    state: BaconUiState,
    onInputChange: (String) -> Unit,
    onAlphabetChange: (BaconAlphabet) -> Unit,
    onModeChange: (CipherMode) -> Unit,
    onDecodeSourceChange: (DecodeSource) -> Unit,
    onCoverChange: (String) -> Unit,
    onUseOutputAsInput: () -> Unit,
    onUseStegotextAsInput: () -> Unit,
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
            options = BaconAlphabet.entries,
            selected = state.alphabet,
            label = { stringResource(it.labelRes) },
            onSelect = onAlphabetChange,
        )
        if (state.mode == CipherMode.DECRYPT) {
            SegmentedSelector(
                options = DecodeSource.entries,
                selected = state.decodeSource,
                label = { stringResource(it.labelRes) },
                onSelect = onDecodeSourceChange,
            )
        }
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.inputLabelRes),
            isError = state.isIncompleteCode,
            supportingText = if (state.isIncompleteCode) stringResource(R.string.bacon_error_incomplete) else null,
        )
        ResultCard(
            label = stringResource(state.mode.outputLabelRes),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
        if (state.mode == CipherMode.ENCRYPT) {
            HideInTextSection(
                state = state,
                onCoverChange = onCoverChange,
                onUseStegotextAsInput = onUseStegotextAsInput,
            )
        }
    }
}

@Composable
private fun HideInTextSection(
    state: BaconUiState,
    onCoverChange: (String) -> Unit,
    onUseStegotextAsInput: () -> Unit,
) {
    Text(
        text = stringResource(R.string.bacon_hide_title),
        style = MaterialTheme.typography.titleMedium,
    )
    CipherTextField(
        value = state.cover,
        onValueChange = onCoverChange,
        label = stringResource(R.string.bacon_cover_label),
        isError = state.isCoverTooShort,
        supportingText = stringResource(
            if (state.isCoverTooShort) R.string.bacon_cover_too_short else R.string.bacon_cover_hint,
            state.requiredCoverLetters,
            state.coverLetters,
        ),
    )
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.bacon_stegotext_label),
                style = MaterialTheme.typography.labelLarge,
            )
            SelectionContainer {
                Text(
                    text = state.stegotext.ifEmpty { stringResource(R.string.bacon_stegotext_placeholder) },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            TextButton(
                onClick = onUseStegotextAsInput,
                enabled = state.stegotext.isNotEmpty(),
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(stringResource(R.string.bacon_reveal_action))
            }
        }
    }
}

private val BaconUiState.inputLabelRes: Int
    get() = when {
        mode == CipherMode.ENCRYPT -> mode.inputLabelRes
        decodeSource == DecodeSource.AB_GROUPS -> R.string.bacon_input_code
        else -> R.string.bacon_input_stegotext
    }

private val BaconAlphabet.labelRes: Int
    get() = when (this) {
        BaconAlphabet.CLASSIC_24 -> R.string.bacon_alphabet_classic
        BaconAlphabet.MODERN_26 -> R.string.bacon_alphabet_modern
    }

private val DecodeSource.labelRes: Int
    get() = when (this) {
        DecodeSource.AB_GROUPS -> R.string.bacon_source_groups
        DecodeSource.LETTER_CASE -> R.string.bacon_source_case
    }

@Preview(showBackground = true)
@Composable
private fun BaconScreenPreview() {
    EncryptionTheme {
        BaconScreen(
            state = BaconUiState(input = "Hi").recalculated(),
            onInputChange = {},
            onAlphabetChange = {},
            onModeChange = {},
            onDecodeSourceChange = {},
            onCoverChange = {},
            onUseOutputAsInput = {},
            onUseStegotextAsInput = {},
        )
    }
}
