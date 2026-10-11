package com.danyastry.encryption.feature.affine.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.affine.AffineKey
import com.danyastry.encryption.feature.affine.R
import kotlin.math.roundToInt

@Composable
fun AffineRoute(
    modifier: Modifier = Modifier,
    viewModel: AffineViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AffineScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onMultiplierChange = viewModel::onMultiplierChange,
        onShiftChange = viewModel::onShiftChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun AffineScreen(
    state: AffineUiState,
    onInputChange: (String) -> Unit,
    onMultiplierChange: (Int) -> Unit,
    onShiftChange: (Int) -> Unit,
    onModeChange: (AffineMode) -> Unit,
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
        MultiplierSelector(selected = state.key.a, onSelect = onMultiplierChange)
        ShiftSlider(shift = state.key.b, onShiftChange = onShiftChange)
        Text(
            text = stringResource(state.mode.formulaRes, state.key.a, state.key.b, state.key.inverseOfA),
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeSelector(selected: AffineMode, onSelect: (AffineMode) -> Unit) {
    val modes = AffineMode.entries
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MultiplierSelector(selected: Int, onSelect: (Int) -> Unit) {
    Column {
        Text(
            text = stringResource(R.string.affine_multiplier_label, selected),
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = stringResource(R.string.affine_multiplier_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AffineKey.VALID_MULTIPLIERS.forEach { a ->
                FilterChip(
                    selected = a == selected,
                    onClick = { onSelect(a) },
                    label = { Text(a.toString()) },
                )
            }
        }
    }
}

@Composable
private fun ShiftSlider(shift: Int, onShiftChange: (Int) -> Unit) {
    Column {
        Text(
            text = stringResource(R.string.affine_shift_label, shift),
            style = MaterialTheme.typography.titleSmall,
        )
        Slider(
            value = shift.toFloat(),
            onValueChange = { onShiftChange(it.roundToInt()) },
            valueRange = 0f..(AffineKey.ALPHABET_SIZE - 1).toFloat(),
            steps = AffineKey.ALPHABET_SIZE - 2,
        )
    }
}

@Composable
private fun ResultCard(mode: AffineMode, output: String, onUseOutputAsInput: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(mode.outputLabelRes),
                style = MaterialTheme.typography.labelLarge,
            )
            SelectionContainer {
                Text(
                    text = output.ifEmpty { stringResource(R.string.affine_output_placeholder) },
                    style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onUseOutputAsInput, enabled = output.isNotEmpty()) {
                    Text(stringResource(R.string.affine_use_output_as_input))
                }
            }
        }
    }
}

private val AffineMode.labelRes: Int
    get() = when (this) {
        AffineMode.ENCRYPT -> R.string.affine_mode_encrypt
        AffineMode.DECRYPT -> R.string.affine_mode_decrypt
    }

private val AffineMode.formulaRes: Int
    get() = when (this) {
        AffineMode.ENCRYPT -> R.string.affine_formula_encrypt
        AffineMode.DECRYPT -> R.string.affine_formula_decrypt
    }

private val AffineMode.inputLabelRes: Int
    get() = when (this) {
        AffineMode.ENCRYPT -> R.string.affine_input_plaintext
        AffineMode.DECRYPT -> R.string.affine_input_ciphertext
    }

private val AffineMode.outputLabelRes: Int
    get() = when (this) {
        AffineMode.ENCRYPT -> R.string.affine_output_ciphertext
        AffineMode.DECRYPT -> R.string.affine_output_plaintext
    }

@Preview(showBackground = true)
@Composable
private fun AffineScreenPreview() {
    EncryptionTheme {
        AffineScreen(
            state = AffineUiState(input = "Affine cipher").recalculated(),
            onInputChange = {},
            onMultiplierChange = {},
            onShiftChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
