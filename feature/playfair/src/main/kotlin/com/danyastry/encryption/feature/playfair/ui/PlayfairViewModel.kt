package com.danyastry.encryption.feature.playfair.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.ui.components.CipherMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlayfairViewModel : ViewModel() {

    private val _state = MutableStateFlow(PlayfairUiState().recalculated())
    val state: StateFlow<PlayfairUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onKeywordChange(keyword: String) = update { copy(keyword = keyword.uppercase()) }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: PlayfairUiState.() -> PlayfairUiState) {
        _state.update { it.transform().recalculated() }
    }
}
