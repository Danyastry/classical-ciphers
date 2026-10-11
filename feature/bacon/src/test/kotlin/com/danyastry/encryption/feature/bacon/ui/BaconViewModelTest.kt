package com.danyastry.encryption.feature.bacon.ui

import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.bacon.BaconAlphabet
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BaconViewModelTest {

    private val viewModel = BaconViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the classic alphabet in encrypt mode`() {
        assertThat(state.alphabet).isEqualTo(BaconAlphabet.CLASSIC_24)
        assertThat(state.mode).isEqualTo(CipherMode.ENCRYPT)
        assertThat(state.output).isEmpty()
        assertThat(state.stegotext).isEmpty()
    }

    @Test
    fun `encodes input and hides it in the cover text`() {
        viewModel.onInputChange("Hi")

        assertThat(state.output).isEqualTo("AABBB ABAAA")
        assertThat(state.requiredCoverLetters).isEqualTo(10)
        assertThat(state.stegotext).isNotEmpty()
        assertThat(state.isCoverTooShort).isFalse()
    }

    @Test
    fun `switching alphabet re-encodes`() {
        viewModel.onInputChange("J")

        viewModel.onAlphabetChange(BaconAlphabet.MODERN_26)

        assertThat(state.output).isEqualTo("ABAAB")
    }

    @Test
    fun `short cover text is reported and nothing is hidden`() {
        viewModel.onCoverChange("tiny")
        viewModel.onInputChange("Hi")

        assertThat(state.isCoverTooShort).isTrue()
        assertThat(state.coverLetters).isEqualTo(4)
        assertThat(state.stegotext).isEmpty()
    }

    @Test
    fun `decodes A and B groups`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange("AABBB AABAA ABABA ABABA ABBAB")

        assertThat(state.output).isEqualTo("HELLO")
        assertThat(state.isIncompleteCode).isFalse()
    }

    @Test
    fun `incomplete code is reported`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange("AABB")

        assertThat(state.isIncompleteCode).isTrue()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `reveal action decodes the hidden message from letter case`() {
        viewModel.onInputChange("Meet at noon")

        viewModel.onUseStegotextAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.decodeSource).isEqualTo(DecodeSource.LETTER_CASE)
        assertThat(state.output).isEqualTo("MEETATNOON")
    }

    @Test
    fun `use output as input decodes the groups back`() {
        viewModel.onInputChange("Hello")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.decodeSource).isEqualTo(DecodeSource.AB_GROUPS)
        assertThat(state.output).isEqualTo("HELLO")
    }
}
