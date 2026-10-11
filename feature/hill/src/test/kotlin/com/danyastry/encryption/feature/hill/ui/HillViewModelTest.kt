package com.danyastry.encryption.feature.hill.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.hill.HillKeyError
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Test

class HillViewModelTest {

    private val viewModel = HillViewModel(random = Random(seed = 2))

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the HILL key and its matrices`() {
        assertThat(state.size).isEqualTo(2)
        assertThat(state.keyText).isEqualTo("HILL")
        assertThat(state.keyError).isNull()
        assertThat(state.keyMatrix).isEqualTo(listOf(listOf(7, 8), listOf(11, 11)))
        assertThat(state.inverseMatrix).isEqualTo(listOf(listOf(25, 22), listOf(1, 23)))
        assertThat(state.determinant).isEqualTo(15)
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("Short example")

        assertThat(state.output).isEqualTo("APADJTFTWLFJ")
    }

    @Test
    fun `switching to 3x3 loads the reference key`() {
        viewModel.onSizeChange(3)
        viewModel.onInputChange("ACT")

        assertThat(state.keyText).isEqualTo("GYBNQKURP")
        assertThat(state.output).isEqualTo("POH")
    }

    @Test
    fun `ignores unsupported sizes`() {
        viewModel.onSizeChange(4)

        assertThat(state.size).isEqualTo(2)
    }

    @Test
    fun `key text is normalized and capped`() {
        viewModel.onKeyTextChange("dd-cf xyz")

        assertThat(state.keyText).isEqualTo("DDCF")
    }

    @Test
    fun `short key reports wrong length and hides the matrices`() {
        viewModel.onInputChange("Hello")

        viewModel.onKeyTextChange("HI")

        assertThat(state.keyError).isEqualTo(HillKeyError.WRONG_LENGTH)
        assertThat(state.keyMatrix).isEmpty()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `non invertible key shows its matrix and determinant but no output`() {
        viewModel.onInputChange("Hello")

        viewModel.onKeyTextChange("ABCD")

        assertThat(state.keyError).isEqualTo(HillKeyError.NOT_INVERTIBLE)
        assertThat(state.keyMatrix).isEqualTo(listOf(listOf(0, 1), listOf(2, 3)))
        assertThat(state.inverseMatrix).isEmpty()
        assertThat(state.determinant).isEqualTo(24)
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `random key is valid`() {
        viewModel.onSizeChange(3)

        viewModel.onRandomKey()

        assertThat(state.keyText).hasLength(9)
        assertThat(state.keyError).isNull()
    }

    @Test
    fun `incomplete ciphertext is reported in decrypt mode`() {
        viewModel.onModeChange(CipherMode.DECRYPT)

        viewModel.onInputChange("APA")

        assertThat(state.isIncompleteCiphertext).isTrue()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `use output as input decrypts back`() {
        viewModel.onInputChange("Short example")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("SHORTEXAMPLE")
    }
}
