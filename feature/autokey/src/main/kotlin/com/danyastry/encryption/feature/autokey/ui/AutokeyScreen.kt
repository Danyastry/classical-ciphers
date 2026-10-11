package com.danyastry.encryption.feature.autokey.ui

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
import com.danyastry.encryption.feature.autokey.R

@Composable
fun AutokeyRoute(
    modifier: Modifier = Modifier,
    viewModel: AutokeyViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AutokeyScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onPrimerChange = viewModel::onPrimerChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun AutokeyScreen(
    state: AutokeyUiState,
    onInputChange: (String) -> Unit,
    onPrimerChange: (String) -> Unit,
    onModeChange: (AutokeyMode) -> Unit,
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
            value = state.primer,
            onValueChange = onPrimerChange,
            label = { Text(stringResource(R.string.autokey_primer_label)) },
            singleLine = true,
            isError = !state.isPrimerValid,
            supportingText = {
                Text(
                    stringResource(
                        if (state.isPrimerValid) R.string.autokey_primer_hint else R.string.autokey_primer_error,
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
private fun ModeSelector(selected: AutokeyMode, onSelect: (AutokeyMode) -> Unit) {
    val modes = AutokeyMode.entries
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
private fun AlignmentTable(mode: AutokeyMode, alignment: List<AlignedLetter>) {
    val primerColor = MaterialTheme.colorScheme.tertiary
    val plaintextKeyColor = MaterialTheme.colorScheme.secondary
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.autokey_alignment_title),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = stringResource(R.string.autokey_alignment_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Column {
                    RowLabel(stringResource(mode.inputLabelRes))
                    RowLabel(stringResource(R.string.autokey_alignment_key))
                    RowLabel(stringResource(mode.outputLabelRes))
                }
                Column(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    LetterRow(alignment.map { it.input to MaterialTheme.colorScheme.onSurface })
                    LetterRow(alignment.map { it.key to if (it.isPrimer) primerColor else plaintextKeyColor })
                    LetterRow(alignment.map { it.output to MaterialTheme.colorScheme.primary })
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
private fun LetterRow(letters: List<Pair<Char, Color>>) {
    Row {
        letters.forEach { (letter, color) ->
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
private fun ResultCard(mode: AutokeyMode, output: String, onUseOutputAsInput: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(mode.outputLabelRes),
                style = MaterialTheme.typography.labelLarge,
            )
            SelectionContainer {
                Text(
                    text = output.ifEmpty { stringResource(R.string.autokey_output_placeholder) },
                    style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onUseOutputAsInput, enabled = output.isNotEmpty()) {
                    Text(stringResource(R.string.autokey_use_output_as_input))
                }
            }
        }
    }
}

private val AutokeyMode.labelRes: Int
    get() = when (this) {
        AutokeyMode.ENCRYPT -> R.string.autokey_mode_encrypt
        AutokeyMode.DECRYPT -> R.string.autokey_mode_decrypt
    }

private val AutokeyMode.inputLabelRes: Int
    get() = when (this) {
        AutokeyMode.ENCRYPT -> R.string.autokey_plaintext
        AutokeyMode.DECRYPT -> R.string.autokey_ciphertext
    }

private val AutokeyMode.outputLabelRes: Int
    get() = when (this) {
        AutokeyMode.ENCRYPT -> R.string.autokey_ciphertext
        AutokeyMode.DECRYPT -> R.string.autokey_plaintext
    }

@Preview(showBackground = true)
@Composable
private fun AutokeyScreenPreview() {
    EncryptionTheme {
        AutokeyScreen(
            state = AutokeyUiState(input = "Attack at dawn").recalculated(),
            onInputChange = {},
            onPrimerChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
