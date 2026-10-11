package com.danyastry.encryption.feature.railfence.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.ui.components.CipherMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RailFenceViewModel : ViewModel() {

    private val _state = MutableStateFlow(RailFenceUiState().recalculated())
    val state: StateFlow<RailFenceUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onRailsChange(rails: Int) = update {
        copy(rails = rails.coerceIn(RailFenceUiState.MIN_RAILS, RailFenceUiState.MAX_RAILS))
    }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: RailFenceUiState.() -> RailFenceUiState) {
        _state.update { it.transform().recalculated() }
    }
}
