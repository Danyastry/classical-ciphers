package com.danyastry.encryption.feature.substitution.ui

import com.danyastry.encryption.feature.substitution.KeyError
import com.danyastry.encryption.feature.substitution.SubstitutionKey
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Test

class SubstitutionViewModelTest {

    private val viewModel = SubstitutionViewModel(random = Random(seed = 3))

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the zebras keyword`() {
        assertThat(state.keyword).isEqualTo("ZEBRAS")
        assertThat(state.keyText).isEqualTo("ZEBRASCDFGHIJKLMNOPQTUVWXY")
        assertThat(state.keyError).isNull()
        assertThat(state.mode).isEqualTo(SubstitutionMode.ENCRYPT)
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("FLEE AT ONCE. WE ARE DISCOVERED!")

        assertThat(state.output).isEqualTo("SIAA ZQ LKBA. VA ZOA RFPBLUAOAR!")
    }

    @Test
    fun `keyword updates the key`() {
        viewModel.onKeywordChange("balloon")

        assertThat(state.keyText).isEqualTo("BALONCDEFGHIJKMPQRSTUVWXYZ")
    }

    @Test
    fun `editing the key directly clears the keyword and normalizes the text`() {
        viewModel.onKeyTextChange("zyx wvu-1")

        assertThat(state.keyword).isEmpty()
        assertThat(state.keyText).isEqualTo("ZYXWVU")
    }

    @Test
    fun `incomplete key reports an error and clears the output`() {
        viewModel.onInputChange("Hello")

        viewModel.onKeyTextChange("ZEBRAS")

        assertThat(state.keyError).isEqualTo(KeyError.WRONG_LENGTH)
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `duplicate letters report an error`() {
        viewModel.onKeyTextChange("AABCDEFGHIJKLMNOPQRSTUVWXY")

        assertThat(state.keyError).isEqualTo(KeyError.DUPLICATE_LETTERS)
    }

    @Test
    fun `key is capped at 26 letters`() {
        viewModel.onKeyTextChange(SubstitutionKey.PLAIN_ALPHABET + "ABC")

        assertThat(state.keyText).isEqualTo(SubstitutionKey.PLAIN_ALPHABET)
        assertThat(state.keyError).isNull()
    }

    @Test
    fun `random key is valid and clears the keyword`() {
        viewModel.onRandomKey()

        assertThat(state.keyword).isEmpty()
        assertThat(state.keyError).isNull()
        assertThat(state.keyText.toSet()).isEqualTo(SubstitutionKey.PLAIN_ALPHABET.toSet())
    }

    @Test
    fun `use output as input flips the mode and restores the plaintext`() {
        viewModel.onRandomKey()
        viewModel.onInputChange("Attack at dawn")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(SubstitutionMode.DECRYPT)
        assertThat(state.output).isEqualTo("Attack at dawn")
    }
}
