package com.danyastry.encryption.feature.beaufort.ui

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.danyastry.encryption.feature.beaufort.R

@Composable
fun BeaufortRoute(
    modifier: Modifier = Modifier,
    viewModel: BeaufortViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BeaufortScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onKeywordChange = viewModel::onKeywordChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun BeaufortScreen(
    state: BeaufortUiState,
    onInputChange: (String) -> Unit,
    onKeywordChange: (String) -> Unit,
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
            text = stringResource(R.string.beaufort_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedTextField(
            value = state.keyword,
            onValueChange = onKeywordChange,
            label = { Text(stringResource(R.string.beaufort_keyword_label)) },
            singleLine = true,
            isError = !state.isKeywordValid,
            supportingText = {
                Text(
                    stringResource(
                        if (state.isKeywordValid) R.string.beaufort_keyword_hint else R.string.beaufort_keyword_error,
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
            label = { Text(stringResource(R.string.beaufort_input_label)) },
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
        )
        ResultCard(output = state.output, onUseOutputAsInput = onUseOutputAsInput)
        if (state.alignment.isNotEmpty()) {
            AlignmentTable(alignment = state.alignment)
        }
    }
}

@Composable
private fun AlignmentTable(alignment: List<AlignedLetter>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.beaufort_alignment_title),
                style = MaterialTheme.typography.labelLarge,
            )
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Column {
                    RowLabel(stringResource(R.string.beaufort_alignment_text))
                    RowLabel(stringResource(R.string.beaufort_alignment_key))
                    RowLabel(stringResource(R.string.beaufort_alignment_result))
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
private fun ResultCard(output: String, onUseOutputAsInput: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.beaufort_output_label),
                style = MaterialTheme.typography.labelLarge,
            )
            SelectionContainer {
                Text(
                    text = output.ifEmpty { stringResource(R.string.beaufort_output_placeholder) },
                    style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onUseOutputAsInput, enabled = output.isNotEmpty()) {
                    Text(stringResource(R.string.beaufort_use_output_as_input))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BeaufortScreenPreview() {
    EncryptionTheme {
        BeaufortScreen(
            state = BeaufortUiState(input = "Defend the east wall").recalculated(),
            onInputChange = {},
            onKeywordChange = {},
            onUseOutputAsInput = {},
        )
    }
}
