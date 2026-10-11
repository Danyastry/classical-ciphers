package com.danyastry.encryption.feature.adfgvx.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.classical.PolybiusSquare
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.adfgvx.AdfgvxKey
import java.security.SecureRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AdfgvxViewModel(
    private val random: Random = SecureRandom().asKotlinRandom(),
) : ViewModel() {

    private val _state = MutableStateFlow(AdfgvxUiState().recalculated())
    val state: StateFlow<AdfgvxUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onSquareKeywordChange(keyword: String) = update {
        copy(squareKeyword = PolybiusSquare.normalize(keyword, PolybiusSquare.ALPHANUMERIC_36).take(PolybiusSquare.ALPHANUMERIC_36.length))
    }

    fun onRandomSquare() = update { copy(squareKeyword = AdfgvxKey.randomSquareKeyword(random)) }

    fun onTranspositionChange(keyword: String) = update { copy(transposition = Keyword.normalize(keyword)) }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: AdfgvxUiState.() -> AdfgvxUiState) {
        _state.update { it.transform().recalculated() }
    }
}
