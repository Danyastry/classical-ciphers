package com.danyastry.encryption.feature.substitution.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.classical.ALPHABET
import com.danyastry.encryption.core.classical.LetterFrequency
import com.danyastry.encryption.core.ui.R as CoreUiR
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.core.ui.components.CipherTextField
import com.danyastry.encryption.core.ui.components.LetterRow
import com.danyastry.encryption.core.ui.components.LetterTable
import com.danyastry.encryption.core.ui.components.LettersField
import com.danyastry.encryption.core.ui.components.ModeSelector
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.components.inputLabelRes
import com.danyastry.encryption.core.ui.components.outputLabelRes
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.substitution.KeyError
import com.danyastry.encryption.feature.substitution.R

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
        attackActions = FrequencyAttackActions(
            onLoadSample = viewModel::onLoadSample,
            onGuessKey = viewModel::onGuessKey,
            onSwapFirstChange = viewModel::onSwapFirstChange,
            onSwapSecondChange = viewModel::onSwapSecondChange,
            onSwap = viewModel::onSwap,
        ),
        modifier = modifier,
    )
}

data class FrequencyAttackActions(
    val onLoadSample: () -> Unit = {},
    val onGuessKey: () -> Unit = {},
    val onSwapFirstChange: (String) -> Unit = {},
    val onSwapSecondChange: (String) -> Unit = {},
    val onSwap: () -> Unit = {},
)

@Composable
fun SubstitutionScreen(
    state: SubstitutionUiState,
    onInputChange: (String) -> Unit,
    onKeywordChange: (String) -> Unit,
    onKeyTextChange: (String) -> Unit,
    onRandomKey: () -> Unit,
    onModeChange: (CipherMode) -> Unit,
    onUseOutputAsInput: () -> Unit,
    modifier: Modifier = Modifier,
    attackActions: FrequencyAttackActions = FrequencyAttackActions(),
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ModeSelector(selected = state.mode, onSelect = onModeChange)
        if (state.mode == CipherMode.DECRYPT) {
            OutlinedButton(onClick = attackActions.onLoadSample, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.substitution_load_sample))
            }
        }
        LettersField(
            value = state.keyword,
            onValueChange = onKeywordChange,
            label = stringResource(CoreUiR.string.label_keyword),
        )
        LettersField(
            value = state.keyText,
            onValueChange = onKeyTextChange,
            label = stringResource(R.string.substitution_key_label),
            isError = state.keyError != null,
            supportingText = stringResource(state.keyError?.messageRes ?: R.string.substitution_key_count),
        )
        OutlinedButton(onClick = onRandomKey, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.substitution_random_key))
        }
        LetterTable(
            title = stringResource(R.string.substitution_mapping_title),
            rows = listOf(
                LetterRow.uniform(ALPHABET.toList(), MaterialTheme.colorScheme.onSurface),
                LetterRow.uniform(ALPHABET.indices.map { state.keyText.getOrNull(it) ?: '·' }, MaterialTheme.colorScheme.primary),
            ),
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
        if (state.cipherLettersByFrequency.isNotEmpty()) {
            FrequencyAttackCard(state, attackActions)
        }
    }
}

@Composable
private fun FrequencyAttackCard(state: SubstitutionUiState, actions: FrequencyAttackActions) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = stringResource(R.string.substitution_attack_title),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = stringResource(R.string.substitution_attack_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            LetterTable(
                title = stringResource(R.string.substitution_attack_table),
                rows = listOf(
                    LetterRow.uniform(
                        state.cipherLettersByFrequency.toList(),
                        MaterialTheme.colorScheme.primary,
                        stringResource(R.string.substitution_attack_ciphertext),
                    ),
                    LetterRow.uniform(
                        LetterFrequency.englishByFrequency.toList(),
                        MaterialTheme.colorScheme.tertiary,
                        stringResource(R.string.substitution_attack_english),
                    ),
                ),
            )
            OutlinedButton(onClick = actions.onGuessKey, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.substitution_attack_guess))
            }
            Text(
                text = stringResource(R.string.substitution_attack_swap_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = state.swapFirst,
                    onValueChange = actions.onSwapFirstChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                    modifier = Modifier.width(SWAP_FIELD_WIDTH),
                )
                Text(text = stringResource(R.string.substitution_attack_with), style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = state.swapSecond,
                    onValueChange = actions.onSwapSecondChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                    modifier = Modifier.width(SWAP_FIELD_WIDTH),
                )
                Button(
                    onClick = actions.onSwap,
                    enabled = state.keyError == null && state.swapFirst.isNotEmpty() && state.swapSecond.isNotEmpty(),
                ) {
                    Text(stringResource(R.string.substitution_attack_swap))
                }
            }
        }
    }
}

private val SWAP_FIELD_WIDTH = 64.dp

private val KeyError.messageRes: Int
    get() = when (this) {
        KeyError.WRONG_LENGTH -> R.string.substitution_error_wrong_length
        KeyError.INVALID_CHARACTERS -> R.string.substitution_error_invalid_characters
        KeyError.DUPLICATE_LETTERS -> R.string.substitution_error_duplicate_letters
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
