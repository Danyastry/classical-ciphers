package com.danyastry.encryption.feature.twosquare.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.feature.twosquare.SquareAlphabet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TwoSquareViewModel : ViewModel() {

    private val _state = MutableStateFlow(TwoSquareUiState().recalculated())
    val state: StateFlow<TwoSquareUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onFirstKeywordChange(keyword: String) = update { copy(firstKeyword = keyword.uppercase()) }

    fun onSecondKeywordChange(keyword: String) = update { copy(secondKeyword = keyword.uppercase()) }

    fun onAlphabetChange(alphabet: SquareAlphabet) = update { copy(alphabet = alphabet) }

    fun onUseOutputAsInput() = update { copy(input = output) }

    private fun update(transform: TwoSquareUiState.() -> TwoSquareUiState) {
        _state.update { it.transform().recalculated() }
    }
}
