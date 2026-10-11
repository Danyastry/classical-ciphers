package com.danyastry.encryption.feature.beaufort.ui

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
import com.danyastry.encryption.core.ui.components.LetterRow
import com.danyastry.encryption.core.ui.components.LetterTable
import com.danyastry.encryption.core.ui.components.LettersField
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.beaufort.R

@Composable
fun BeaufortRoute(
    modifier: Modifier = Modifier,
    viewModel: BeaufortViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BeaufortScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onKeywordChange = viewModel::onKeywordChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

@Composable
fun BeaufortScreen(
    state: BeaufortUiState,
    onInputChange: (String) -> Unit,
    onKeywordChange: (String) -> Unit,
    onUseOutputAsInput: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.beaufort_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        LettersField(
            value = state.keyword,
            onValueChange = onKeywordChange,
            label = stringResource(CoreUiR.string.label_keyword),
            isError = !state.isKeywordValid,
            supportingText = stringResource(
                if (state.isKeywordValid) R.string.beaufort_keyword_hint else CoreUiR.string.error_letters_required,
            ),
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
        if (state.alignment.isNotEmpty()) {
            LetterTable(
                title = stringResource(R.string.beaufort_alignment_title),
                rows = listOf(
                    LetterRow.uniform(state.alignment.map { it.input }, MaterialTheme.colorScheme.onSurface, stringResource(CoreUiR.string.label_text)),
                    LetterRow.uniform(state.alignment.map { it.key }, MaterialTheme.colorScheme.tertiary, stringResource(CoreUiR.string.label_key)),
                    LetterRow.uniform(state.alignment.map { it.output }, MaterialTheme.colorScheme.primary, stringResource(CoreUiR.string.label_result)),
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BeaufortScreenPreview() {
    EncryptionTheme {
        BeaufortScreen(
            state = BeaufortUiState(input = "Defend the east wall").recalculated(),
            onInputChange = {},
            onKeywordChange = {},
            onUseOutputAsInput = {},
        )
    }
}
