package com.danyastry.encryption.feature.atbash.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danyastry.encryption.core.classical.ALPHABET
import com.danyastry.encryption.core.ui.R as CoreUiR
import com.danyastry.encryption.core.ui.components.CipherTextField
import com.danyastry.encryption.core.ui.components.LetterRow
import com.danyastry.encryption.core.ui.components.LetterTable
import com.danyastry.encryption.core.ui.components.ResultCard
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.atbash.AtbashCipher
import com.danyastry.encryption.feature.atbash.R

@Composable
fun AtbashRoute(
    modifier: Modifier = Modifier,
    viewModel: AtbashViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AtbashScreen(
        state = state,
        onInputChange = viewModel::onInputChange,
        onUseOutputAsInput = viewModel::onUseOutputAsInput,
        modifier = modifier,
    )
}

private val mirroredAlphabet = AtbashCipher().transform(ALPHABET)

@Composable
fun AtbashScreen(
    state: AtbashUiState,
    onInputChange: (String) -> Unit,
    onUseOutputAsInput: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LetterTable(
            title = stringResource(R.string.atbash_mapping_title),
            rows = listOf(
                LetterRow.uniform(ALPHABET.toList(), MaterialTheme.colorScheme.onSurface),
                LetterRow.uniform(mirroredAlphabet.toList(), MaterialTheme.colorScheme.primary),
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
    }
}

@Preview(showBackground = true)
@Composable
private fun AtbashScreenPreview() {
    EncryptionTheme {
        AtbashScreen(
            state = AtbashUiState(input = "Hello, World!", output = AtbashCipher().transform("Hello, World!")),
            onInputChange = {},
            onUseOutputAsInput = {},
        )
    }
}
