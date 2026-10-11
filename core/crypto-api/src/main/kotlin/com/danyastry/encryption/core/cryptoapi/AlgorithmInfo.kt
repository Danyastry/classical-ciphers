package com.danyastry.encryption.core.cryptoapi

data class AlgorithmInfo(
    val id: String,
    val name: String,
    val category: AlgorithmCategory,
    val securityStatus: SecurityStatus,
    val summary: String,
) {
    init {
        require(ID_PATTERN.matches(id)) { "Algorithm id must be lowercase kebab-case, was '$id'" }
        require(name.isNotBlank()) { "Algorithm name must not be blank" }
    }

    private companion object {
        val ID_PATTERN = Regex("[a-z0-9]+(-[a-z0-9]+)*")
    }
}
