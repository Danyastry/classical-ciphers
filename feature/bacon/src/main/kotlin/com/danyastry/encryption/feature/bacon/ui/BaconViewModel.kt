package com.danyastry.encryption.feature.bacon.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.bacon.BaconAlphabet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BaconViewModel : ViewModel() {

    private val _state = MutableStateFlow(BaconUiState().recalculated())
    val state: StateFlow<BaconUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onAlphabetChange(alphabet: BaconAlphabet) = update { copy(alphabet = alphabet) }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onDecodeSourceChange(source: DecodeSource) = update { copy(decodeSource = source) }

    fun onCoverChange(cover: String) = update { copy(cover = cover) }

    fun onUseOutputAsInput() = update {
        copy(input = output, mode = mode.toggled(), decodeSource = DecodeSource.AB_GROUPS)
    }

    fun onUseStegotextAsInput() = update {
        copy(input = stegotext, mode = CipherMode.DECRYPT, decodeSource = DecodeSource.LETTER_CASE)
    }

    private fun update(transform: BaconUiState.() -> BaconUiState) {
        _state.update { it.transform().recalculated() }
    }
}
