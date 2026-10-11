package com.danyastry.encryption.feature.substitution

object SampleText {
    const val FREQUENCY_ANALYSIS =
        "Frequency analysis is the oldest way to break a substitution cipher. In English the letter E appears more " +
            "often than any other, followed by T, A, O, I and N. A cipher that always replaces each letter with the same " +
            "symbol keeps these patterns, so the most common symbol in the message is probably E. The Arab scholar Al " +
            "Kindi described this method more than a thousand years ago. With enough text, the first guess already " +
            "reveals short words such as the and and, and the remaining letters fall into place one by one. This is why " +
            "a simple substitution cipher can be solved with pencil and paper, even though it has more possible keys " +
            "than there are stars in the sky."
}
