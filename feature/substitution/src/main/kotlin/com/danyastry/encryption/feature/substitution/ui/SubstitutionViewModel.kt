package com.danyastry.encryption.feature.substitution.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.classical.ALPHABET
import com.danyastry.encryption.core.classical.ALPHABET_SIZE
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.substitution.FrequencyAttack
import com.danyastry.encryption.feature.substitution.SampleText
import com.danyastry.encryption.feature.substitution.SubstitutionCipher
import com.danyastry.encryption.feature.substitution.SubstitutionKey
import java.security.SecureRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SubstitutionViewModel(
    private val random: Random = SecureRandom().asKotlinRandom(),
) : ViewModel() {

    private val _state = MutableStateFlow(SubstitutionUiState().recalculated())
    val state: StateFlow<SubstitutionUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onKeywordChange(keyword: String) = update {
        copy(keyword = keyword, keyText = SubstitutionKey.fromKeyword(keyword).alphabet)
    }

    fun onKeyTextChange(keyText: String) = update {
        copy(keyword = "", keyText = keyText.uppercase().filter { it in 'A'..'Z' }.take(ALPHABET_SIZE))
    }

    fun onRandomKey() = update {
        copy(keyword = "", keyText = SubstitutionKey.random(random).alphabet)
    }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onGuessKey() = update { copy(keyword = "", keyText = FrequencyAttack.guessKey(input).alphabet) }

    fun onSwapFirstChange(letter: String) = update { copy(swapFirst = singleLetter(letter)) }

    fun onSwapSecondChange(letter: String) = update { copy(swapSecond = singleLetter(letter)) }

    fun onSwap() = update {
        if (keyError != null || swapFirst.isEmpty() || swapSecond.isEmpty()) return@update this
        copy(
            keyword = "",
            keyText = FrequencyAttack.swapPlaintextLetters(SubstitutionKey(keyText), swapFirst[0], swapSecond[0]).alphabet,
            swapFirst = "",
            swapSecond = "",
        )
    }

    fun onLoadSample() = update {
        val secret = SubstitutionKey.random(random)
        copy(
            mode = CipherMode.DECRYPT,
            input = SubstitutionCipher().encrypt(SampleText.FREQUENCY_ANALYSIS, secret),
            keyword = "",
            keyText = ALPHABET,
        )
    }

    private fun singleLetter(text: String): String = text.uppercase().filter { it in 'A'..'Z' }.takeLast(1)

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    private fun update(transform: SubstitutionUiState.() -> SubstitutionUiState) {
        _state.update { it.transform().recalculated() }
    }
}
