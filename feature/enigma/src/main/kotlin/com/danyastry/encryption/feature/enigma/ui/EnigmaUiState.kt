package com.danyastry.encryption.feature.enigma.ui

import com.danyastry.encryption.feature.enigma.EnigmaCipher
import com.danyastry.encryption.feature.enigma.EnigmaMachine
import com.danyastry.encryption.feature.enigma.EnigmaSettings
import com.danyastry.encryption.feature.enigma.Plugboard
import com.danyastry.encryption.feature.enigma.PlugboardError
import com.danyastry.encryption.feature.enigma.Reflector
import com.danyastry.encryption.feature.enigma.Rotor
import com.danyastry.encryption.feature.enigma.Stage

data class EnigmaUiState(
    val rotors: List<Rotor> = listOf(Rotor.I, Rotor.II, Rotor.III),
    val reflector: Reflector = Reflector.B,
    val rings: String = "AAA",
    val positions: String = "AAA",
    val plugboard: String = "",
    val plugboardError: PlugboardError? = null,
    val isRingsValid: Boolean = true,
    val isPositionsValid: Boolean = true,
    val input: String = "",
    val output: String = "",
    val finalWindow: String = "AAA",
    val lastPath: Map<Stage, Char> = emptyMap(),
) {
    companion object {
        const val GROUP_SIZE = 5
    }
}

internal fun EnigmaUiState.recalculated(): EnigmaUiState {
    val plugboardError = Plugboard.validate(plugboard)
    val ringsValid = EnigmaSettings.isValidSetting(rings)
    val positionsValid = EnigmaSettings.isValidSetting(positions)
    if (plugboardError != null || !ringsValid || !positionsValid) {
        return copy(
            plugboardError = plugboardError,
            isRingsValid = ringsValid,
            isPositionsValid = positionsValid,
            output = "",
            finalWindow = positions,
            lastPath = emptyMap(),
        )
    }

    val machine = EnigmaMachine(
        EnigmaSettings(rotors, reflector, rings, positions, Plugboard.parse(plugboard)),
    )
    var lastPath = emptyMap<Stage, Char>()
    val output = EnigmaCipher.normalize(input).map { key -> machine.pressWithPath(key).also { lastPath = it }.getValue(Stage.LAMP) }
    return copy(
        plugboardError = null,
        isRingsValid = true,
        isPositionsValid = true,
        output = output.chunked(EnigmaUiState.GROUP_SIZE).joinToString(" ") { it.joinToString("") },
        finalWindow = machine.window,
        lastPath = lastPath,
    )
}
