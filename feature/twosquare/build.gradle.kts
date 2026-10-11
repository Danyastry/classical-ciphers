plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.twosquare"
}

dependencies {
    implementation(projects.core.classical)
}
