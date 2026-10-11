plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.bifid"
}

dependencies {
    implementation(projects.core.classical)
}
