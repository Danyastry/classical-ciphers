package com.danyastry.encryption.feature.playfair.ui

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
import com.danyastry.encryption.feature.playfair.PlayfairKey
import com.danyastry.encryption.feature.playfair.PlayfairRule
import com.danyastry.encryption.feature.playfair.PlayfairStep
import com.danyastry.encryption.feature.playfair.R

@Composable
fun PlayfairRoute(
    modifier: Modifier = Modifier,
    viewModel: PlayfairViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    PlayfairScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onKeywordChange = viewModel::onKeywordChange,
        onModeChange = viewModel::onModeChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun PlayfairScreen(
    state: PlayfairUiState,
    onInputChange: (String) -> Unit,
    onKeywordChange: (String) -> Unit,
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
        LettersField(
            value = state.keyword,
            onValueChange = onKeywordChange,
            label = stringResource(CoreUiR.string.label_keyword),
            supportingText = stringResource(R.string.playfair_keyword_hint),
        )
        LetterTable(
            title = stringResource(R.string.playfair_square_title),
            rows = state.square.chunked(PlayfairKey.SIZE).map { row ->
                LetterRow.uniform(row.toList(), MaterialTheme.colorScheme.primary)
            },
        )
        CipherTextField(
            value = state.input,
            onValueChange = onInputChange,
            label = stringResource(state.mode.inputLabelRes),
            isError = state.isOddCiphertext,
            supportingText = if (state.isOddCiphertext) stringResource(R.string.playfair_error_odd) else null,
        )
        ResultCard(
            label = stringResource(state.mode.outputLabelRes),
            output = state.output,
            onUseOutputAsInput = onUseOutputAsInput,
        )
        if (state.steps.isNotEmpty()) {
            StepsCard(steps = state.steps, mode = state.mode)
        }
    }
}

@Composable
private fun StepsCard(steps: List<PlayfairStep>, mode: CipherMode) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.playfair_steps_title),
                style = MaterialTheme.typography.labelLarge,
            )
            steps.forEach { step ->
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = "${step.input} → ${step.output}",
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.width(96.dp),
                    )
                    Text(
                        text = stringResource(step.rule.labelRes(mode)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

private fun PlayfairRule.labelRes(mode: CipherMode): Int = when (this) {
    PlayfairRule.SAME_ROW -> if (mode == CipherMode.ENCRYPT) R.string.playfair_rule_same_row_right else R.string.playfair_rule_same_row_left
    PlayfairRule.SAME_COLUMN -> if (mode == CipherMode.ENCRYPT) R.string.playfair_rule_same_column_down else R.string.playfair_rule_same_column_up
    PlayfairRule.RECTANGLE -> R.string.playfair_rule_rectangle
}

@Preview(showBackground = true)
@Composable
private fun PlayfairScreenPreview() {
    EncryptionTheme {
        PlayfairScreen(
            state = PlayfairUiState(input = "Hide the gold in the tree stump").recalculated(),
            onInputChange = {},
            onKeywordChange = {},
            onModeChange = {},
            onUseOutputAsInput = {},
        )
    }
}
