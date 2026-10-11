package com.danyastry.encryption.feature.polybius.ui

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
import com.danyastry.encryption.core.ui.components.LetterRow
import com.danyastry.encryption.core.ui.components.LetterTable
import com.danyastry.encryption.core.ui.components.LettersField
import com.danyastry.encryption.core.ui.components.ModeSelector
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.inputLabelRes
import com.danyastry.encryption.core.ui.components.outputLabelRes
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.polybius.R

@Composable
fun PolybiusRoute(
    modifier: Modifier = Modifier,
    viewModel: PolybiusViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    PolybiusScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onKeywordChange = viewModel::onKeywordChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun PolybiusScreen(
    state: PolybiusUiState,
    onInputChange: (String) -> Unit,
    onKeywordChange: (String) -> Unit,
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
            supportingText = stringResource(R.string.polybius_keyword_hint),
        )
        SquareTable(state.square)
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
            isError = state.isInvalidCode,
            supportingText = if (state.isInvalidCode) stringResource(R.string.polybius_error_code) else null,
        )
        ResultCard(
            label = stringResource(state.mode.outputLabelRes),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
    }
}

@Composable
private fun SquareTable(square: List<String>) {
    val headerColor = MaterialTheme.colorScheme.tertiary
    val letterColor = MaterialTheme.colorScheme.primary
    val numbers = square.indices.map { '1' + it }
    LetterTable(
        title = stringResource(R.string.polybius_square_title),
        subtitle = stringResource(R.string.polybius_square_hint),
        rows = listOf(LetterRow.uniform(numbers, headerColor, label = " ")) +
            square.mapIndexed { index, row -> LetterRow.uniform(row.toList(), letterColor, label = numbers[index].toString()) },
    )
}

@Preview(showBackground = true)
@Composable
private fun PolybiusScreenPreview() {
    EncryptionTheme {
        PolybiusScreen(
            state = PolybiusUiState(input = "Hello").recalculated(),
            onInputChange = {},
            onKeywordChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
