package com.danyastry.encryption.feature.railfence.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RailFenceViewModelTest {

    private val viewModel = RailFenceViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with three rails in encrypt mode`() {
        assertThat(state.rails).isEqualTo(3)
        assertThat(state.mode).isEqualTo(CipherMode.ENCRYPT)
        assertThat(state.output).isEmpty()
        assertThat(state.zigzag).isEmpty()
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("WEAREDISCOVEREDRUNATONCE")

        assertThat(state.output).isEqualTo("WECRUOERDSOEERNTNEAIVDAC")
    }

    @Test
    fun `clamps rails into range`() {
        viewModel.onRailsChange(1)
        assertThat(state.rails).isEqualTo(2)

        viewModel.onRailsChange(99)
        assertThat(state.rails).isEqualTo(10)
    }

    @Test
    fun `zigzag places each character on its rail`() {
        viewModel.onInputChange("WEARE")

        assertThat(state.zigzag).containsExactly(
            listOf('W', null, null, null, 'E'),
            listOf(null, 'E', null, 'R', null),
            listOf(null, null, 'A', null, null),
        ).inOrder()
    }

    @Test
    fun `zigzag is built from the plaintext in decrypt mode`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange("WECRUOERDSOEERNTNEAIVDAC")

        assertThat(state.output).isEqualTo("WEAREDISCOVEREDRUNATONCE")
        assertThat(state.zigzag.first().filterNotNull().joinToString("")).isEqualTo("WECRUO")
    }

    @Test
    fun `zigzag is capped`() {
        viewModel.onInputChange("a".repeat(100))

        assertThat(state.zigzag.first()).hasSize(RailFenceUiState.MAX_ZIGZAG_COLUMNS)
    }

    @Test
    fun `use output as input flips the mode and restores the plaintext`() {
        viewModel.onInputChange("Attack at dawn!")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("Attack at dawn!")
    }
}
