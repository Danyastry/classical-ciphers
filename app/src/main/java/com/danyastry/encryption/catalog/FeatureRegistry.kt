package com.danyastry.encryption.catalog

import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.caesar.CaesarFeature

object FeatureRegistry {
    val features: List<FeatureEntry> = listOf(
        CaesarFeature,
    )
}
