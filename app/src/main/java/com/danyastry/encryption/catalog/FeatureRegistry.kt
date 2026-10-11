package com.danyastry.encryption.catalog

import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.affine.AffineFeature
import com.danyastry.encryption.feature.atbash.AtbashFeature
import com.danyastry.encryption.feature.autokey.AutokeyFeature
import com.danyastry.encryption.feature.beaufort.BeaufortFeature
import com.danyastry.encryption.feature.caesar.CaesarFeature
import com.danyastry.encryption.feature.rot.RotFeature
import com.danyastry.encryption.feature.substitution.SubstitutionFeature
import com.danyastry.encryption.feature.vigenere.VigenereFeature

object FeatureRegistry {
    val features: List<FeatureEntry> = listOf(
        CaesarFeature,
        RotFeature,
        AtbashFeature,
        AffineFeature,
        SubstitutionFeature,
        VigenereFeature,
        BeaufortFeature,
        AutokeyFeature,
    )
}
