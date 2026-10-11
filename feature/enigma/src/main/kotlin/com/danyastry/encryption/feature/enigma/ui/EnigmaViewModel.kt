package com.danyastry.encryption.feature.enigma.ui

import androidx.lifecycle.ViewModel
import com.danyastry.encryption.feature.enigma.EnigmaSettings
import com.danyastry.encryption.feature.enigma.Reflector
import com.danyastry.encryption.feature.enigma.Rotor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EnigmaViewModel : ViewModel() {

    private val _state = MutableStateFlow(EnigmaUiState().recalculated())
    val state: StateFlow<EnigmaUiState> = _state.asStateFlow()

    fun onInputChange(input: String) = update { copy(input = input) }

    fun onRotorChange(slot: Int, rotor: Rotor) = update {
        val current = rotors.toMutableList()
        val previousSlot = current.indexOf(rotor)
        if (previousSlot >= 0) current[previousSlot] = current[slot]
        current[slot] = rotor
        copy(rotors = current)
    }

    fun onReflectorChange(reflector: Reflector) = update { copy(reflector = reflector) }

    fun onRingsChange(rings: String) = update { copy(rings = normalizeSetting(rings)) }

    fun onPositionsChange(positions: String) = update { copy(positions = normalizeSetting(positions)) }

    fun onPlugboardChange(plugboard: String) = update { copy(plugboard = plugboard.uppercase()) }

    fun onUseOutputAsInput() = update { copy(input = output) }

    fun onLoadBarbarossa() = update {
        copy(
            rotors = listOf(Rotor.II, Rotor.IV, Rotor.V),
            reflector = Reflector.B,
            rings = "BUL",
            positions = "BLA",
            plugboard = "AV BS CG DL FU HZ IN KM OW RX",
            input = BARBAROSSA_CIPHERTEXT,
        )
    }

    private fun normalizeSetting(text: String): String =
        text.uppercase().filter { it in 'A'..'Z' }.take(EnigmaSettings.ROTOR_COUNT)

    private fun update(transform: EnigmaUiState.() -> EnigmaUiState) {
        _state.update { it.transform().recalculated() }
    }

    companion object {
        const val BARBAROSSA_CIPHERTEXT =
            "EDPUD NRGYS ZRCXN UYTPO MRMBO FKTBZ REZKM LXLVE FGUEY SIOZV EQMIK UBPMM YLKLT TDEIS " +
                "MDICA GYKUA CTCDO MOHWX MUUIA UBSTS LRNBZ SZWNR FXWFY SSXJZ VIJHI DISHP RKLKA YUPAD " +
                "TXQSP INQMA TLPIF SVKDA SCTAC DPBOP VHJK"
    }
}
