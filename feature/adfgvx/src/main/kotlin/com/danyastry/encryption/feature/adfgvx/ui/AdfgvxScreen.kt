package com.danyastry.encryption.feature.adfgvx.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.danyastry.encryption.feature.adfgvx.AdfgvxCipher
import com.danyastry.encryption.feature.adfgvx.R

@Composable
fun AdfgvxRoute(
    modifier: Modifier = Modifier,
    viewModel: AdfgvxViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AdfgvxScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onSquareKeywordChange = viewModel::onSquareKeywordChange,
        onRandomSquare = viewModel::onRandomSquare,
        onTranspositionChange = viewModel::onTranspositionChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun AdfgvxScreen(
    state: AdfgvxUiState,
    onInputChange: (String) -> Unit,
    onSquareKeywordChange: (String) -> Unit,
    onRandomSquare: () -> Unit,
    onTranspositionChange: (String) -> Unit,
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
            value = state.squareKeyword,
            onValueChange = onSquareKeywordChange,
            label = stringResource(R.string.adfgvx_square_keyword_label),
            supportingText = stringResource(R.string.adfgvx_square_keyword_hint),
        )
        OutlinedButton(onClick = onRandomSquare, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.adfgvx_random_square))
        }
        SquareTable(state.square)
        LettersField(
            value = state.transposition,
            onValueChange = onTranspositionChange,
            label = stringResource(R.string.adfgvx_transposition_label),
            isError = !state.isTranspositionValid,
            supportingText = stringResource(
                if (state.isTranspositionValid) R.string.adfgvx_transposition_hint else CoreUiR.string.error_letters_required,
            ),
        )
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
            isError = state.isInvalidCiphertext,
            supportingText = stringResource(
                when {
                    state.isInvalidCiphertext -> R.string.adfgvx_error_ciphertext
                    state.mode == CipherMode.ENCRYPT -> R.string.adfgvx_plaintext_hint
                    else -> R.string.adfgvx_ciphertext_hint
                },
            ),
        )
        if (state.fractionated.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.adfgvx_fractionated_label),
                        style = MaterialTheme.typography.labelLarge,
                    )
                    SelectionContainer {
                        Text(
                            text = state.fractionated,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                }
            }
        }
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
    LetterTable(
        title = stringResource(R.string.adfgvx_square_title),
        rows = listOf(LetterRow.uniform(AdfgvxCipher.LABELS.toList(), headerColor, label = " ")) +
            square.mapIndexed { index, row ->
                LetterRow.uniform(row.toList(), letterColor, label = AdfgvxCipher.LABELS[index].toString())
            },
    )
}

@Preview(showBackground = true)
@Composable
private fun AdfgvxScreenPreview() {
    EncryptionTheme {
        AdfgvxScreen(
            state = AdfgvxUiState(input = "Attack at 1200 am").recalculated(),
            onInputChange = {},
            onSquareKeywordChange = {},
            onRandomSquare = {},
            onTranspositionChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
