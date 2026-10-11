plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.bacon"
}

dependencies {
    implementation(projects.core.classical)
}
