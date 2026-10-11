package com.danyastry.encryption.feature.columnar.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.ui.components.CipherMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ColumnarViewModel : ViewModel() {

    private val _state = MutableStateFlow(ColumnarUiState().recalculated())
    val state: StateFlow<ColumnarUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onKeywordChange(keyword: String) = update { copy(keyword = Keyword.normalize(keyword)) }

    fun onPadWithXChange(padWithX: Boolean) = update { copy(padWithX = padWithX) }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: ColumnarUiState.() -> ColumnarUiState) {
        _state.update { it.transform().recalculated() }
    }
}
