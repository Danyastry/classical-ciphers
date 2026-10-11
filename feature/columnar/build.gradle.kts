plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.columnar"
}

dependencies {
    implementation(projects.core.classical)
}
