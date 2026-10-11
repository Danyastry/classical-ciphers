package com.danyastry.encryption.feature.rot.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.rot.R
import com.danyastry.encryption.feature.rot.RotVariant

@Composable
fun RotRoute(
    modifier: Modifier = Modifier,
    viewModel: RotViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    RotScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onVariantChange = viewModel::onVariantChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun RotScreen(
    state: RotUiState,
    onInputChange: (String) -> Unit,
    onVariantChange: (RotVariant) -> Unit,
    onUseOutputAsInput: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        VariantSelector(selected = state.variant, onSelect = onVariantChange)
        Text(
            text = stringResource(state.variant.descriptionRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = { Text(stringResource(R.string.rot_input_label)) },
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
        )
        ResultCard(output = state.output, onUseOutputAsInput = onUseOutputAsInput)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VariantSelector(selected: RotVariant, onSelect: (RotVariant) -> Unit) {
    val variants = RotVariant.entries
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        variants.forEachIndexed { index, variant ->
            SegmentedButton(
                selected = variant == selected,
                onClick = { onSelect(variant) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = variants.size),
            ) {
                Text(stringResource(variant.labelRes))
            }
        }
    }
}

@Composable
private fun ResultCard(output: String, onUseOutputAsInput: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.rot_output_label),
                style = MaterialTheme.typography.labelLarge,
            )
            SelectionContainer {
                Text(
                    text = output.ifEmpty { stringResource(R.string.rot_output_placeholder) },
                    style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onUseOutputAsInput, enabled = output.isNotEmpty()) {
                    Text(stringResource(R.string.rot_use_output_as_input))
                }
            }
        }
    }
}

private val RotVariant.labelRes: Int
    get() = when (this) {
        RotVariant.ROT13 -> R.string.rot_variant_rot13
        RotVariant.ROT47 -> R.string.rot_variant_rot47
    }

private val RotVariant.descriptionRes: Int
    get() = when (this) {
        RotVariant.ROT13 -> R.string.rot_description_rot13
        RotVariant.ROT47 -> R.string.rot_description_rot47
    }

@Preview(showBackground = true)
@Composable
private fun RotScreenPreview() {
    EncryptionTheme {
        RotScreen(
            state = RotUiState(variant = RotVariant.ROT47, input = "Hello, World!").recalculated(),
            onInputChange = {},
            onVariantChange = {},
            onUseOutputAsInput = {},
        )
    }
}
