package com.danyastry.encryption.feature.twosquare.ui

import com.danyastry.encryption.feature.twosquare.SquareAlphabet
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TwoSquareViewModelTest {

    private val viewModel = TwoSquareViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the wikipedia keywords`() {
        assertThat(state.alphabet).isEqualTo(SquareAlphabet.OMIT_Q)
        assertThat(state.topSquare.first()).isEqualTo("EXAMP")
        assertThat(state.bottomSquare.first()).isEqualTo("KEYWO")
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `encrypts into spaced pairs and counts transparencies`() {
        viewModel.onInputChange("Help me Obi-Wan Kenobi")

        assertThat(state.output).isEqualTo("HE DL XW SD JY AN HO TK DG")
        assertThat(state.transparencies).isEqualTo(2)
        assertThat(state.pairs).isEqualTo(9)
    }

    @Test
    fun `steps are capped`() {
        viewModel.onInputChange("ab".repeat(40))

        assertThat(state.steps).hasSize(TwoSquareUiState.MAX_STEPS)
        assertThat(state.pairs).isEqualTo(40)
    }

    @Test
    fun `keywords and alphabet update the squares`() {
        viewModel.onAlphabetChange(SquareAlphabet.MERGE_I_J)
        viewModel.onFirstKeywordChange("zebras")

        assertThat(state.topSquare.first()).isEqualTo("ZEBRA")
        assertThat(state.topSquare.joinToString("")).doesNotContain("J")
    }

    @Test
    fun `use output as input restores the plaintext`() {
        viewModel.onInputChange("Help me Obi-Wan Kenobi")

        viewModel.onUseOutputAsInput()

        assertThat(state.output).isEqualTo("HE LP ME OB IW AN KE NO BI")
    }
}
