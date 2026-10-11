package com.danyastry.encryption.feature.caesar.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CaesarViewModel : ViewModel() {

    private val _state = MutableStateFlow(CaesarUiState().recalculated())
    val state: StateFlow<CaesarUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onShiftChange(shift: Int) = update {
        copy(shift = shift.coerceIn(0, ALPHABET_SIZE - 1))
    }

    fun onModeChange(mode: CaesarMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update {
        copy(
            input = output,
            mode = if (mode == CaesarMode.ENCRYPT) CaesarMode.DECRYPT else CaesarMode.ENCRYPT,
        )
    }

    fun onCandidateSelected(shift: Int) = update {
        copy(shift = shift, mode = CaesarMode.DECRYPT)
    }

    private fun update(transform: CaesarUiState.() -> CaesarUiState) {
        _state.update { it.transform().recalculated() }
    }
}
