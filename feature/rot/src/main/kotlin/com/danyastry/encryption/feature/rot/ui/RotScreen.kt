package com.danyastry.encryption.feature.rot.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.ui.R as CoreUiR
import com.danyastry.encryption.core.ui.components.CipherTextField
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.SegmentedSelector
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
        SegmentedSelector(
            options = RotVariant.entries,
            selected = state.variant,
            label = { stringResource(it.labelRes) },
            onSelect = onVariantChange,
        )
        Text(
            text = stringResource(state.variant.descriptionRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
