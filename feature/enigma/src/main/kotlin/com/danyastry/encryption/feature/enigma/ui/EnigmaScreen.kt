package com.danyastry.encryption.feature.enigma.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.ui.R as CoreUiR
import com.danyastry.encryption.core.ui.components.CipherTextField
import com.danyastry.encryption.core.ui.components.LettersField
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.SegmentedSelector
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.enigma.PlugboardError
import com.danyastry.encryption.feature.enigma.R
import com.danyastry.encryption.feature.enigma.Reflector
import com.danyastry.encryption.feature.enigma.Rotor
import com.danyastry.encryption.feature.enigma.Stage

@Composable
fun EnigmaRoute(
    modifier: Modifier = Modifier,
    viewModel: EnigmaViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    EnigmaScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onRotorChange = viewModel::onRotorChange,
        onReflectorChange = viewModel::onReflectorChange,
        onRingsChange = viewModel::onRingsChange,
        onPositionsChange = viewModel::onPositionsChange,
        onPlugboardChange = viewModel::onPlugboardChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        onLoadBarbarossa = viewModel::onLoadBarbarossa,
        modifier = modifier,
    )
}

@Composable
fun EnigmaScreen(
    state: EnigmaUiState,
    onInputChange: (String) -> Unit,
    onRotorChange: (Int, Rotor) -> Unit,
    onReflectorChange: (Reflector) -> Unit,
    onRingsChange: (String) -> Unit,
    onPositionsChange: (String) -> Unit,
    onPlugboardChange: (String) -> Unit,
    onUseOutputAsInput: () -> Unit,
    onLoadBarbarossa: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.enigma_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedButton(onClick = onLoadBarbarossa, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.enigma_load_barbarossa))
        }
        Section(stringResource(R.string.enigma_reflector)) {
            SegmentedSelector(
                options = Reflector.entries,
                selected = state.reflector,
                label = { it.name },
                onSelect = onReflectorChange,
            )
        }
        ROTOR_LABELS.forEachIndexed { slot, labelRes ->
            Section(stringResource(labelRes)) {
                SegmentedSelector(
                    options = Rotor.entries,
                    selected = state.rotors[slot],
                    label = { it.name },
                    onSelect = { onRotorChange(slot, it) },
                )
            }
        }
        LettersField(
            value = state.rings,
            onValueChange = onRingsChange,
            label = stringResource(R.string.enigma_rings),
            isError = !state.isRingsValid,
            supportingText = stringResource(if (state.isRingsValid) R.string.enigma_rings_hint else R.string.enigma_error_three_letters),
        )
        LettersField(
            value = state.positions,
            onValueChange = onPositionsChange,
            label = stringResource(R.string.enigma_positions),
            isError = !state.isPositionsValid,
            supportingText = stringResource(if (state.isPositionsValid) R.string.enigma_positions_hint else R.string.enigma_error_three_letters),
        )
        LettersField(
            value = state.plugboard,
            onValueChange = onPlugboardChange,
            label = stringResource(R.string.enigma_plugboard),
            isError = state.plugboardError != null,
            supportingText = stringResource(state.plugboardError?.messageRes ?: R.string.enigma_plugboard_hint),
        )
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(CoreUiR.string.label_text),
        )
        ResultCard(
            label = stringResource(CoreUiR.string.label_result),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
        if (state.lastPath.isNotEmpty()) {
            PathCard(state)
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleSmall)
        content()
    }
}

@Composable
private fun PathCard(state: EnigmaUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.enigma_window_after, state.finalWindow),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = stringResource(R.string.enigma_path_title),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 12.dp),
            )
            state.lastPath.forEach { (stage, letter) ->
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = letter.toString(),
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(24.dp),
                    )
                    Text(
                        text = stringResource(stage.labelRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

private val ROTOR_LABELS = listOf(R.string.enigma_rotor_left, R.string.enigma_rotor_middle, R.string.enigma_rotor_right)

private val PlugboardError.messageRes: Int
    get() = when (this) {
        PlugboardError.ODD_LETTERS -> R.string.enigma_error_plugboard_odd
        PlugboardError.REPEATED_LETTER -> R.string.enigma_error_plugboard_repeated
    }

private val Stage.labelRes: Int
    get() = when (this) {
        Stage.KEY -> R.string.enigma_stage_key
        Stage.PLUGBOARD_IN -> R.string.enigma_stage_plugboard_in
        Stage.RIGHT_IN -> R.string.enigma_stage_right_in
        Stage.MIDDLE_IN -> R.string.enigma_stage_middle_in
        Stage.LEFT_IN -> R.string.enigma_stage_left_in
        Stage.REFLECTOR -> R.string.enigma_stage_reflector
        Stage.LEFT_OUT -> R.string.enigma_stage_left_out
        Stage.MIDDLE_OUT -> R.string.enigma_stage_middle_out
        Stage.RIGHT_OUT -> R.string.enigma_stage_right_out
        Stage.LAMP -> R.string.enigma_stage_lamp
    }

@Preview(showBackground = true)
@Composable
private fun EnigmaScreenPreview() {
    EncryptionTheme {
        EnigmaScreen(
            state = EnigmaUiState(input = "Hello world").recalculated(),
            onInputChange = {},
            onRotorChange = { _, _ -> },
            onReflectorChange = {},
            onRingsChange = {},
            onPositionsChange = {},
            onPlugboardChange = {},
            onUseOutputAsInput = {},
            onLoadBarbarossa = {},
        )
    }
}
