plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.onetimepad"
}

dependencies {
    implementation(projects.core.classical)
}
