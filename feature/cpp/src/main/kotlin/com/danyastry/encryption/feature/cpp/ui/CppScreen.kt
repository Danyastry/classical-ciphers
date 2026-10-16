package com.danyastry.encryption.feature.cpp.ui

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import com.danyastry.encryption.core.ui.components.SegmentedSelector
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.feature.cpp.BenchmarkCipher
import com.danyastry.encryption.feature.cpp.CipherBenchmark
import com.danyastry.encryption.feature.cpp.CipherSet
import com.danyastry.encryption.feature.cpp.Comparison
import com.danyastry.encryption.feature.cpp.InputSize
import com.danyastry.encryption.feature.cpp.R
import com.danyastry.encryption.feature.cpp.Verdict
import java.util.Locale

@Composable
fun CppRoute(kotlinCiphers: CipherSet, modifier: Modifier = Modifier) {
    val viewModel: CppViewModel = viewModel { CppViewModel(kotlinCiphers, abi = Build.SUPPORTED_ABIS.first()) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    CppScreen(
        state = state,
        onCipherChange = viewModel::onCipherChange,
        onSizeChange = viewModel::onSizeChange,
        onCompare = viewModel::onCompare,
        onMeasureOverhead = viewModel::onMeasureOverhead,
        modifier = modifier,
    )
}

@Composable
fun CppScreen(
    state: CppUiState,
    onCipherChange: (BenchmarkCipher) -> Unit,
    onSizeChange: (InputSize) -> Unit,
    onCompare: () -> Unit,
    onMeasureOverhead: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.cpp_intro),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (state.abi.isNotEmpty()) {
            Text(
                text = stringResource(R.string.cpp_abi, state.abi),
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
            )
        }
        SegmentedSelector(
            options = BenchmarkCipher.entries,
            selected = state.cipher,
            label = { stringResource(it.labelRes) },
            onSelect = onCipherChange,
        )
        BenchmarkCard(
            title = stringResource(R.string.cpp_compare_title),
            hint = stringResource(R.string.cpp_compare_hint, CipherBenchmark.RUNS, CipherBenchmark.WARMUPS),
            comparison = state.comparison,
        ) {
            SegmentedSelector(
                options = InputSize.entries,
                selected = state.size,
                label = { stringResource(it.labelRes) },
                onSelect = onSizeChange,
            )
            Button(onClick = onCompare, enabled = !state.isRunning, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(if (state.isRunning) R.string.cpp_running else R.string.cpp_compare))
            }
        }
        BenchmarkCard(
            title = stringResource(R.string.cpp_overhead_title),
            hint = stringResource(R.string.cpp_overhead_hint, CipherBenchmark.TINY_INPUT_CHARS, CipherBenchmark.OVERHEAD_CALLS),
            comparison = state.overhead,
        ) {
            OutlinedButton(onClick = onMeasureOverhead, enabled = !state.isRunning, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(if (state.isRunning) R.string.cpp_running else R.string.cpp_overhead))
            }
        }
        Text(
            text = stringResource(R.string.cpp_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun BenchmarkCard(title: String, hint: String, comparison: Comparison?, controls: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            controls()
            comparison?.let { ComparisonResult(it) }
        }
    }
}

@Composable
private fun ComparisonResult(comparison: Comparison) {
    val mono = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)
    Text(text = stringResource(R.string.cpp_kotlin_time, formatNanos(comparison.kotlinNanos)), style = mono)
    Text(text = stringResource(R.string.cpp_cpp_time, formatNanos(comparison.cppNanos)), style = mono)
    Text(
        text = when (comparison.verdict) {
            Verdict.CPP_FASTER -> stringResource(R.string.cpp_cpp_faster, formatRatio(comparison.cppSpeedup))
            Verdict.KOTLIN_FASTER -> stringResource(R.string.cpp_kotlin_faster, formatRatio(1 / comparison.cppSpeedup))
            Verdict.ABOUT_THE_SAME -> stringResource(R.string.cpp_about_the_same)
        },
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
    )
    Text(
        text = stringResource(if (comparison.outputsMatch) R.string.cpp_outputs_match else R.string.cpp_outputs_differ),
        style = MaterialTheme.typography.bodyMedium,
        color = if (comparison.outputsMatch) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
    )
}

internal fun formatNanos(nanos: Long): String = String.format(Locale.US, "%.3f ms", nanos / 1_000_000.0)

internal fun formatRatio(ratio: Double): String = String.format(Locale.US, "%.1f", ratio)

private val BenchmarkCipher.labelRes: Int
    get() = when (this) {
        BenchmarkCipher.CAESAR -> R.string.cpp_cipher_caesar
        BenchmarkCipher.VIGENERE -> R.string.cpp_cipher_vigenere
        BenchmarkCipher.XOR -> R.string.cpp_cipher_xor
    }

private val InputSize.labelRes: Int
    get() = when (this) {
        InputSize.SMALL -> R.string.cpp_size_small
        InputSize.MEDIUM -> R.string.cpp_size_medium
        InputSize.LARGE -> R.string.cpp_size_large
    }

@Preview(showBackground = true)
@Composable
private fun CppScreenPreview() {
    EncryptionTheme {
        CppScreen(
            state = CppUiState(
                abi = "arm64-v8a",
                comparison = Comparison(kotlinNanos = 2_400_000, cppNanos = 610_000, outputsMatch = true),
                overhead = Comparison(kotlinNanos = 3_100_000, cppNanos = 9_800_000, outputsMatch = true),
            ),
            onCipherChange = {},
            onSizeChange = {},
            onCompare = {},
            onMeasureOverhead = {},
        )
    }
}
