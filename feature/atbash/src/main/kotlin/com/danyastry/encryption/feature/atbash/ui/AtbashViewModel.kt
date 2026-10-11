package com.danyastry.encryption.feature.atbash.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.feature.atbash.AtbashCipher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AtbashViewModel : ViewModel() {

    private val cipher = AtbashCipher()

    private val _state = MutableStateFlow(AtbashUiState())
    val state: StateFlow<AtbashUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = setInput(input)

    fun onUseOutputAsInput() = setInput(_state.value.output)

    private fun setInput(input: String) {
        _state.update { it.copy(input = input, output = cipher.transform(input)) }
    }
}
