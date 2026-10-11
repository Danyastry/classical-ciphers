package com.danyastry.encryption.feature.scytale.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.ui.components.CipherMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScytaleViewModel : ViewModel() {

    private val _state = MutableStateFlow(ScytaleUiState().recalculated())
    val state: StateFlow<ScytaleUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onTurnsChange(turns: Int) = update {
        copy(turns = turns.coerceIn(ScytaleUiState.MIN_TURNS, ScytaleUiState.MAX_TURNS))
    }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: ScytaleUiState.() -> ScytaleUiState) {
        _state.update { it.transform().recalculated() }
    }
}
