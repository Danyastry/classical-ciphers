package com.danyastry.encryption.feature.caesar.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
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
import com.danyastry.encryption.feature.caesar.BruteForceCandidate
import com.danyastry.encryption.feature.caesar.R

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
    onModeChange: (CipherMode) -> Unit,
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
            LabeledSlider(
                label = stringResource(R.string.caesar_shift_label, state.shift),
                value = state.shift,
                range = 0 until ALPHABET_SIZE,
                onValueChange = onShiftChange,
            )
        }
        item {
            CipherTextField(
                value = state.input,
                onValueChange = onInputChange,
                label = stringResource(state.mode.inputLabelRes),
            )
        }
        item {
            ResultCard(
                label = stringResource(state.mode.outputLabelRes),
                output = state.output,
                onUseOutputAsInput = onUseOutputAsInput,
            )
        }
        if (state.candidates.isNotEmpty()) {
            item {
                Column {
                    Text(
                        text = stringResource(R.string.caesar_brute_force_title),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResource(R.string.caesar_brute_force_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            items(state.candidates, key = { it.shift }) { candidate ->
                CandidateRow(
                    candidate = candidate,
                    isSelected = candidate.shift == state.shift,
                    isMostLikely = candidate.shift == state.mostLikelyShift,
                    onClick = { onCandidateSelected(candidate.shift) },
                )
            }
        }
    }
}

@Composable
private fun CandidateRow(
    candidate: BruteForceCandidate,
    isSelected: Boolean,
    isMostLikely: Boolean,
    onClick: () -> Unit,
) {
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
        trailingContent = if (isMostLikely) {
            {
                Text(
                    text = stringResource(R.string.caesar_most_likely),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        } else {
            null
        },
        modifier = Modifier.clickable(onClick = onClick),
    )
}

@Preview(showBackground = true)
@Composable
private fun CaesarScreenPreview() {
    EncryptionTheme {
        CaesarScreen(
            state = CaesarUiState(mode = CipherMode.DECRYPT, input = "Khoor, Zruog!").recalculated(),
            onInputChange = {},
            onShiftChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
            onCandidateSelected = {},
        )
    }
}
