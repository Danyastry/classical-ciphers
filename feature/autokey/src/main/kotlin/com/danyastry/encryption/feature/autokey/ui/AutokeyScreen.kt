package com.danyastry.encryption.feature.autokey.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.ui.R as CoreUiR
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.core.ui.components.CipherTextField
import com.danyastry.encryption.core.ui.components.LetterCell
import com.danyastry.encryption.core.ui.components.LetterRow
import com.danyastry.encryption.core.ui.components.LetterTable
import com.danyastry.encryption.core.ui.components.LettersField
import com.danyastry.encryption.core.ui.components.ModeSelector
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.inputLabelRes
import com.danyastry.encryption.core.ui.components.outputLabelRes
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.autokey.R

@Composable
fun AutokeyRoute(
    modifier: Modifier = Modifier,
    viewModel: AutokeyViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AutokeyScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onPrimerChange = viewModel::onPrimerChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun AutokeyScreen(
    state: AutokeyUiState,
    onInputChange: (String) -> Unit,
    onPrimerChange: (String) -> Unit,
    onModeChange: (CipherMode) -> Unit,
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
        LettersField(
            value = state.primer,
            onValueChange = onPrimerChange,
            label = stringResource(R.string.autokey_primer_label),
            isError = !state.isPrimerValid,
            supportingText = stringResource(
                if (state.isPrimerValid) R.string.autokey_primer_hint else CoreUiR.string.error_letters_required,
            ),
        )
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
        )
        ResultCard(
            label = stringResource(state.mode.outputLabelRes),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
        if (state.alignment.isNotEmpty()) {
            val primerColor = MaterialTheme.colorScheme.tertiary
            val plaintextKeyColor = MaterialTheme.colorScheme.secondary
            LetterTable(
                title = stringResource(R.string.autokey_alignment_title),
                subtitle = stringResource(R.string.autokey_alignment_hint),
                rows = listOf(
                    LetterRow.uniform(state.alignment.map { it.input }, MaterialTheme.colorScheme.onSurface, stringResource(state.mode.inputLabelRes)),
                    LetterRow(
                        cells = state.alignment.mapIndexed { index, letter ->
                            LetterCell(letter.key, if (index < state.primer.length) primerColor else plaintextKeyColor)
                        },
                        label = stringResource(CoreUiR.string.label_key),
                    ),
                    LetterRow.uniform(state.alignment.map { it.output }, MaterialTheme.colorScheme.primary, stringResource(state.mode.outputLabelRes)),
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AutokeyScreenPreview() {
    EncryptionTheme {
        AutokeyScreen(
            state = AutokeyUiState(input = "Attack at dawn").recalculated(),
            onInputChange = {},
            onPrimerChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
