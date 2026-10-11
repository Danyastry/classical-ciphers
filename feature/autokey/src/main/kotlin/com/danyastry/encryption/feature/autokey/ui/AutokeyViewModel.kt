package com.danyastry.encryption.feature.autokey.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.classical.Keyword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AutokeyViewModel : ViewModel() {

    private val _state = MutableStateFlow(AutokeyUiState().recalculated())
    val state: StateFlow<AutokeyUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onPrimerChange(primer: String) = update { copy(primer = Keyword.normalize(primer)) }

    fun onModeChange(mode: AutokeyMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update {
        copy(
            input = output,
            mode = if (mode == AutokeyMode.ENCRYPT) AutokeyMode.DECRYPT else AutokeyMode.ENCRYPT,
        )
    }

    private fun update(transform: AutokeyUiState.() -> AutokeyUiState) {
        _state.update { it.transform().recalculated() }
    }
}
