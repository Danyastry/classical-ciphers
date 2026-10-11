package com.danyastry.encryption.feature.vigenere.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.core.classical.ALPHABET
import com.danyastry.encryption.core.classical.Keyword
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.vigenere.SampleText
import com.danyastry.encryption.feature.vigenere.VigenereCipher
import java.security.SecureRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom
import kotlin.random.nextInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VigenereViewModel(
    private val random: Random = SecureRandom().asKotlinRandom(),
) : ViewModel() {

    private val _state = MutableStateFlow(VigenereUiState().recalculated())
    val state: StateFlow<VigenereUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onKeywordChange(keyword: String) = update { copy(keyword = Keyword.normalize(keyword)) }

    fun onModeChange(mode: CipherMode) = update { copy(mode = mode) }

    fun onUseOutputAsInput() = update { copy(input = output, mode = mode.toggled()) }

    fun onLoadSample() = update {
        val secret = String(CharArray(random.nextInt(SAMPLE_KEY_LENGTHS)) { ALPHABET[random.nextInt(ALPHABET.length)] })
        copy(mode = CipherMode.DECRYPT, input = VigenereCipher().encrypt(SampleText.KASISKI, Keyword(secret)), keyword = "A")
    }

    fun onUseCrackedKey() = update { kasiski?.let { copy(keyword = it.key) } ?: this }

    private fun update(transform: VigenereUiState.() -> VigenereUiState) {
        _state.update { it.transform().recalculated() }
    }

    private companion object {
        val SAMPLE_KEY_LENGTHS = 4..9
    }
}
