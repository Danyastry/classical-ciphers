package com.danyastry.encryption.feature.onetimepad.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.onetimepad.OneTimePadCipher
import java.security.SecureRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OneTimePadViewModel(
    private val random: Random = SecureRandom().asKotlinRandom(),
) : ViewModel() {

    private val _state = MutableStateFlow(OneTimePadUiState().recalculated())
    val state: StateFlow<OneTimePadUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onKeyChange(key: String) = update { copy(key = Keyword.normalize(key)) }

    fun onGenerateKey() = update {
        copy(key = OneTimePadCipher.randomKey(lettersNeeded.coerceAtLeast(1), random))
    }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onAlternativeChange(plaintext: String) = update { copy(alternativePlaintext = plaintext) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: OneTimePadUiState.() -> OneTimePadUiState) {
        _state.update { it.transform().recalculated() }
    }
}
