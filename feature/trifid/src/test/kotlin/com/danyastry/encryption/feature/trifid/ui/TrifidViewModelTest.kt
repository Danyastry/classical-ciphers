package com.danyastry.encryption.feature.trifid.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TrifidViewModelTest {

    private val viewModel = TrifidViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the wikipedia cube and period five`() {
        assertThat(state.layers.first()).containsExactly("FEL", "IXM", "ARD").inOrder()
        assertThat(state.period).isEqualTo(5)
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("Aide-toi, le ciel t'aidera")

        assertThat(state.output).isEqualTo("FMJFVOISSUFTFPUFEQQC")
    }

    @Test
    fun `shows layer, row and column of each plaintext letter`() {
        viewModel.onInputChange("Aide")

        assertThat(state.coordinates).containsExactly(
            CubeCoordinates('A', layer = 1, row = 3, column = 1),
            CubeCoordinates('I', layer = 1, row = 2, column = 1),
            CubeCoordinates('D', layer = 1, row = 3, column = 3),
            CubeCoordinates('E', layer = 1, row = 1, column = 2),
        ).inOrder()
    }

    @Test
    fun `period is clamped`() {
        viewModel.onPeriodChange(99)
        assertThat(state.period).isEqualTo(10)

        viewModel.onPeriodChange(-1)
        assertThat(state.period).isEqualTo(0)
    }

    @Test
    fun `keyword updates the cube`() {
        viewModel.onKeywordChange("epsducvwym+zlkxnbtfgorijhaq")

        assertThat(state.layers.first()).containsExactly("EPS", "DUC", "VWY").inOrder()
    }

    @Test
    fun `use output as input decrypts back`() {
        viewModel.onInputChange("Attack at dawn")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("ATTACKATDAWN")
    }
}
