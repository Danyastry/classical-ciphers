package com.danyastry.encryption.feature.hill.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.hill.HillCipher
import com.danyastry.encryption.feature.hill.HillKey
import java.security.SecureRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HillViewModel(
    private val random: Random = SecureRandom().asKotlinRandom(),
) : ViewModel() {

    private val _state = MutableStateFlow(HillUiState().recalculated())
    val state: StateFlow<HillUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onKeyTextChange(keyText: String) = update {
        copy(keyText = HillCipher.normalize(keyText).take(size * size))
    }

    fun onSizeChange(size: Int) {
        if (size !in SIZES) return
        update {
            copy(size = size, keyText = if (size == 2) HillUiState.DEFAULT_KEY else HillUiState.DEFAULT_KEY_3X3)
        }
    }

    fun onRandomKey() = update { copy(keyText = HillKey.randomLetters(size, random)) }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: HillUiState.() -> HillUiState) {
        _state.update { it.transform().recalculated() }
    }

    companion object {
        val SIZES = listOf(2, 3)
    }
}
