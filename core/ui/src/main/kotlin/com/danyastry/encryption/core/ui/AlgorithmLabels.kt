package com.danyastry.encryption.core.ui

import androidx.annotation.StringRes
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus

@get:StringRes
val AlgorithmCategory.titleRes: Int
    get() = when (this) {
        AlgorithmCategory.SUBSTITUTION -> R.string.category_substitution
        AlgorithmCategory.POLYALPHABETIC -> R.string.category_polyalphabetic
        AlgorithmCategory.TRANSPOSITION -> R.string.category_transposition
        AlgorithmCategory.TABLE_BASED -> R.string.category_table_based
        AlgorithmCategory.MACHINE -> R.string.category_machine
    }

@get:StringRes
val AlgorithmCategory.descriptionRes: Int
    get() = when (this) {
        AlgorithmCategory.SUBSTITUTION -> R.string.category_substitution_description
        AlgorithmCategory.POLYALPHABETIC -> R.string.category_polyalphabetic_description
        AlgorithmCategory.TRANSPOSITION -> R.string.category_transposition_description
        AlgorithmCategory.TABLE_BASED -> R.string.category_table_based_description
        AlgorithmCategory.MACHINE -> R.string.category_machine_description
    }

@get:StringRes
val SecurityStatus.labelRes: Int
    get() = when (this) {
        SecurityStatus.RECOMMENDED -> R.string.status_recommended
        SecurityStatus.PERFECT_SECRECY -> R.string.status_perfect_secrecy
        SecurityStatus.LEGACY -> R.string.status_legacy
        SecurityStatus.BROKEN -> R.string.status_broken
        SecurityStatus.NO_SECURITY -> R.string.status_no_security
    }

@get:StringRes
val SecurityStatus.descriptionRes: Int
    get() = when (this) {
        SecurityStatus.RECOMMENDED -> R.string.status_recommended_description
        SecurityStatus.PERFECT_SECRECY -> R.string.status_perfect_secrecy_description
        SecurityStatus.LEGACY -> R.string.status_legacy_description
        SecurityStatus.BROKEN -> R.string.status_broken_description
        SecurityStatus.NO_SECURITY -> R.string.status_no_security_description
    }
