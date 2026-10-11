package com.danyastry.encryption.feature.vigenere.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import com.danyastry.encryption.feature.vigenere.KasiskiResult
import com.danyastry.encryption.feature.vigenere.R

@Composable
fun VigenereRoute(
    modifier: Modifier = Modifier,
    viewModel: VigenereViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    VigenereScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onKeywordChange = viewModel::onKeywordChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        onLoadSample = viewModel::onLoadSample,
        onUseCrackedKey = viewModel::onUseCrackedKey,
        modifier = modifier,
    )
}

@Composable
fun VigenereScreen(
    state: VigenereUiState,
    onInputChange: (String) -> Unit,
    onKeywordChange: (String) -> Unit,
    onModeChange: (CipherMode) -> Unit,
    onUseOutputAsInput: () -> Unit,
    modifier: Modifier = Modifier,
    onLoadSample: () -> Unit = {},
    onUseCrackedKey: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ModeSelector(selected = state.mode, onSelect = onModeChange)
        if (state.mode == CipherMode.DECRYPT) {
            OutlinedButton(onClick = onLoadSample, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.vigenere_load_sample))
            }
        }
        LettersField(
            value = state.keyword,
            onValueChange = onKeywordChange,
            label = stringResource(CoreUiR.string.label_keyword),
            isError = !state.isKeywordValid,
            supportingText = stringResource(
                if (state.isKeywordValid) R.string.vigenere_keyword_hint else CoreUiR.string.error_letters_required,
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
        state.kasiski?.let { result ->
            KasiskiCard(result = result, isKeyInUse = result.key == state.keyword, onUseCrackedKey = onUseCrackedKey)
        }
        if (state.alignment.isNotEmpty()) {
            LetterTable(
                title = stringResource(R.string.vigenere_alignment_title),
                rows = listOf(
                    LetterRow.uniform(state.alignment.map { it.input }, MaterialTheme.colorScheme.onSurface, stringResource(state.mode.inputLabelRes)),
                    LetterRow.uniform(state.alignment.map { it.key }, MaterialTheme.colorScheme.tertiary, stringResource(CoreUiR.string.label_key)),
                    LetterRow.uniform(state.alignment.map { it.output }, MaterialTheme.colorScheme.primary, stringResource(state.mode.outputLabelRes)),
                ),
            )
        }
    }
}

@Composable
private fun KasiskiCard(result: KasiskiResult, isKeyInUse: Boolean, onUseCrackedKey: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = stringResource(R.string.vigenere_kasiski_title),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = stringResource(R.string.vigenere_kasiski_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (result.repeats.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.vigenere_kasiski_repeats),
                    style = MaterialTheme.typography.labelLarge,
                )
                result.repeats.take(MAX_REPEATS_SHOWN).forEach { repeat ->
                    Text(
                        text = stringResource(R.string.vigenere_kasiski_repeat, repeat.sequence, repeat.distance),
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Text(
                    text = stringResource(R.string.vigenere_kasiski_factors),
                    style = MaterialTheme.typography.labelLarge,
                )
                FactorBars(result)
            }
            Text(
                text = stringResource(R.string.vigenere_kasiski_result, result.keyLength, result.key),
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
            )
            Button(onClick = onUseCrackedKey, enabled = !isKeyInUse, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.vigenere_kasiski_use_key))
            }
        }
    }
}

@Composable
private fun FactorBars(result: KasiskiResult) {
    val highest = result.factorCounts.values.maxOrNull()?.coerceAtLeast(1) ?: return
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        result.factorCounts.forEach { (length, count) ->
            val isChosen = length == result.keyLength
            val color = if (isChosen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = length.toString(),
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.width(24.dp),
                )
                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .width(BAR_MAX_WIDTH * count / highest)
                        .background(color),
                )
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private const val MAX_REPEATS_SHOWN = 5
private val BAR_MAX_WIDTH = 160.dp

@Preview(showBackground = true)
@Composable
private fun VigenereScreenPreview() {
    EncryptionTheme {
        VigenereScreen(
            state = VigenereUiState(input = "Attack at dawn").recalculated(),
            onInputChange = {},
            onKeywordChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
