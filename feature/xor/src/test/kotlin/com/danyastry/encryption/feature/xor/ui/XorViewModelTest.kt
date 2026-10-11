package com.danyastry.encryption.feature.xor.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.xor.ByteEncoding
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class XorViewModelTest {

    private val viewModel = XorViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the ICE key and hex output`() {
        assertThat(state.key).isEqualTo("ICE")
        assertThat(state.keyFormat).isEqualTo(KeyFormat.TEXT)
        assertThat(state.encoding).isEqualTo(ByteEncoding.HEX)
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `encrypts text to hex and base64`() {
        viewModel.onKeyChange("key")
        viewModel.onInputChange("Hello")
        assertThat(state.output).isEqualTo("230015070a")

        viewModel.onEncodingChange(ByteEncoding.BASE64)
        assertThat(state.output).isEqualTo("IwAVBwo=")
    }

    @Test
    fun `hex keys are accepted and validated`() {
        viewModel.onKeyFormatChange(KeyFormat.HEX)
        viewModel.onKeyChange("6b6579")
        viewModel.onInputChange("Hello")
        assertThat(state.output).isEqualTo("230015070a")

        viewModel.onKeyChange("6b6")
        assertThat(state.isKeyInvalid).isTrue()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `empty key is reported`() {
        viewModel.onInputChange("Hello")

        viewModel.onKeyChange("")

        assertThat(state.isKeyInvalid).isTrue()
    }

    @Test
    fun `invalid ciphertext is reported in decrypt mode`() {
        viewModel.onModeChange(CipherMode.DECRYPT)

        viewModel.onInputChange("not hex")

        assertThat(state.isInputInvalid).isTrue()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `known plaintext reveals the key`() {
        viewModel.onInputChange("Burning 'em, if you ain't quick and nimble")

        viewModel.onCribChange("Burning")

        assertThat(state.recoveredKeyText).isEqualTo("ICEICEI")
        assertThat(state.recoveredKeyHex).isEqualTo("49434549434549")
    }

    @Test
    fun `use output as input decrypts back`() {
        viewModel.onInputChange("Attack at dawn!")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.isInputInvalid).isFalse()
        assertThat(state.output).isEqualTo("Attack at dawn!")
    }

    @Test
    fun `round trips unicode text through base64`() {
        viewModel.onEncodingChange(ByteEncoding.BASE64)
        viewModel.onInputChange("Emoji ✓ and ümlauts")

        viewModel.onUseOutputAsInput()

        assertThat(state.output).isEqualTo("Emoji ✓ and ümlauts")
    }
}
