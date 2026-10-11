package com.danyastry.encryption.feature.railfence.ui

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
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.core.ui.components.CipherTextField
import com.danyastry.encryption.core.ui.components.LabeledSlider
import com.danyastry.encryption.core.ui.components.LetterCell
import com.danyastry.encryption.core.ui.components.LetterRow
import com.danyastry.encryption.core.ui.components.LetterTable
import com.danyastry.encryption.core.ui.components.ModeSelector
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.inputLabelRes
import com.danyastry.encryption.core.ui.components.outputLabelRes
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.railfence.R

@Composable
fun RailFenceRoute(
    modifier: Modifier = Modifier,
    viewModel: RailFenceViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    RailFenceScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onRailsChange = viewModel::onRailsChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun RailFenceScreen(
    state: RailFenceUiState,
    onInputChange: (String) -> Unit,
    onRailsChange: (Int) -> Unit,
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
        LabeledSlider(
            label = stringResource(R.string.railfence_rails_label, state.rails),
            value = state.rails,
            range = RailFenceUiState.MIN_RAILS..RailFenceUiState.MAX_RAILS,
            onValueChange = onRailsChange,
        )
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
            supportingText = stringResource(R.string.railfence_input_hint),
        )
        ResultCard(
            label = stringResource(state.mode.outputLabelRes),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
        if (state.zigzag.isNotEmpty()) {
            val letterColor = MaterialTheme.colorScheme.primary
            val emptyColor = MaterialTheme.colorScheme.outlineVariant
            LetterTable(
                title = stringResource(R.string.railfence_zigzag_title),
                subtitle = stringResource(R.string.railfence_zigzag_hint),
                rows = state.zigzag.mapIndexed { rail, cells ->
                    LetterRow(
                        cells = cells.map { char ->
                            when (char) {
                                null -> LetterCell(EMPTY_CELL, emptyColor)
                                ' ' -> LetterCell(SPACE_CELL, letterColor)
                                else -> LetterCell(char, letterColor)
                            }
                        },
                        label = (rail + 1).toString(),
                    )
                },
            )
        }
    }
}

private const val EMPTY_CELL = '·'
private const val SPACE_CELL = '␣'

@Preview(showBackground = true)
@Composable
private fun RailFenceScreenPreview() {
    EncryptionTheme {
        RailFenceScreen(
            state = RailFenceUiState(input = "WE ARE DISCOVERED").recalculated(),
            onInputChange = {},
            onRailsChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
