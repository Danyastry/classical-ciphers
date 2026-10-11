package com.danyastry.encryption.feature.beaufort.ui

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BeaufortViewModelTest {

    private val viewModel = BeaufortViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the fortification keyword`() {
        assertThat(state.keyword).isEqualTo("FORTIFICATION")
        assertThat(state.isKeywordValid).isTrue()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `transforms input as it changes`() {
        viewModel.onInputChange("DEFENDTHEEASTWALLOFTHECASTLE")

        assertThat(state.output).isEqualTo("CKMPVCPVWPIWUJOGIUAPVWRIWUUK")
    }

    @Test
    fun `keyword is normalized`() {
        viewModel.onKeywordChange("ke-y 1")

        assertThat(state.keyword).isEqualTo("KEY")
    }

    @Test
    fun `empty keyword is reported and clears the output`() {
        viewModel.onInputChange("Hello")

        viewModel.onKeywordChange("")

        assertThat(state.isKeywordValid).isFalse()
        assertThat(state.output).isEmpty()
        assertThat(state.alignment).isEmpty()
    }

    @Test
    fun `alignment pairs each letter with its key letter`() {
        viewModel.onKeywordChange("KEY")
        viewModel.onInputChange("He, l")

        assertThat(state.alignment).containsExactly(
            AlignedLetter(input = 'H', key = 'K', output = 'D'),
            AlignedLetter(input = 'e', key = 'E', output = 'a'),
            AlignedLetter(input = 'l', key = 'Y', output = 'n'),
        ).inOrder()
    }

    @Test
    fun `use output as input restores the original text`() {
        viewModel.onInputChange("Attack at dawn")

        viewModel.onUseOutputAsInput()

        assertThat(state.output).isEqualTo("Attack at dawn")
    }
}
