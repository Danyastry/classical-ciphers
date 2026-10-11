package com.danyastry.encryption.feature.affine.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.affine.AffineKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AffineViewModel : ViewModel() {

    private val _state = MutableStateFlow(AffineUiState().recalculated())
    val state: StateFlow<AffineUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onMultiplierChange(a: Int) {
        if (a !in AffineKey.VALID_MULTIPLIERS) return
        update { copy(key = key.copy(a = a)) }
    }

    fun onShiftChange(b: Int) = update {
        copy(key = key.copy(b = b.coerceIn(0, ALPHABET_SIZE - 1)))
    }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: AffineUiState.() -> AffineUiState) {
        _state.update { it.transform().recalculated() }
    }
}
