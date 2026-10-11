package com.danyastry.encryption.feature.xor.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.xor.ByteEncoding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class XorViewModel : ViewModel() {

    private val _state = MutableStateFlow(XorUiState().recalculated())
    val state: StateFlow<XorUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onKeyChange(key: String) = update { copy(key = key) }

    fun onKeyFormatChange(format: KeyFormat) = update { copy(keyFormat = format) }

    fun onEncodingChange(encoding: ByteEncoding) = update { copy(encoding = encoding) }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onCribChange(crib: String) = update { copy(crib = crib) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: XorUiState.() -> XorUiState) {
        _state.update { it.transform().recalculated() }
    }
}
