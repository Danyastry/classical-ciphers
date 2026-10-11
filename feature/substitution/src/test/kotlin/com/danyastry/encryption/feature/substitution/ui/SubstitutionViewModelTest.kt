package com.danyastry.encryption.feature.substitution.ui

import com.danyastry.encryption.core.classical.ALPHABET
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.substitution.KeyError
import com.danyastry.encryption.feature.substitution.SampleText
import com.danyastry.encryption.feature.substitution.SubstitutionCipher
import com.danyastry.encryption.feature.substitution.SubstitutionKey
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Test

class SubstitutionViewModelTest {

    private val viewModel = SubstitutionViewModel(random = Random(seed = 3))

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the zebras keyword`() {
        assertThat(state.keyword).isEqualTo("ZEBRAS")
        assertThat(state.keyText).isEqualTo("ZEBRASCDFGHIJKLMNOPQTUVWXY")
        assertThat(state.keyError).isNull()
        assertThat(state.mode).isEqualTo(CipherMode.ENCRYPT)
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("FLEE AT ONCE. WE ARE DISCOVERED!")

        assertThat(state.output).isEqualTo("SIAA ZQ LKBA. VA ZOA RFPBLUAOAR!")
    }

    @Test
    fun `keyword updates the key`() {
        viewModel.onKeywordChange("balloon")

        assertThat(state.keyText).isEqualTo("BALONCDEFGHIJKMPQRSTUVWXYZ")
    }

    @Test
    fun `editing the key directly clears the keyword and normalizes the text`() {
        viewModel.onKeyTextChange("zyx wvu-1")

        assertThat(state.keyword).isEmpty()
        assertThat(state.keyText).isEqualTo("ZYXWVU")
    }

    @Test
    fun `incomplete key reports an error and clears the output`() {
        viewModel.onInputChange("Hello")

        viewModel.onKeyTextChange("ZEBRAS")

        assertThat(state.keyError).isEqualTo(KeyError.WRONG_LENGTH)
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `duplicate letters report an error`() {
        viewModel.onKeyTextChange("AABCDEFGHIJKLMNOPQRSTUVWXY")

        assertThat(state.keyError).isEqualTo(KeyError.DUPLICATE_LETTERS)
    }

    @Test
    fun `key is capped at 26 letters`() {
        viewModel.onKeyTextChange(ALPHABET + "ABC")

        assertThat(state.keyText).isEqualTo(ALPHABET)
        assertThat(state.keyError).isNull()
    }

    @Test
    fun `random key is valid and clears the keyword`() {
        viewModel.onRandomKey()

        assertThat(state.keyword).isEmpty()
        assertThat(state.keyError).isNull()
        assertThat(state.keyText.toSet()).isEqualTo(ALPHABET.toSet())
    }

    @Test
    fun `use output as input flips the mode and restores the plaintext`() {
        viewModel.onRandomKey()
        viewModel.onInputChange("Attack at dawn")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("Attack at dawn")
    }

    @Test
    fun `loading the sample switches to decrypt with an identity key`() {
        viewModel.onLoadSample()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.keyText).isEqualTo(ALPHABET)
        assertThat(state.output).isEqualTo(state.input)
        assertThat(state.cipherLettersByFrequency).hasLength(26)
    }

    @Test
    fun `guessing the key recovers E and T in the sample`() {
        viewModel.onLoadSample()

        viewModel.onGuessKey()

        val guess = SubstitutionKey(state.keyText)
        val plainLetters = SampleText.FREQUENCY_ANALYSIS.uppercase().filter { it in 'A'..'Z' }
        val guessedLetters = state.output.uppercase().filter { it in 'A'..'Z' }
        val matching = plainLetters.indices.filter { plainLetters[it] == guessedLetters[it] }.map { plainLetters[it] }.toSet()
        assertThat(guess.alphabet.toSet()).hasSize(26)
        assertThat(matching).containsAtLeast('E', 'T')
    }

    @Test
    fun `swap exchanges two plaintext letters and clears the fields`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange(SubstitutionCipher().encrypt("HELLO", SubstitutionKey.fromKeyword("ZEBRAS")))

        viewModel.onSwapFirstChange("l")
        viewModel.onSwapSecondChange("o")
        viewModel.onSwap()

        assertThat(state.output).isEqualTo("HEOOL")
        assertThat(state.swapFirst).isEmpty()
        assertThat(state.swapSecond).isEmpty()
    }

    @Test
    fun `swap fields keep a single letter`() {
        viewModel.onSwapFirstChange("ab1")

        assertThat(state.swapFirst).isEqualTo("B")
    }

    @Test
    fun `frequency table is only shown for ciphertext`() {
        viewModel.onInputChange("Hello")

        assertThat(state.cipherLettersByFrequency).isEmpty()
    }
}
