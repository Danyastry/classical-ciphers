package com.danyastry.encryption.feature.bifid.ui

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
import com.danyastry.encryption.core.ui.components.LabeledSlider
import com.danyastry.encryption.core.ui.components.LetterRow
import com.danyastry.encryption.core.ui.components.LetterTable
import com.danyastry.encryption.core.ui.components.LettersField
import com.danyastry.encryption.core.ui.components.ModeSelector
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.inputLabelRes
import com.danyastry.encryption.core.ui.components.outputLabelRes
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.bifid.BifidKey
import com.danyastry.encryption.feature.bifid.R

@Composable
fun BifidRoute(
    modifier: Modifier = Modifier,
    viewModel: BifidViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BifidScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onKeywordChange = viewModel::onKeywordChange,
        onPeriodChange = viewModel::onPeriodChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun BifidScreen(
    state: BifidUiState,
    onInputChange: (String) -> Unit,
    onKeywordChange: (String) -> Unit,
    onPeriodChange: (Int) -> Unit,
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
            value = state.keyword,
            onValueChange = onKeywordChange,
            label = stringResource(CoreUiR.string.label_keyword),
            supportingText = stringResource(R.string.bifid_keyword_hint),
        )
        SquareTable(state.square)
        LabeledSlider(
            label = if (state.period == BifidKey.WHOLE_MESSAGE) {
                stringResource(R.string.bifid_period_whole)
            } else {
                stringResource(R.string.bifid_period_label, state.period)
            },
            value = state.period,
            range = 0..BifidUiState.MAX_PERIOD,
            onValueChange = onPeriodChange,
        )
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
            supportingText = stringResource(R.string.bifid_input_hint),
        )
        ResultCard(
            label = stringResource(state.mode.outputLabelRes),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
        if (state.coordinates.isNotEmpty()) {
            CoordinatesTable(state.coordinates)
        }
    }
}

@Composable
private fun SquareTable(square: List<String>) {
    val headerColor = MaterialTheme.colorScheme.tertiary
    val letterColor = MaterialTheme.colorScheme.primary
    val numbers = square.indices.map { '1' + it }
    LetterTable(
        title = stringResource(R.string.bifid_square_title),
        rows = listOf(LetterRow.uniform(numbers, headerColor, label = " ")) +
            square.mapIndexed { index, row -> LetterRow.uniform(row.toList(), letterColor, label = numbers[index].toString()) },
    )
}

@Composable
private fun CoordinatesTable(coordinates: List<Coordinates>) {
    LetterTable(
        title = stringResource(R.string.bifid_coordinates_title),
        subtitle = stringResource(R.string.bifid_coordinates_hint),
        rows = listOf(
            LetterRow.uniform(coordinates.map { it.letter }, MaterialTheme.colorScheme.onSurface, stringResource(CoreUiR.string.label_plaintext)),
            LetterRow.uniform(coordinates.map { '0' + it.row }, MaterialTheme.colorScheme.primary, stringResource(R.string.bifid_row)),
            LetterRow.uniform(coordinates.map { '0' + it.column }, MaterialTheme.colorScheme.tertiary, stringResource(R.string.bifid_column)),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun BifidScreenPreview() {
    EncryptionTheme {
        BifidScreen(
            state = BifidUiState(input = "Flee at once").recalculated(),
            onInputChange = {},
            onKeywordChange = {},
            onPeriodChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
