package com.danyastry.encryption.feature.scytale.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ScytaleViewModelTest {

    private val viewModel = ScytaleViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with four letters around the rod`() {
        assertThat(state.turns).isEqualTo(4)
        assertThat(state.mode).isEqualTo(CipherMode.ENCRYPT)
        assertThat(state.output).isEmpty()
        assertThat(state.wrapped).isEmpty()
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("Iamhurtverybadlyhelp")

        assertThat(state.output).isEqualTo("Iryyatbhmvaehedlurlp")
        assertThat(state.wrapped).containsExactly("Iamhu", "rtver", "ybadl", "yhelp").inOrder()
    }

    @Test
    fun `clamps turns into range`() {
        viewModel.onTurnsChange(1)
        assertThat(state.turns).isEqualTo(2)

        viewModel.onTurnsChange(50)
        assertThat(state.turns).isEqualTo(10)
    }

    @Test
    fun `decrypt mode shows the plaintext on the rod`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange("Iryyatbhmvaehedlurlp")

        assertThat(state.output).isEqualTo("Iamhurtverybadlyhelp")
        assertThat(state.wrapped.first()).isEqualTo("Iamhu")
    }

    @Test
    fun `wrapped rows are capped`() {
        viewModel.onInputChange("a".repeat(400))

        assertThat(state.wrapped.first()).hasLength(ScytaleUiState.MAX_WRAPPED_COLUMNS)
    }

    @Test
    fun `use output as input flips the mode and restores the plaintext`() {
        viewModel.onInputChange("Attack at dawn!")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("Attack at dawn!")
    }
}
