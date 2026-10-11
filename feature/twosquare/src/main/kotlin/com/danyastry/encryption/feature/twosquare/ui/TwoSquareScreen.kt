package com.danyastry.encryption.feature.twosquare.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import com.danyastry.encryption.core.ui.R as CoreUiR
import com.danyastry.encryption.core.ui.components.CipherTextField
import com.danyastry.encryption.core.ui.components.LetterRow
import com.danyastry.encryption.core.ui.components.LetterTable
import com.danyastry.encryption.core.ui.components.LettersField
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.SegmentedSelector
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.twosquare.R
import com.danyastry.encryption.feature.twosquare.SquareAlphabet
import com.danyastry.encryption.feature.twosquare.TwoSquareStep

@Composable
fun TwoSquareRoute(
    modifier: Modifier = Modifier,
    viewModel: TwoSquareViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TwoSquareScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onFirstKeywordChange = viewModel::onFirstKeywordChange,
        onSecondKeywordChange = viewModel::onSecondKeywordChange,
        onAlphabetChange = viewModel::onAlphabetChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun TwoSquareScreen(
    state: TwoSquareUiState,
    onInputChange: (String) -> Unit,
    onFirstKeywordChange: (String) -> Unit,
    onSecondKeywordChange: (String) -> Unit,
    onAlphabetChange: (SquareAlphabet) -> Unit,
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
            text = stringResource(R.string.twosquare_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SegmentedSelector(
            options = SquareAlphabet.entries,
            selected = state.alphabet,
            label = { stringResource(it.labelRes) },
            onSelect = onAlphabetChange,
        )
        LettersField(
            value = state.firstKeyword,
            onValueChange = onFirstKeywordChange,
            label = stringResource(R.string.twosquare_top_keyword),
        )
        LettersField(
            value = state.secondKeyword,
            onValueChange = onSecondKeywordChange,
            label = stringResource(R.string.twosquare_bottom_keyword),
        )
        SquaresTable(state)
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(CoreUiR.string.label_text),
            supportingText = stringResource(R.string.twosquare_input_hint),
        )
        ResultCard(
            label = stringResource(CoreUiR.string.label_result),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
        if (state.steps.isNotEmpty()) {
            StepsCard(state)
        }
    }
}

@Composable
private fun SquaresTable(state: TwoSquareUiState) {
    if (state.topSquare.isEmpty()) return
    val color = MaterialTheme.colorScheme.primary
    val spacer = LetterRow.uniform(List(state.topSquare.first().length) { ' ' }, color)
    LetterTable(
        title = stringResource(R.string.twosquare_squares_title),
        rows = state.topSquare.map { LetterRow.uniform(it.toList(), color) } +
            spacer +
            state.bottomSquare.map { LetterRow.uniform(it.toList(), color) },
    )
}

@Composable
private fun StepsCard(state: TwoSquareUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.twosquare_steps_title),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = stringResource(R.string.twosquare_transparency_summary, state.transparencies, state.pairs),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            state.steps.forEach { step -> StepRow(step) }
        }
    }
}

@Composable
private fun StepRow(step: TwoSquareStep) {
    Row(modifier = Modifier.padding(top = 4.dp)) {
        Text(
            text = "${step.input} → ${step.output}",
            fontFamily = FontFamily.Monospace,
            color = if (step.isTransparency) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(96.dp),
        )
        if (step.isTransparency) {
            Text(
                text = stringResource(R.string.twosquare_transparency),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

private val SquareAlphabet.labelRes: Int
    get() = when (this) {
        SquareAlphabet.MERGE_I_J -> R.string.twosquare_alphabet_merge
        SquareAlphabet.OMIT_Q -> R.string.twosquare_alphabet_omit_q
    }

@Preview(showBackground = true)
@Composable
private fun TwoSquareScreenPreview() {
    EncryptionTheme {
        TwoSquareScreen(
            state = TwoSquareUiState(input = "Help me Obi-Wan Kenobi").recalculated(),
            onInputChange = {},
            onFirstKeywordChange = {},
            onSecondKeywordChange = {},
            onAlphabetChange = {},
            onUseOutputAsInput = {},
        )
    }
}
