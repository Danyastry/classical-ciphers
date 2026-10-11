package com.danyastry.encryption.feature.rot.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.feature.rot.RotVariant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RotViewModel : ViewModel() {

    private val _state = MutableStateFlow(RotUiState())
    val state: StateFlow<RotUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onVariantChange(variant: RotVariant) = update { copy(variant = variant) }

    fun onUseOutputAsInput() = update { copy(input = output) }

    private fun update(transform: RotUiState.() -> RotUiState) {
        _state.update { it.transform().recalculated() }
    }
}
