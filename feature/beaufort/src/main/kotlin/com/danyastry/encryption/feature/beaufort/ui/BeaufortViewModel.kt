package com.danyastry.encryption.feature.beaufort.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.feature.beaufort.BeaufortKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BeaufortViewModel : ViewModel() {

    private val _state = MutableStateFlow(BeaufortUiState().recalculated())
    val state: StateFlow<BeaufortUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onKeywordChange(keyword: String) = update { copy(keyword = BeaufortKey.normalize(keyword)) }

    fun onUseOutputAsInput() = update { copy(input = output) }

    private fun update(transform: BeaufortUiState.() -> BeaufortUiState) {
        _state.update { it.transform().recalculated() }
    }
}
