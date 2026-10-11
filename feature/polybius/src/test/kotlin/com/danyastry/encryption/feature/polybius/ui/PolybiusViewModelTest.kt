package com.danyastry.encryption.feature.polybius.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PolybiusViewModelTest {

    private val viewModel = PolybiusViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the standard square`() {
        assertThat(state.keyword).isEmpty()
        assertThat(state.square).containsExactly("ABCDE", "FGHIK", "LMNOP", "QRSTU", "VWXYZ").inOrder()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("Hello")

        assertThat(state.output).isEqualTo("23 15 31 31 34")
    }

    @Test
    fun `keyword updates the square and the output`() {
        viewModel.onInputChange("Hello")

        viewModel.onKeywordChange("zebras")

        assertThat(state.square.first()).isEqualTo("ZEBRA")
        assertThat(state.output).isEqualTo("31 12 34 34 42")
    }

    @Test
    fun `invalid code is reported in decrypt mode`() {
        viewModel.onModeChange(CipherMode.DECRYPT)

        viewModel.onInputChange("23 1")

        assertThat(state.isInvalidCode).isTrue()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `use output as input decodes back`() {
        viewModel.onInputChange("Attack at dawn")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.isInvalidCode).isFalse()
        assertThat(state.output).isEqualTo("ATTACKATDAWN")
    }
}
