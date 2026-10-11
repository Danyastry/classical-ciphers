package com.danyastry.encryption.feature.foursquare.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.foursquare.SquareAlphabet
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FourSquareViewModelTest {

    private val viewModel = FourSquareViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the wikipedia keywords and squares`() {
        assertThat(state.firstKeyword).isEqualTo("EXAMPLE")
        assertThat(state.secondKeyword).isEqualTo("KEYWORD")
        assertThat(state.alphabet).isEqualTo(SquareAlphabet.OMIT_Q)
        assertThat(state.upperRightSquare.first()).isEqualTo("EXAMP")
        assertThat(state.lowerLeftSquare.first()).isEqualTo("KEYWO")
        assertThat(state.plainSquare.first()).isEqualTo("ABCDE")
    }

    @Test
    fun `encrypts input into spaced pairs`() {
        viewModel.onInputChange("Help me Obi-Wan Kenobi")

        assertThat(state.output).isEqualTo("FY GM KY HO BX MF KK KI MD")
    }

    @Test
    fun `alphabet and keywords reproduce the practical cryptography example`() {
        viewModel.onAlphabetChange(SquareAlphabet.MERGE_I_J)
        viewModel.onFirstKeywordChange("zgptfoihmuwdrcnykeqaxvsbl")
        viewModel.onSecondKeywordChange("mfnbdcrhsaxyogvituewlqzkp")

        viewModel.onInputChange("attack at dawn")

        assertThat(state.output).isEqualTo("TI YB FH TI ZB SY")
    }

    @Test
    fun `odd ciphertext is reported in decrypt mode`() {
        viewModel.onModeChange(CipherMode.DECRYPT)

        viewModel.onInputChange("FYG")

        assertThat(state.isOddCiphertext).isTrue()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `use output as input decrypts back`() {
        viewModel.onInputChange("Help me Obi-Wan Kenobi")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.isOddCiphertext).isFalse()
        assertThat(state.output).isEqualTo("HELPMEOBIWANKENOBI")
    }
}
