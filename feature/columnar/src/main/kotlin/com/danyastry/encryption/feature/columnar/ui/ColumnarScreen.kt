package com.danyastry.encryption.feature.columnar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.danyastry.encryption.feature.columnar.R

@Composable
fun ColumnarRoute(
    modifier: Modifier = Modifier,
    viewModel: ColumnarViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ColumnarScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onKeywordChange = viewModel::onKeywordChange,
        onPadWithXChange = viewModel::onPadWithXChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun ColumnarScreen(
    state: ColumnarUiState,
    onInputChange: (String) -> Unit,
    onKeywordChange: (String) -> Unit,
    onPadWithXChange: (Boolean) -> Unit,
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
            isError = !state.isKeywordValid,
            supportingText = stringResource(
                if (state.isKeywordValid) R.string.columnar_keyword_hint else CoreUiR.string.error_letters_required,
            ),
        )
        if (state.mode == CipherMode.ENCRYPT) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.columnar_pad_label),
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        text = stringResource(R.string.columnar_pad_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(checked = state.padWithX, onCheckedChange = onPadWithXChange)
            }
        }
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
        if (state.grid.isNotEmpty()) {
            GridTable(state)
        }
    }
}

@Composable
private fun GridTable(state: ColumnarUiState) {
    val headerColor = MaterialTheme.colorScheme.tertiary
    val letterColor = MaterialTheme.colorScheme.primary
    val emptyColor = MaterialTheme.colorScheme.outlineVariant
    LetterTable(
        title = stringResource(R.string.columnar_grid_title),
        subtitle = stringResource(R.string.columnar_reading_order, state.readingOrder),
        rows = listOf(LetterRow.uniform(state.keyword.toList(), headerColor)) + state.grid.map { row ->
            LetterRow(
                cells = state.keyword.indices.map { column ->
                    when (val char = row.getOrNull(column)) {
                        null -> LetterCell(EMPTY_CELL, emptyColor)
                        ' ' -> LetterCell(SPACE_CELL, letterColor)
                        else -> LetterCell(char, letterColor)
                    }
                },
            )
        },
    )
}

private const val EMPTY_CELL = '·'
private const val SPACE_CELL = '␣'

@Preview(showBackground = true)
@Composable
private fun ColumnarScreenPreview() {
    EncryptionTheme {
        ColumnarScreen(
            state = ColumnarUiState(input = "WEAREDISCOVEREDFLEEATONCE").recalculated(),
            onInputChange = {},
            onKeywordChange = {},
            onPadWithXChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
