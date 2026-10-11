package com.danyastry.encryption.feature.autokey.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AutokeyViewModelTest {

    private val viewModel = AutokeyViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the queenly primer in encrypt mode`() {
        assertThat(state.primer).isEqualTo("QUEENLY")
        assertThat(state.isPrimerValid).isTrue()
        assertThat(state.mode).isEqualTo(CipherMode.ENCRYPT)
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("ATTACKATDAWN")

        assertThat(state.output).isEqualTo("QNXEPVYTWTWP")
    }

    @Test
    fun `primer is normalized`() {
        viewModel.onPrimerChange("ke-y 1")

        assertThat(state.primer).isEqualTo("KEY")
    }

    @Test
    fun `empty primer is reported and clears the output`() {
        viewModel.onInputChange("Hello")

        viewModel.onPrimerChange("")

        assertThat(state.isPrimerValid).isFalse()
        assertThat(state.output).isEmpty()
        assertThat(state.alignment).isEmpty()
    }

    @Test
    fun `key row starts with the primer and continues with the plaintext`() {
        viewModel.onPrimerChange("KEY")
        viewModel.onInputChange("Hi, all")

        assertThat(state.alignment.map { it.key }).containsExactly('K', 'E', 'Y', 'H', 'I').inOrder()
    }

    @Test
    fun `decrypt mode shows the recovered plaintext in the key row`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange("QNXEPVYTWTWP")

        assertThat(state.output).isEqualTo("ATTACKATDAWN")
        assertThat(state.alignment.map { it.key }.joinToString("")).isEqualTo("QUEENLYATTAC")
    }

    @Test
    fun `alignment is capped`() {
        viewModel.onInputChange("a".repeat(100))

        assertThat(state.alignment).hasSize(AutokeyUiState.MAX_ALIGNED_LETTERS)
    }

    @Test
    fun `use output as input flips the mode and restores the plaintext`() {
        viewModel.onInputChange("Attack at dawn")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("Attack at dawn")
    }
}
