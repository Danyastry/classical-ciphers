package com.danyastry.encryption.feature.bifid.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.ui.components.CipherMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BifidViewModel : ViewModel() {

    private val _state = MutableStateFlow(BifidUiState().recalculated())
    val state: StateFlow<BifidUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onKeywordChange(keyword: String) = update { copy(keyword = keyword.uppercase()) }

    fun onPeriodChange(period: Int) = update { copy(period = period.coerceIn(0, BifidUiState.MAX_PERIOD)) }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: BifidUiState.() -> BifidUiState) {
        _state.update { it.transform().recalculated() }
    }
}
