package com.danyastry.encryption.feature.foursquare.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.core.ui.components.CipherTextField
import com.danyastry.encryption.core.ui.components.LetterCell
import com.danyastry.encryption.core.ui.components.LetterRow
import com.danyastry.encryption.core.ui.components.LetterTable
import com.danyastry.encryption.core.ui.components.LettersField
import com.danyastry.encryption.core.ui.components.ModeSelector
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.SegmentedSelector
import com.danyastry.encryption.core.ui.components.inputLabelRes
import com.danyastry.encryption.core.ui.components.outputLabelRes
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.foursquare.R
import com.danyastry.encryption.feature.foursquare.SquareAlphabet

@Composable
fun FourSquareRoute(
    modifier: Modifier = Modifier,
    viewModel: FourSquareViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    FourSquareScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onFirstKeywordChange = viewModel::onFirstKeywordChange,
        onSecondKeywordChange = viewModel::onSecondKeywordChange,
        onAlphabetChange = viewModel::onAlphabetChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun FourSquareScreen(
    state: FourSquareUiState,
    onInputChange: (String) -> Unit,
    onFirstKeywordChange: (String) -> Unit,
    onSecondKeywordChange: (String) -> Unit,
    onAlphabetChange: (SquareAlphabet) -> Unit,
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
        SegmentedSelector(
            options = SquareAlphabet.entries,
            selected = state.alphabet,
            label = { stringResource(it.labelRes) },
            onSelect = onAlphabetChange,
        )
        LettersField(
            value = state.firstKeyword,
            onValueChange = onFirstKeywordChange,
            label = stringResource(R.string.foursquare_first_keyword),
        )
        LettersField(
            value = state.secondKeyword,
            onValueChange = onSecondKeywordChange,
            label = stringResource(R.string.foursquare_second_keyword),
        )
        SquaresTable(state)
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
            isError = state.isOddCiphertext,
            supportingText = stringResource(
                if (state.isOddCiphertext) R.string.foursquare_error_odd else R.string.foursquare_input_hint,
            ),
        )
        ResultCard(
            label = stringResource(state.mode.outputLabelRes),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
    }
}

@Composable
private fun SquaresTable(state: FourSquareUiState) {
    if (state.plainSquare.isEmpty()) return
    val plainColor = MaterialTheme.colorScheme.onSurfaceVariant
    val keyedColor = MaterialTheme.colorScheme.primary
    val top = state.plainSquare.indices.map { row ->
        joinedRow(state.plainSquare[row], plainColor, state.upperRightSquare[row], keyedColor)
    }
    val bottom = state.plainSquare.indices.map { row ->
        joinedRow(state.lowerLeftSquare[row], keyedColor, state.plainSquare[row], plainColor)
    }
    val spacer = LetterRow.uniform(List(state.plainSquare.size * 2 + 1) { GAP }, plainColor)
    LetterTable(
        title = stringResource(R.string.foursquare_squares_title),
        subtitle = stringResource(R.string.foursquare_squares_hint),
        rows = top + spacer + bottom,
    )
}

private fun joinedRow(left: String, leftColor: Color, right: String, rightColor: Color) = LetterRow(
    cells = left.map { LetterCell(it, leftColor) } + LetterCell(GAP, leftColor) + right.map { LetterCell(it, rightColor) },
)

private const val GAP = ' '

private val SquareAlphabet.labelRes: Int
    get() = when (this) {
        SquareAlphabet.MERGE_I_J -> R.string.foursquare_alphabet_merge
        SquareAlphabet.OMIT_Q -> R.string.foursquare_alphabet_omit_q
    }

@Preview(showBackground = true)
@Composable
private fun FourSquareScreenPreview() {
    EncryptionTheme {
        FourSquareScreen(
            state = FourSquareUiState(input = "Help me Obi-Wan Kenobi").recalculated(),
            onInputChange = {},
            onFirstKeywordChange = {},
            onSecondKeywordChange = {},
            onAlphabetChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
