package com.danyastry.encryption.feature.vigenere.ui

import com.danyastry.encryption.core.classical.AlignedLetter
import com.danyastry.encryption.core.ui.components.CipherMode
import com.danyastry.encryption.feature.vigenere.SampleText
import com.google.common.truth.Truth.assertThat
import kotlin.random.Random
import org.junit.Test

class VigenereViewModelTest {

    private val viewModel = VigenereViewModel(random = Random(seed = 5))

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with the lemon keyword in encrypt mode`() {
        assertThat(state.keyword).isEqualTo("LEMON")
        assertThat(state.isKeywordValid).isTrue()
        assertThat(state.mode).isEqualTo(CipherMode.ENCRYPT)
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `encrypts input as it changes`() {
        viewModel.onInputChange("attack at dawn")

        assertThat(state.output).isEqualTo("lxfopv ef rnhr")
    }

    @Test
    fun `keyword is normalized`() {
        viewModel.onKeywordChange("ke-y 1")

        assertThat(state.keyword).isEqualTo("KEY")
    }

    @Test
    fun `empty keyword is reported and clears the output`() {
        viewModel.onInputChange("Hello")

        viewModel.onKeywordChange("123")

        assertThat(state.isKeywordValid).isFalse()
        assertThat(state.output).isEmpty()
        assertThat(state.alignment).isEmpty()
    }

    @Test
    fun `alignment pairs each letter with its key letter and skips non letters`() {
        viewModel.onInputChange("at, ta")

        assertThat(state.alignment).containsExactly(
            AlignedLetter(input = 'a', key = 'L', output = 'l'),
            AlignedLetter(input = 't', key = 'E', output = 'x'),
            AlignedLetter(input = 't', key = 'M', output = 'f'),
            AlignedLetter(input = 'a', key = 'O', output = 'o'),
        ).inOrder()
    }

    @Test
    fun `alignment is capped`() {
        viewModel.onInputChange("a".repeat(100))

        assertThat(state.alignment).hasSize(VigenereUiState.MAX_ALIGNED_LETTERS)
    }

    @Test
    fun `decrypt mode decrypts the input`() {
        viewModel.onModeChange(CipherMode.DECRYPT)
        viewModel.onInputChange("LXFOPVEFRNHR")

        assertThat(state.output).isEqualTo("ATTACKATDAWN")
    }

    @Test
    fun `use output as input flips the mode and restores the plaintext`() {
        viewModel.onInputChange("Attack at dawn")

        viewModel.onUseOutputAsInput()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.output).isEqualTo("Attack at dawn")
    }

    @Test
    fun `loading the sample switches to decrypt and runs the analysis`() {
        viewModel.onLoadSample()

        assertThat(state.mode).isEqualTo(CipherMode.DECRYPT)
        assertThat(state.kasiski).isNotNull()
        assertThat(state.kasiski?.key?.length).isIn(4..9)
    }

    @Test
    fun `using the cracked key reveals the sample`() {
        viewModel.onLoadSample()

        viewModel.onUseCrackedKey()

        assertThat(state.output).isEqualTo(SampleText.KASISKI)
    }

    @Test
    fun `no analysis in encrypt mode`() {
        viewModel.onInputChange(SampleText.KASISKI)

        assertThat(state.kasiski).isNull()
    }
}
