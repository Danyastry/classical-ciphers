plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.caesar"
}

dependencies {
    implementation(projects.core.classical)
}
