package com.danyastry.encryption.feature.scytale.ui

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
import com.danyastry.encryption.feature.scytale.R

@Composable
fun ScytaleRoute(
    modifier: Modifier = Modifier,
    viewModel: ScytaleViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ScytaleScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onTurnsChange = viewModel::onTurnsChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun ScytaleScreen(
    state: ScytaleUiState,
    onInputChange: (String) -> Unit,
    onTurnsChange: (Int) -> Unit,
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
            label = stringResource(R.string.scytale_turns_label, state.turns),
            value = state.turns,
            range = ScytaleUiState.MIN_TURNS..ScytaleUiState.MAX_TURNS,
            onValueChange = onTurnsChange,
        )
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
            supportingText = stringResource(R.string.scytale_input_hint),
        )
        ResultCard(
            label = stringResource(state.mode.outputLabelRes),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
        if (state.wrapped.isNotEmpty()) {
            RodTable(state.wrapped)
        }
    }
}

@Composable
private fun RodTable(wrapped: List<String>) {
    val letterColor = MaterialTheme.colorScheme.primary
    val emptyColor = MaterialTheme.colorScheme.outlineVariant
    val columns = wrapped.first().length
    LetterTable(
        title = stringResource(R.string.scytale_rod_title),
        subtitle = stringResource(R.string.scytale_rod_hint),
        rows = wrapped.mapIndexed { turn, row ->
            LetterRow(
                cells = (0 until columns).map { column ->
                    when (val char = row.getOrNull(column)) {
                        null -> LetterCell(EMPTY_CELL, emptyColor)
                        ' ' -> LetterCell(SPACE_CELL, letterColor)
                        else -> LetterCell(char, letterColor)
                    }
                },
                label = (turn + 1).toString(),
            )
        },
    )
}

private const val EMPTY_CELL = '·'
private const val SPACE_CELL = '␣'

@Preview(showBackground = true)
@Composable
private fun ScytaleScreenPreview() {
    EncryptionTheme {
        ScytaleScreen(
            state = ScytaleUiState(input = "I am hurt very badly help").recalculated(),
            onInputChange = {},
            onTurnsChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
