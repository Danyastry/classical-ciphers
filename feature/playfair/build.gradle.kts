plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.playfair"
}

dependencies {
    implementation(projects.core.classical)
}
