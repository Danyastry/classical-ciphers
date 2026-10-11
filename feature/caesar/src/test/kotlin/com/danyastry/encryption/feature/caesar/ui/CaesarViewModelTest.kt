package com.danyastry.encryption.feature.caesar.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CaesarViewModelTest {

    private val viewModel = CaesarViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts in encrypt mode with shift three and empty output`() {
        assertThat(state.mode).isEqualTo(CipherMode.ENCRYPT)
        assertThat(state.shift).isEqualTo(3)
        assertThat(state.output).isEmpty()
        assertThat(state.candidates).isEmpty()
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("Hello")

        assertThat(state.output).isEqualTo("Khoor")
    }

    @Test
    fun `re-encrypts when shift changes`() {
        viewModel.onInputChange("abc")
        viewModel.onShiftChange(1)

        assertThat(state.output).isEqualTo("bcd")
    }

    @Test
    fun `clamps shift into the alphabet range`() {
        viewModel.onShiftChange(100)
        assertThat(state.shift).isEqualTo(25)

        viewModel.onShiftChange(-5)
        assertThat(state.shift).isEqualTo(0)
    }

    @Test
    fun `decrypt mode decrypts and offers brute force candidates`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange("Khoor")

        assertThat(state.output).isEqualTo("Hello")
        assertThat(state.candidates).hasSize(26)
        assertThat(state.candidates[3].plaintext).isEqualTo("Hello")
    }

    @Test
    fun `no brute force candidates for empty input or in encrypt mode`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        assertThat(state.candidates).isEmpty()

        viewModel.onInputChange("abc")
        viewModel.onModeChange(CipherMode.ENCRYPT)
        assertThat(state.candidates).isEmpty()
    }

    @Test
    fun `use output as input flips the mode and restores the plaintext`() {
        viewModel.onInputChange("Attack at dawn")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.input).isEqualTo("Dwwdfn dw gdzq")
        assertThat(state.output).isEqualTo("Attack at dawn")
    }

    @Test
    fun `selecting a brute force candidate decrypts with that shift`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onShiftChange(0)
        viewModel.onInputChange("Hfjxfw")

        viewModel.onCandidateSelected(5)

        assertThat(state.shift).isEqualTo(5)
        assertThat(state.output).isEqualTo("Caesar")
    }
}
