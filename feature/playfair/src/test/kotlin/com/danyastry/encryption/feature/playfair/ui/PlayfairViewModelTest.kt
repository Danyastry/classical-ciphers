package com.danyastry.encryption.feature.playfair.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.playfair.PlayfairRule
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PlayfairViewModelTest {

    private val viewModel = PlayfairViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the wikipedia keyword and its square`() {
        assertThat(state.keyword).isEqualTo("PLAYFAIR EXAMPLE")
        assertThat(state.square).isEqualTo("PLAYFIREXMBCDGHKNOQSTUVWZ")
        assertThat(state.mode).isEqualTo(CipherMode.ENCRYPT)
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `encrypts input into spaced pairs`() {
        viewModel.onInputChange("hide the gold in the tree stump")

        assertThat(state.output).isEqualTo("BM OD ZB XD NA BE KU DM UI XM MO UV IF")
    }

    @Test
    fun `keyword updates the square`() {
        viewModel.onKeywordChange("monarchy")

        assertThat(state.keyword).isEqualTo("MONARCHY")
        assertThat(state.square).isEqualTo("MONARCHYBDEFGIKLPQSTUVWXZ")
    }

    @Test
    fun `lists steps with their rules`() {
        viewModel.onInputChange("hide")

        assertThat(state.steps.map { it.input to it.rule }).containsExactly(
            "HI" to PlayfairRule.RECTANGLE,
            "DE" to PlayfairRule.SAME_COLUMN,
        ).inOrder()
    }

    @Test
    fun `steps are capped`() {
        viewModel.onInputChange("ab".repeat(40))

        assertThat(state.steps).hasSize(PlayfairUiState.MAX_STEPS)
    }

    @Test
    fun `decrypt mode decrypts without spaces`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange("BM OD ZB XD NA BE KU DM UI XM MO UV IF")

        assertThat(state.output).isEqualTo("HIDETHEGOLDINTHETREXESTUMP")
        assertThat(state.isOddCiphertext).isFalse()
    }

    @Test
    fun `odd ciphertext is reported and clears the output`() {
        viewModel.onModeChange(CipherMode.DECRYPT)

        viewModel.onInputChange("BMO")

        assertThat(state.isOddCiphertext).isTrue()
        assertThat(state.output).isEmpty()
        assertThat(state.steps).isEmpty()
    }

    @Test
    fun `odd length plaintext is fine in encrypt mode`() {
        viewModel.onInputChange("abc")

        assertThat(state.isOddCiphertext).isFalse()
        assertThat(state.output).isNotEmpty()
    }

    @Test
    fun `use output as input decrypts back to the prepared plaintext`() {
        viewModel.onInputChange("balloon")
        viewModel.onKeywordChange("monarchy")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("BALXLOON")
    }
}
