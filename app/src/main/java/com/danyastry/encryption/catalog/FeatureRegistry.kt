package com.danyastry.encryption.catalog

import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.feature.adfgvx.AdfgvxFeature
import com.danyastry.encryption.feature.affine.AffineFeature
import com.danyastry.encryption.feature.atbash.AtbashFeature
import com.danyastry.encryption.feature.autokey.AutokeyFeature
import com.danyastry.encryption.feature.bacon.BaconFeature
import com.danyastry.encryption.feature.beaufort.BeaufortFeature
import com.danyastry.encryption.feature.bifid.BifidFeature
import com.danyastry.encryption.feature.caesar.CaesarCipher
import com.danyastry.encryption.feature.caesar.CaesarFeature
import com.danyastry.encryption.feature.columnar.ColumnarFeature
import com.danyastry.encryption.feature.cpp.CipherSet
import com.danyastry.encryption.feature.cpp.CppFeature
import com.danyastry.encryption.feature.enigma.EnigmaFeature
import com.danyastry.encryption.feature.foursquare.FourSquareFeature
import com.danyastry.encryption.feature.hill.HillFeature
import com.danyastry.encryption.feature.onetimepad.OneTimePadFeature
import com.danyastry.encryption.feature.playfair.PlayfairFeature
import com.danyastry.encryption.feature.polybius.PolybiusFeature
import com.danyastry.encryption.feature.railfence.RailFenceFeature
import com.danyastry.encryption.feature.rot.RotFeature
import com.danyastry.encryption.feature.scytale.ScytaleFeature
import com.danyastry.encryption.feature.substitution.SubstitutionFeature
import com.danyastry.encryption.feature.trifid.TrifidFeature
import com.danyastry.encryption.feature.twosquare.TwoSquareFeature
import com.danyastry.encryption.feature.vigenere.VigenereCipher
import com.danyastry.encryption.feature.vigenere.VigenereFeature
import com.danyastry.encryption.feature.xor.XorCipher
import com.danyastry.encryption.feature.xor.XorFeature

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
        PlayfairFeature,
        HillFeature,
        RailFenceFeature,
        ColumnarFeature,
        ScytaleFeature,
        BaconFeature,
        PolybiusFeature,
        AdfgvxFeature,
        BifidFeature,
        TrifidFeature,
        FourSquareFeature,
        TwoSquareFeature,
        EnigmaFeature,
        OneTimePadFeature,
        XorFeature,
        CppFeature(CipherSet(caesar = CaesarCipher(), vigenere = VigenereCipher(), xor = XorCipher())),
    )
}
