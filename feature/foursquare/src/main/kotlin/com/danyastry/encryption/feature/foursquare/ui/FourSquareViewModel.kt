package com.danyastry.encryption.feature.foursquare.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.foursquare.SquareAlphabet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FourSquareViewModel : ViewModel() {

    private val _state = MutableStateFlow(FourSquareUiState().recalculated())
    val state: StateFlow<FourSquareUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onFirstKeywordChange(keyword: String) = update { copy(firstKeyword = keyword.uppercase()) }

    fun onSecondKeywordChange(keyword: String) = update { copy(secondKeyword = keyword.uppercase()) }

    fun onAlphabetChange(alphabet: SquareAlphabet) = update { copy(alphabet = alphabet) }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: FourSquareUiState.() -> FourSquareUiState) {
        _state.update { it.transform().recalculated() }
    }
}
