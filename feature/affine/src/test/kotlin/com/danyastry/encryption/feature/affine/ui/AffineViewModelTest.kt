package com.danyastry.encryption.feature.affine.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.affine.AffineKey
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AffineViewModelTest {

    private val viewModel = AffineViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts in encrypt mode with the reference key`() {
        assertThat(state.mode).isEqualTo(CipherMode.ENCRYPT)
        assertThat(state.key).isEqualTo(AffineKey(a = 5, b = 8))
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("AFFINE CIPHER")

        assertThat(state.output).isEqualTo("IHHWVC SWFRCP")
    }

    @Test
    fun `re-encrypts when the key changes`() {
        viewModel.onInputChange("Hello, World!")
        viewModel.onMultiplierChange(7)
        viewModel.onShiftChange(3)

        assertThat(state.key).isEqualTo(AffineKey(a = 7, b = 3))
        assertThat(state.output).isEqualTo("Afccx, Bxscy!")
    }

    @Test
    fun `ignores multipliers that are not coprime with 26`() {
        viewModel.onMultiplierChange(13)
        viewModel.onMultiplierChange(2)

        assertThat(state.key.a).isEqualTo(5)
    }

    @Test
    fun `clamps shift into range`() {
        viewModel.onShiftChange(40)
        assertThat(state.key.b).isEqualTo(25)

        viewModel.onShiftChange(-1)
        assertThat(state.key.b).isEqualTo(0)
    }

    @Test
    fun `decrypt mode decrypts the input`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange("IHHWVC SWFRCP")

        assertThat(state.output).isEqualTo("AFFINE CIPHER")
    }

    @Test
    fun `use output as input flips the mode and restores the plaintext`() {
        viewModel.onInputChange("Attack at dawn")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("Attack at dawn")
    }
}
