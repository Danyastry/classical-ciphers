package com.danyastry.encryption.feature.atbash.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.atbash.AtbashCipher
import com.danyastry.encryption.feature.atbash.R

@Composable
fun AtbashRoute(
    modifier: Modifier = Modifier,
    viewModel: AtbashViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AtbashScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun AtbashScreen(
    state: AtbashUiState,
    onInputChange: (String) -> Unit,
    onUseOutputAsInput: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MappingTable()
        OutlinedTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = { Text(stringResource(R.string.atbash_input_label)) },
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
        )
        ResultCard(output = state.output, onUseOutputAsInput = onUseOutputAsInput)
    }
}

private val alphabet = ('A'..'Z').toList()
private val mirrored = AtbashCipher().transform(alphabet.joinToString("")).toList()

@Composable
private fun MappingTable() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.atbash_mapping_title),
                style = MaterialTheme.typography.labelLarge,
            )
            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .horizontalScroll(rememberScrollState()),
            ) {
                MappingRow(alphabet, MaterialTheme.colorScheme.onSurface)
                MappingRow(mirrored, MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun MappingRow(letters: List<Char>, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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
private fun ResultCard(output: String, onUseOutputAsInput: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.atbash_output_label),
                style = MaterialTheme.typography.labelLarge,
            )
            SelectionContainer {
                Text(
                    text = output.ifEmpty { stringResource(R.string.atbash_output_placeholder) },
                    style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onUseOutputAsInput, enabled = output.isNotEmpty()) {
                    Text(stringResource(R.string.atbash_use_output_as_input))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AtbashScreenPreview() {
    EncryptionTheme {
        AtbashScreen(
            state = AtbashUiState(input = "Hello, World!", output = AtbashCipher().transform("Hello, World!")),
            onInputChange = {},
            onUseOutputAsInput = {},
        )
    }
}
