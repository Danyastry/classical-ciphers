package com.danyastry.encryption.feature.vigenere.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.vigenere.R

@Composable
fun VigenereRoute(
    modifier: Modifier = Modifier,
    viewModel: VigenereViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    VigenereScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onKeywordChange = viewModel::onKeywordChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun VigenereScreen(
    state: VigenereUiState,
    onInputChange: (String) -> Unit,
    onKeywordChange: (String) -> Unit,
    onModeChange: (VigenereMode) -> Unit,
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
        OutlinedTextField(
            value = state.keyword,
            onValueChange = onKeywordChange,
            label = { Text(stringResource(R.string.vigenere_keyword_label)) },
            singleLine = true,
            isError = !state.isKeywordValid,
            supportingText = {
                Text(
                    stringResource(
                        if (state.isKeywordValid) R.string.vigenere_keyword_hint else R.string.vigenere_keyword_error,
                    ),
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = { Text(stringResource(state.mode.inputLabelRes)) },
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
        )
        ResultCard(
            mode = state.mode,
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
        if (state.alignment.isNotEmpty()) {
            AlignmentTable(mode = state.mode, alignment = state.alignment)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeSelector(selected: VigenereMode, onSelect: (VigenereMode) -> Unit) {
    val modes = VigenereMode.entries
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        modes.forEachIndexed { index, mode ->
            SegmentedButton(
                selected = mode == selected,
                onClick = { onSelect(mode) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = modes.size),
            ) {
                Text(stringResource(mode.labelRes))
            }
        }
    }
}

@Composable
private fun AlignmentTable(mode: VigenereMode, alignment: List<AlignedLetter>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.vigenere_alignment_title),
                style = MaterialTheme.typography.labelLarge,
            )
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Column {
                    RowLabel(stringResource(mode.inputLabelRes))
                    RowLabel(stringResource(R.string.vigenere_alignment_key))
                    RowLabel(stringResource(mode.outputLabelRes))
                }
                Column(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    LetterRow(alignment.map { it.input }, MaterialTheme.colorScheme.onSurface)
                    LetterRow(alignment.map { it.key }, MaterialTheme.colorScheme.tertiary)
                    LetterRow(alignment.map { it.output }, MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun RowLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(end = 12.dp),
    )
}

@Composable
private fun LetterRow(letters: List<Char>, color: Color) {
    Row {
        letters.forEach { letter ->
            Text(
                text = letter.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = color,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(20.dp),
            )
        }
    }
}

@Composable
private fun ResultCard(mode: VigenereMode, output: String, onUseOutputAsInput: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(mode.outputLabelRes),
                style = MaterialTheme.typography.labelLarge,
            )
            SelectionContainer {
                Text(
                    text = output.ifEmpty { stringResource(R.string.vigenere_output_placeholder) },
                    style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onUseOutputAsInput, enabled = output.isNotEmpty()) {
                    Text(stringResource(R.string.vigenere_use_output_as_input))
                }
            }
        }
    }
}

private val VigenereMode.labelRes: Int
    get() = when (this) {
        VigenereMode.ENCRYPT -> R.string.vigenere_mode_encrypt
        VigenereMode.DECRYPT -> R.string.vigenere_mode_decrypt
    }

private val VigenereMode.inputLabelRes: Int
    get() = when (this) {
        VigenereMode.ENCRYPT -> R.string.vigenere_plaintext
        VigenereMode.DECRYPT -> R.string.vigenere_ciphertext
    }

private val VigenereMode.outputLabelRes: Int
    get() = when (this) {
        VigenereMode.ENCRYPT -> R.string.vigenere_ciphertext
        VigenereMode.DECRYPT -> R.string.vigenere_plaintext
    }

@Preview(showBackground = true)
@Composable
private fun VigenereScreenPreview() {
    EncryptionTheme {
        VigenereScreen(
            state = VigenereUiState(input = "Attack at dawn").recalculated(),
            onInputChange = {},
            onKeywordChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
