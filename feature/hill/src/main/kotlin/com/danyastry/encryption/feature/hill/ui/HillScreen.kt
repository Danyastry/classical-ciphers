package com.danyastry.encryption.feature.hill.ui

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.core.ui.components.CipherTextField
import com.danyastry.encryption.core.ui.components.LettersField
import com.danyastry.encryption.core.ui.components.ModeSelector
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.SegmentedSelector
import com.danyastry.encryption.core.ui.components.inputLabelRes
import com.danyastry.encryption.core.ui.components.outputLabelRes
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.hill.HillKeyError
import com.danyastry.encryption.feature.hill.R

@Composable
fun HillRoute(
    modifier: Modifier = Modifier,
    viewModel: HillViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HillScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onKeyTextChange = viewModel::onKeyTextChange,
        onSizeChange = viewModel::onSizeChange,
        onRandomKey = viewModel::onRandomKey,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun HillScreen(
    state: HillUiState,
    onInputChange: (String) -> Unit,
    onKeyTextChange: (String) -> Unit,
    onSizeChange: (Int) -> Unit,
    onRandomKey: () -> Unit,
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
            options = HillViewModel.SIZES,
            selected = state.size,
            label = { stringResource(R.string.hill_size_option, it, it) },
            onSelect = onSizeChange,
        )
        LettersField(
            value = state.keyText,
            onValueChange = onKeyTextChange,
            label = stringResource(R.string.hill_key_label, state.size * state.size),
            isError = state.keyError != null,
            supportingText = keySupportingText(state),
        )
        OutlinedButton(onClick = onRandomKey, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.hill_random_key))
        }
        if (state.keyMatrix.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                MatrixCard(
                    title = stringResource(R.string.hill_key_matrix_title),
                    matrix = state.keyMatrix,
                    modifier = Modifier.weight(1f),
                )
                if (state.inverseMatrix.isNotEmpty()) {
                    MatrixCard(
                        title = stringResource(R.string.hill_inverse_matrix_title),
                        matrix = state.inverseMatrix,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
            isError = state.isIncompleteCiphertext,
            supportingText = stringResource(
                if (state.isIncompleteCiphertext) R.string.hill_error_incomplete else R.string.hill_input_hint,
                state.size,
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
private fun keySupportingText(state: HillUiState): String = when (state.keyError) {
    HillKeyError.WRONG_LENGTH -> stringResource(R.string.hill_error_wrong_length, state.size * state.size)
    HillKeyError.NOT_INVERTIBLE -> stringResource(R.string.hill_error_not_invertible, state.determinant ?: 0)
    null -> stringResource(R.string.hill_key_valid, state.determinant ?: 0)
}

@Composable
private fun MatrixCard(title: String, matrix: List<List<Int>>, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
            )
            matrix.forEach { row ->
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    row.forEach { value ->
                        Text(
                            text = value.toString(),
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.End,
                            modifier = Modifier.width(32.dp),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HillScreenPreview() {
    EncryptionTheme {
        HillScreen(
            state = HillUiState(input = "Short example").recalculated(),
            onInputChange = {},
            onKeyTextChange = {},
            onSizeChange = {},
            onRandomKey = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
