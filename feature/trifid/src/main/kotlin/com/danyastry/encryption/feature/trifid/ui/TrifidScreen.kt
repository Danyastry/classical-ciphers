package com.danyastry.encryption.feature.trifid.ui

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
import com.danyastry.encryption.core.ui.components.LetterCell
import com.danyastry.encryption.core.ui.components.LetterRow
import com.danyastry.encryption.core.ui.components.LetterTable
import com.danyastry.encryption.core.ui.components.LettersField
import com.danyastry.encryption.core.ui.components.ModeSelector
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.inputLabelRes
import com.danyastry.encryption.core.ui.components.outputLabelRes
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.trifid.R
import com.danyastry.encryption.feature.trifid.TrifidKey

@Composable
fun TrifidRoute(
    modifier: Modifier = Modifier,
    viewModel: TrifidViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TrifidScreen(
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
fun TrifidScreen(
    state: TrifidUiState,
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
            supportingText = stringResource(R.string.trifid_keyword_hint),
        )
        CubeTable(state.layers)
        LabeledSlider(
            label = if (state.period == TrifidKey.WHOLE_MESSAGE) {
                stringResource(R.string.trifid_period_whole)
            } else {
                stringResource(R.string.trifid_period_label, state.period)
            },
            value = state.period,
            range = 0..TrifidUiState.MAX_PERIOD,
            onValueChange = onPeriodChange,
        )
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
            supportingText = stringResource(R.string.trifid_input_hint),
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
private fun CubeTable(layers: List<List<String>>) {
    val headerColor = MaterialTheme.colorScheme.tertiary
    val letterColor = MaterialTheme.colorScheme.primary
    val header = layers.indices.flatMap { layer -> listOf(' ', '1' + layer, ' ') + if (layer < layers.lastIndex) listOf(GAP) else emptyList() }
    LetterTable(
        title = stringResource(R.string.trifid_cube_title),
        subtitle = stringResource(R.string.trifid_cube_hint),
        rows = listOf(LetterRow.uniform(header, headerColor)) + (0 until layers.first().size).map { row ->
            LetterRow(
                cells = layers.flatMapIndexed { layer, rows ->
                    rows[row].map { LetterCell(it, letterColor) } +
                        if (layer < layers.lastIndex) listOf(LetterCell(GAP, letterColor)) else emptyList()
                },
            )
        },
    )
}

@Composable
private fun CoordinatesTable(coordinates: List<CubeCoordinates>) {
    LetterTable(
        title = stringResource(R.string.trifid_coordinates_title),
        subtitle = stringResource(R.string.trifid_coordinates_hint),
        rows = listOf(
            LetterRow.uniform(coordinates.map { it.letter }, MaterialTheme.colorScheme.onSurface, stringResource(CoreUiR.string.label_plaintext)),
            LetterRow.uniform(coordinates.map { '0' + it.layer }, MaterialTheme.colorScheme.secondary, stringResource(R.string.trifid_layer)),
            LetterRow.uniform(coordinates.map { '0' + it.row }, MaterialTheme.colorScheme.primary, stringResource(R.string.trifid_row)),
            LetterRow.uniform(coordinates.map { '0' + it.column }, MaterialTheme.colorScheme.tertiary, stringResource(R.string.trifid_column)),
        ),
    )
}

private const val GAP = ' '

@Preview(showBackground = true)
@Composable
private fun TrifidScreenPreview() {
    EncryptionTheme {
        TrifidScreen(
            state = TrifidUiState(input = "Aide-toi, le ciel t'aidera").recalculated(),
            onInputChange = {},
            onKeywordChange = {},
            onPeriodChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
