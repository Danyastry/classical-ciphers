package com.danyastry.encryption.feature.columnar.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ColumnarViewModelTest {

    private val viewModel = ColumnarViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the zebras keyword without padding`() {
        assertThat(state.keyword).isEqualTo("ZEBRAS")
        assertThat(state.padWithX).isFalse()
        assertThat(state.readingOrder).isEqualTo("A B E R S Z")
        assertThat(state.output).isEmpty()
        assertThat(state.grid).isEmpty()
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("WEAREDISCOVEREDFLEEATONCE")

        assertThat(state.output).isEqualTo("EVLNACDTESEAROFODEECWIREE")
    }

    @Test
    fun `padding switch pads the ciphertext and the grid`() {
        viewModel.onKeywordChange("german")
        viewModel.onInputChange("DEFENDTHEEASTWALLOFTHECASTLE")

        viewModel.onPadWithXChange(true)

        assertThat(state.output).isEqualTo("NALCXEHWTTDTTFSEELEEDSOAXFEAHL")
        assertThat(state.grid.last()).isEqualTo("STLEXX")
    }

    @Test
    fun `grid writes the text in rows of keyword length`() {
        viewModel.onInputChange("WEAREDISCOVER")

        assertThat(state.grid).containsExactly("WEARED", "ISCOVE", "R").inOrder()
    }

    @Test
    fun `grid is capped`() {
        viewModel.onInputChange("a".repeat(200))

        assertThat(state.grid).hasSize(ColumnarUiState.MAX_GRID_ROWS)
    }

    @Test
    fun `empty keyword is reported and clears the output`() {
        viewModel.onInputChange("Hello")

        viewModel.onKeywordChange("123")

        assertThat(state.isKeywordValid).isFalse()
        assertThat(state.output).isEmpty()
        assertThat(state.grid).isEmpty()
    }

    @Test
    fun `decrypt mode decrypts and shows the plaintext grid`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange("EVLNACDTESEAROFODEECWIREE")

        assertThat(state.output).isEqualTo("WEAREDISCOVEREDFLEEATONCE")
        assertThat(state.grid.first()).isEqualTo("WEARED")
    }

    @Test
    fun `use output as input flips the mode and restores the plaintext`() {
        viewModel.onInputChange("Attack at dawn!")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("Attack at dawn!")
    }
}
