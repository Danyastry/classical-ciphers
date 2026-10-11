package com.danyastry.encryption.feature.affine.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
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
import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.core.ui.components.CipherTextField
import com.danyastry.encryption.core.ui.components.LabeledSlider
import com.danyastry.encryption.core.ui.components.ModeSelector
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.inputLabelRes
import com.danyastry.encryption.core.ui.components.outputLabelRes
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.affine.AffineKey
import com.danyastry.encryption.feature.affine.R

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
        MultiplierSelector(selected = state.key.a, onSelect = onMultiplierChange)
        LabeledSlider(
            label = stringResource(R.string.affine_shift_label, state.key.b),
            value = state.key.b,
            range = 0 until ALPHABET_SIZE,
            onValueChange = onShiftChange,
        )
        Text(
            text = stringResource(state.mode.formulaRes, state.key.a, state.key.b, state.key.inverseOfA),
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
        )
        ResultCard(
            label = stringResource(state.mode.outputLabelRes),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
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

private val CipherMode.formulaRes: Int
    get() = when (this) {
        CipherMode.ENCRYPT -> R.string.affine_formula_encrypt
        CipherMode.DECRYPT -> R.string.affine_formula_decrypt
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
