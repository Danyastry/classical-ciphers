package com.danyastry.encryption.feature.cpp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danyastry.encryption.feature.cpp.BenchmarkCipher
import com.danyastry.encryption.feature.cpp.CipherBenchmark
import com.danyastry.encryption.feature.cpp.CipherSet
import com.danyastry.encryption.feature.cpp.InputSize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CppViewModel(
    private val kotlinCiphers: CipherSet,
    private val cppCiphers: CipherSet = CipherSet.Native,
    private val benchmark: CipherBenchmark = CipherBenchmark(),
    abi: String = "",
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    private val _state = MutableStateFlow(CppUiState(abi = abi))
    val state: StateFlow<CppUiState> = _state.asStateFlow()

    fun onCipherChange(cipher: BenchmarkCipher) {
        _state.update { it.copy(cipher = cipher, comparison = null) }
    }

    fun onSizeChange(size: InputSize) {
        _state.update { it.copy(size = size, comparison = null) }
    }

    fun onCompare() = benchmarkInBackground { state ->
        val comparison = benchmark.compare(state.cipher, state.size, kotlinCiphers, cppCiphers)
        _state.update { it.copy(comparison = comparison) }
    }

    fun onMeasureOverhead() = benchmarkInBackground {
        val overhead = benchmark.callOverhead(CipherBenchmark.OVERHEAD_CALLS, kotlinCiphers, cppCiphers)
        _state.update { it.copy(overhead = overhead) }
    }

    private fun benchmarkInBackground(work: (CppUiState) -> Unit) {
        if (_state.value.isRunning) return
        val snapshot = _state.value
        _state.update { it.copy(isRunning = true) }
        viewModelScope.launch {
            withContext(dispatcher) { work(snapshot) }
            _state.update { it.copy(isRunning = false) }
        }
    }
}
