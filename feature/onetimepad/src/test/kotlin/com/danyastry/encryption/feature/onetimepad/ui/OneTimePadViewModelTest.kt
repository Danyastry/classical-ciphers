package com.danyastry.encryption.feature.onetimepad.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Test

class OneTimePadViewModelTest {

    private val viewModel = OneTimePadViewModel(random = Random(seed = 6))

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the wikipedia key`() {
        assertThat(state.key).isEqualTo("XMCKL")
        assertThat(state.mode).isEqualTo(CipherMode.ENCRYPT)
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("Hello")

        assertThat(state.output).isEqualTo("Eqnvz")
        assertThat(state.lettersNeeded).isEqualTo(5)
    }

    @Test
    fun `short key is reported and clears the output`() {
        viewModel.onInputChange("Hello world")

        assertThat(state.isKeyTooShort).isTrue()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `generated key matches the message length`() {
        viewModel.onInputChange("Hello world")

        viewModel.onGenerateKey()

        assertThat(state.key).hasLength(10)
        assertThat(state.isKeyTooShort).isFalse()
        assertThat(state.output).isNotEmpty()
    }

    @Test
    fun `key is normalized`() {
        viewModel.onKeyChange("xm-ck l1")

        assertThat(state.key).isEqualTo("XMCKL")
    }

    @Test
    fun `finds the key for an alternative message`() {
        viewModel.onInputChange("HELLO")

        viewModel.onAlternativeChange("later")

        assertThat(state.alternativeKey).isEqualTo("TQURI")
        assertThat(state.isAlternativeLengthMismatch).isFalse()
    }

    @Test
    fun `alternative of a different length is reported`() {
        viewModel.onInputChange("HELLO")

        viewModel.onAlternativeChange("hi")

        assertThat(state.isAlternativeLengthMismatch).isTrue()
        assertThat(state.alternativeKey).isEmpty()
    }

    @Test
    fun `decrypt mode uses the input as the ciphertext`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange("EQNVZ")
        viewModel.onAlternativeChange("LATER")

        assertThat(state.output).isEqualTo("HELLO")
        assertThat(state.alternativeKey).isEqualTo("TQURI")
    }

    @Test
    fun `use output as input decrypts back`() {
        viewModel.onInputChange("Hello")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("Hello")
    }
}
