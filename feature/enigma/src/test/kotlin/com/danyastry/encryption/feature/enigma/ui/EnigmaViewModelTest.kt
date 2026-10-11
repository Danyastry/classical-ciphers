package com.danyastry.encryption.feature.enigma.ui

import com.danyastry.encryption.feature.enigma.PlugboardError
import com.danyastry.encryption.feature.enigma.Reflector
import com.danyastry.encryption.feature.enigma.Rotor
import com.danyastry.encryption.feature.enigma.Stage
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EnigmaViewModelTest {

    private val viewModel = EnigmaViewModel()

    private val state
        get() = viewModel.state.value

    @Test
    fun `starts with rotors I II III and reflector B`() {
        assertThat(state.rotors).containsExactly(Rotor.I, Rotor.II, Rotor.III).inOrder()
        assertThat(state.reflector).isEqualTo(Reflector.B)
        assertThat(state.output).isEmpty()
        assertThat(state.lastPath).isEmpty()
    }

    @Test
    fun `encrypts input in groups of five and tracks the windows`() {
        viewModel.onInputChange("AAAAA AAAAA")

        assertThat(state.output).startsWith("BDZGO ")
        assertThat(state.finalWindow).isEqualTo("AAK")
        assertThat(state.lastPath[Stage.KEY]).isEqualTo('A')
    }

    @Test
    fun `picking a rotor used in another slot swaps them`() {
        viewModel.onRotorChange(slot = 0, rotor = Rotor.III)

        assertThat(state.rotors).containsExactly(Rotor.III, Rotor.II, Rotor.I).inOrder()
    }

    @Test
    fun `picking an unused rotor replaces the slot`() {
        viewModel.onRotorChange(slot = 1, rotor = Rotor.V)

        assertThat(state.rotors).containsExactly(Rotor.I, Rotor.V, Rotor.III).inOrder()
    }

    @Test
    fun `settings are normalized and validated`() {
        viewModel.onPositionsChange("a-d u!z")
        assertThat(state.positions).isEqualTo("ADU")

        viewModel.onRingsChange("b")
        assertThat(state.isRingsValid).isFalse()
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `plugboard errors are reported`() {
        viewModel.onInputChange("hello")

        viewModel.onPlugboardChange("AV A")

        assertThat(state.plugboardError).isEqualTo(PlugboardError.ODD_LETTERS)
        assertThat(state.output).isEmpty()
    }

    @Test
    fun `loading the barbarossa message decrypts it`() {
        viewModel.onLoadBarbarossa()

        assertThat(state.rotors).containsExactly(Rotor.II, Rotor.IV, Rotor.V).inOrder()
        assertThat(state.output.replace(" ", "")).startsWith("AUFKLXABTEILUNGXVONXKURTINOWA")
    }

    @Test
    fun `use output as input gives the plaintext back`() {
        viewModel.onInputChange("Attack at dawn")

        viewModel.onUseOutputAsInput()

        assertThat(state.output.replace(" ", "")).isEqualTo("ATTACKATDAWN")
    }
}
