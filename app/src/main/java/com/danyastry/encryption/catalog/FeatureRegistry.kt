package com.danyastry.encryption.catalog

import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.affine.AffineFeature
import com.danyastry.encryption.feature.atbash.AtbashFeature
import com.danyastry.encryption.feature.caesar.CaesarFeature
import com.danyastry.encryption.feature.rot.RotFeature

object FeatureRegistry {
    val features: List<FeatureEntry> = listOf(
        CaesarFeature,
        RotFeature,
        AtbashFeature,
        AffineFeature,
    )
}
