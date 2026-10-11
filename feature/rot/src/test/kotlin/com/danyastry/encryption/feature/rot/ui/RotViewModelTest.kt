package com.danyastry.encryption.feature.rot.ui

import com.danyastry.encryption.feature.rot.RotVariant
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RotViewModelTest {

    private val viewModel = RotViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with rot13 and empty output`() {
        assertThat(state.variant).isEqualTo(RotVariant.ROT13)
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `transforms input as it changes`() {
        viewModel.onInputChange("Hello")

        assertThat(state.output).isEqualTo("Uryyb")
    }

    @Test
    fun `switching variant re-transforms the input`() {
        viewModel.onInputChange("Hi 1")

        viewModel.onVariantChange(RotVariant.ROT47)

        assertThat(state.output).isEqualTo("w: `")
    }

    @Test
    fun `use output as input restores the original text`() {
        viewModel.onInputChange("Secret 42")

        viewModel.onUseOutputAsInput()

        assertThat(state.input).isEqualTo("Frperg 42")
        assertThat(state.output).isEqualTo("Secret 42")
    }
}
