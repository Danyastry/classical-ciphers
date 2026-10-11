package com.danyastry.encryption.feature.substitution.ui

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
import androidx.compose.material3.OutlinedButton
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
import com.danyastry.encryption.feature.substitution.KeyError
import com.danyastry.encryption.feature.substitution.R
import com.danyastry.encryption.feature.substitution.SubstitutionKey

@Composable
fun SubstitutionRoute(
    modifier: Modifier = Modifier,
    viewModel: SubstitutionViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SubstitutionScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onKeywordChange = viewModel::onKeywordChange,
        onKeyTextChange = viewModel::onKeyTextChange,
        onRandomKey = viewModel::onRandomKey,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun SubstitutionScreen(
    state: SubstitutionUiState,
    onInputChange: (String) -> Unit,
    onKeywordChange: (String) -> Unit,
    onKeyTextChange: (String) -> Unit,
    onRandomKey: () -> Unit,
    onModeChange: (SubstitutionMode) -> Unit,
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
            label = { Text(stringResource(R.string.substitution_keyword_label)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.keyText,
            onValueChange = onKeyTextChange,
            label = { Text(stringResource(R.string.substitution_key_label)) },
            singleLine = true,
            isError = state.keyError != null,
            supportingText = {
                Text(
                    state.keyError?.let { stringResource(it.messageRes) }
                        ?: stringResource(R.string.substitution_key_count),
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedButton(onClick = onRandomKey, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.substitution_random_key))
        }
        MappingTable(keyText = state.keyText)
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeSelector(selected: SubstitutionMode, onSelect: (SubstitutionMode) -> Unit) {
    val modes = SubstitutionMode.entries
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
private fun MappingTable(keyText: String) {
    val keyLetters = SubstitutionKey.PLAIN_ALPHABET.indices.map { keyText.getOrNull(it) ?: '·' }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.substitution_mapping_title),
                style = MaterialTheme.typography.labelLarge,
            )
            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .horizontalScroll(rememberScrollState()),
            ) {
                MappingRow(SubstitutionKey.PLAIN_ALPHABET.toList(), MaterialTheme.colorScheme.onSurface)
                MappingRow(keyLetters, MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun MappingRow(letters: List<Char>, color: Color) {
    Row {
        letters.forEach { letter ->
            Text(
                text = letter.toString(),
                color = color,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(20.dp),
            )
        }
    }
}

@Composable
private fun ResultCard(mode: SubstitutionMode, output: String, onUseOutputAsInput: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(mode.outputLabelRes),
                style = MaterialTheme.typography.labelLarge,
            )
            SelectionContainer {
                Text(
                    text = output.ifEmpty { stringResource(R.string.substitution_output_placeholder) },
                    style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onUseOutputAsInput, enabled = output.isNotEmpty()) {
                    Text(stringResource(R.string.substitution_use_output_as_input))
                }
            }
        }
    }
}

private val KeyError.messageRes: Int
    get() = when (this) {
        KeyError.WRONG_LENGTH -> R.string.substitution_error_wrong_length
        KeyError.INVALID_CHARACTERS -> R.string.substitution_error_invalid_characters
        KeyError.DUPLICATE_LETTERS -> R.string.substitution_error_duplicate_letters
    }

private val SubstitutionMode.labelRes: Int
    get() = when (this) {
        SubstitutionMode.ENCRYPT -> R.string.substitution_mode_encrypt
        SubstitutionMode.DECRYPT -> R.string.substitution_mode_decrypt
    }

private val SubstitutionMode.inputLabelRes: Int
    get() = when (this) {
        SubstitutionMode.ENCRYPT -> R.string.substitution_input_plaintext
        SubstitutionMode.DECRYPT -> R.string.substitution_input_ciphertext
    }

private val SubstitutionMode.outputLabelRes: Int
    get() = when (this) {
        SubstitutionMode.ENCRYPT -> R.string.substitution_output_ciphertext
        SubstitutionMode.DECRYPT -> R.string.substitution_output_plaintext
    }

@Preview(showBackground = true)
@Composable
private fun SubstitutionScreenPreview() {
    EncryptionTheme {
        SubstitutionScreen(
            state = SubstitutionUiState(input = "Flee at once. We are discovered!").recalculated(),
            onInputChange = {},
            onKeywordChange = {},
            onKeyTextChange = {},
            onRandomKey = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
