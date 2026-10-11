package com.danyastry.encryption.feature.bifid.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BifidViewModelTest {

    private val viewModel = BifidViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the wikipedia square for the whole message`() {
        assertThat(state.square).containsExactly("BGWKZ", "QPNDS", "IOAXE", "FCLUM", "THYVR").inOrder()
        assertThat(state.period).isEqualTo(0)
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("Flee at once")

        assertThat(state.output).isEqualTo("UAEOLWRINS")
    }

    @Test
    fun `shows each plaintext letter with its coordinates`() {
        viewModel.onInputChange("Flee")

        assertThat(state.coordinates).containsExactly(
            Coordinates('F', row = 4, column = 1),
            Coordinates('L', row = 4, column = 3),
            Coordinates('E', row = 3, column = 5),
            Coordinates('E', row = 3, column = 5),
        ).inOrder()
    }

    @Test
    fun `period is clamped`() {
        viewModel.onPeriodChange(50)
        assertThat(state.period).isEqualTo(10)

        viewModel.onPeriodChange(-3)
        assertThat(state.period).isEqualTo(0)
    }

    @Test
    fun `keyword and period reproduce the practical cryptography example`() {
        viewModel.onKeywordChange("phqgmeaylnofdxkrcvszwbuti")
        viewModel.onPeriodChange(5)

        viewModel.onInputChange("defend the east wall of the castle")

        assertThat(state.output).isEqualTo("FFYHMKHYCPLIASHADTRLHCCHLBLR")
    }

    @Test
    fun `decrypt mode shows coordinates of the recovered plaintext`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange("UAEOLWRINS")

        assertThat(state.output).isEqualTo("FLEEATONCE")
        assertThat(state.coordinates.first()).isEqualTo(Coordinates('F', row = 4, column = 1))
    }

    @Test
    fun `use output as input decrypts back`() {
        viewModel.onInputChange("Attack at dawn")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("ATTACKATDAWN")
    }
}
