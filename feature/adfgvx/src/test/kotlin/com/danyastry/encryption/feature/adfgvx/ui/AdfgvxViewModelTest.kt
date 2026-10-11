package com.danyastry.encryption.feature.adfgvx.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Test

class AdfgvxViewModelTest {

    private val viewModel = AdfgvxViewModel(random = Random(seed = 4))

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the wikipedia square and keyword`() {
        assertThat(state.square.first()).isEqualTo("NA1C3H")
        assertThat(state.transposition).isEqualTo("PRIVACY")
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `encrypts input into groups of five and shows the first step`() {
        viewModel.onInputChange("attack at 1200am")

        assertThat(state.fractionated).isEqualTo("AD DD DD AD AG VG AD DD AF DG VF VF AD DX")
        assertThat(state.output).isEqualTo("DGDDD AGDDG AFADD FDADV DVFAA DVX")
    }

    @Test
    fun `square keyword is normalized and capped`() {
        viewModel.onSquareKeywordChange("ph0-qg 64!" + "a".repeat(50))

        assertThat(state.squareKeyword).startsWith("PH0QG64")
        assertThat(state.squareKeyword).hasLength(36)
        assertThat(state.square.first()).isEqualTo("PH0QG6")
    }

    @Test
    fun `random square uses every character once`() {
        viewModel.onRandomSquare()

        assertThat(state.square.joinToString("").toSet()).hasSize(36)
    }

    @Test
    fun `empty transposition keyword is reported`() {
        viewModel.onInputChange("attack")

        viewModel.onTranspositionChange("123")

        assertThat(state.isTranspositionValid).isFalse()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `invalid ciphertext is reported in decrypt mode`() {
        viewModel.onModeChange(CipherMode.DECRYPT)

        viewModel.onInputChange("ADFGB")

        assertThat(state.isInvalidCiphertext).isTrue()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `use output as input decrypts back and shows the first step`() {
        viewModel.onInputChange("attack at 1200am")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("ATTACKAT1200AM")
        assertThat(state.fractionated).isEqualTo("AD DD DD AD AG VG AD DD AF DG VF VF AD DX")
    }
}
