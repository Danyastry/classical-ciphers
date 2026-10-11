package com.danyastry.encryption.feature.caesar.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.caesar.BruteForceCandidate
import com.danyastry.encryption.feature.caesar.CaesarCipher
import com.danyastry.encryption.feature.caesar.R
import kotlin.math.roundToInt

@Composable
fun CaesarRoute(
    modifier: Modifier = Modifier,
    viewModel: CaesarViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CaesarScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onShiftChange = viewModel::onShiftChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        onCandidateSelected = viewModel::onCandidateSelected,
        modifier = modifier,
    )
}

@Composable
fun CaesarScreen(
    state: CaesarUiState,
    onInputChange: (String) -> Unit,
    onShiftChange: (Int) -> Unit,
    onModeChange: (CaesarMode) -> Unit,
    onUseOutputAsInput: () -> Unit,
    onCandidateSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ModeSelector(selected = state.mode, onSelect = onModeChange)
        }
        item {
            ShiftSlider(shift = state.shift, onShiftChange = onShiftChange)
        }
        item {
            OutlinedTextField(
                value = state.input,
                onValueChange = onInputChange,
                label = { Text(stringResource(state.mode.inputLabelRes)) },
                minLines = 3,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            ResultCard(
                mode = state.mode,
                output = state.output,
                onUseOutputAsInput = onUseOutputAsInput,
            )
        }
        if (state.candidates.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.caesar_brute_force_title),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            items(state.candidates, key = { it.shift }) { candidate ->
                CandidateRow(
                    candidate = candidate,
                    isSelected = candidate.shift == state.shift,
                    onClick = { onCandidateSelected(candidate.shift) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeSelector(selected: CaesarMode, onSelect: (CaesarMode) -> Unit) {
    val modes = CaesarMode.entries
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
private fun ShiftSlider(shift: Int, onShiftChange: (Int) -> Unit) {
    Column {
        Text(
            text = stringResource(R.string.caesar_shift_label, shift),
            style = MaterialTheme.typography.titleSmall,
        )
        Slider(
            value = shift.toFloat(),
            onValueChange = { onShiftChange(it.roundToInt()) },
            valueRange = 0f..(CaesarCipher.ALPHABET_SIZE - 1).toFloat(),
            steps = CaesarCipher.ALPHABET_SIZE - 2,
        )
    }
}

@Composable
private fun ResultCard(mode: CaesarMode, output: String, onUseOutputAsInput: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(mode.outputLabelRes),
                style = MaterialTheme.typography.labelLarge,
            )
            SelectionContainer {
                Text(
                    text = output.ifEmpty { stringResource(R.string.caesar_output_placeholder) },
                    style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onUseOutputAsInput, enabled = output.isNotEmpty()) {
                    Text(stringResource(R.string.caesar_use_output_as_input))
                }
            }
        }
    }
}

@Composable
private fun CandidateRow(candidate: BruteForceCandidate, isSelected: Boolean, onClick: () -> Unit) {
    ListItem(
        leadingContent = {
            Text(
                text = candidate.shift.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        headlineContent = {
            Text(
                text = candidate.plaintext,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
            )
        },
        modifier = Modifier.clickable(onClick = onClick),
    )
}

private val CaesarMode.labelRes: Int
    get() = when (this) {
        CaesarMode.ENCRYPT -> R.string.caesar_mode_encrypt
        CaesarMode.DECRYPT -> R.string.caesar_mode_decrypt
    }

private val CaesarMode.inputLabelRes: Int
    get() = when (this) {
        CaesarMode.ENCRYPT -> R.string.caesar_input_plaintext
        CaesarMode.DECRYPT -> R.string.caesar_input_ciphertext
    }

private val CaesarMode.outputLabelRes: Int
    get() = when (this) {
        CaesarMode.ENCRYPT -> R.string.caesar_output_ciphertext
        CaesarMode.DECRYPT -> R.string.caesar_output_plaintext
    }

@Preview(showBackground = true)
@Composable
private fun CaesarScreenPreview() {
    EncryptionTheme {
        CaesarScreen(
            state = CaesarUiState(mode = CaesarMode.DECRYPT, input = "Khoor, Zruog!").recalculated(),
            onInputChange = {},
            onShiftChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
            onCandidateSelected = {},
        )
    }
}
