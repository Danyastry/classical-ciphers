package com.danyastry.encryption.core.ui

import androidx.annotation.StringRes
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus

@get:StringRes
val AlgorithmCategory.titleRes: Int
    get() = when (this) {
        AlgorithmCategory.CLASSICAL -> R.string.category_classical
        AlgorithmCategory.ENCODING -> R.string.category_encoding
        AlgorithmCategory.SYMMETRIC -> R.string.category_symmetric
        AlgorithmCategory.STREAM -> R.string.category_stream
        AlgorithmCategory.ASYMMETRIC -> R.string.category_asymmetric
        AlgorithmCategory.KEY_EXCHANGE -> R.string.category_key_exchange
        AlgorithmCategory.SIGNATURE -> R.string.category_signature
        AlgorithmCategory.HASH -> R.string.category_hash
        AlgorithmCategory.MAC -> R.string.category_mac
        AlgorithmCategory.KDF -> R.string.category_kdf
        AlgorithmCategory.ANDROID_PLATFORM -> R.string.category_android_platform
        AlgorithmCategory.PROTOCOL -> R.string.category_protocol
        AlgorithmCategory.ADVANCED -> R.string.category_advanced
        AlgorithmCategory.CRYPTANALYSIS -> R.string.category_cryptanalysis
    }

@get:StringRes
val SecurityStatus.labelRes: Int
    get() = when (this) {
        SecurityStatus.RECOMMENDED -> R.string.status_recommended
        SecurityStatus.LEGACY -> R.string.status_legacy
        SecurityStatus.BROKEN -> R.string.status_broken
        SecurityStatus.NO_SECURITY -> R.string.status_no_security
    }
