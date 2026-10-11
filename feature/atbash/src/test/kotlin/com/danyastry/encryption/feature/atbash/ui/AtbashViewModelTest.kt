package com.danyastry.encryption.feature.atbash.ui

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AtbashViewModelTest {

    private val viewModel = AtbashViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts empty`() {
        assertThat(state.input).isEmpty()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `transforms input as it changes`() {
        viewModel.onInputChange("Hello")

        assertThat(state.output).isEqualTo("Svool")
    }

    @Test
    fun `use output as input restores the original text`() {
        viewModel.onInputChange("Secret 42")

        viewModel.onUseOutputAsInput()

        assertThat(state.input).isEqualTo("Hvxivg 42")
        assertThat(state.output).isEqualTo("Secret 42")
    }
}
