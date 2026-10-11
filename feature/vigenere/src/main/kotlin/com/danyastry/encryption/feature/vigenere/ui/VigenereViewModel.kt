package com.danyastry.encryption.feature.vigenere.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.ui.components.CipherMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VigenereViewModel : ViewModel() {

    private val _state = MutableStateFlow(VigenereUiState().recalculated())
    val state: StateFlow<VigenereUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onKeywordChange(keyword: String) = update { copy(keyword = Keyword.normalize(keyword)) }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: VigenereUiState.() -> VigenereUiState) {
        _state.update { it.transform().recalculated() }
    }
}
