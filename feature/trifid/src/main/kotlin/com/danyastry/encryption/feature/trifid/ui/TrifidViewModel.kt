package com.danyastry.encryption.feature.trifid.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.ui.components.CipherMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TrifidViewModel : ViewModel() {

    private val _state = MutableStateFlow(TrifidUiState().recalculated())
    val state: StateFlow<TrifidUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onKeywordChange(keyword: String) = update { copy(keyword = keyword.uppercase()) }

    fun onPeriodChange(period: Int) = update { copy(period = period.coerceIn(0, TrifidUiState.MAX_PERIOD)) }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: TrifidUiState.() -> TrifidUiState) {
        _state.update { it.transform().recalculated() }
    }
}
