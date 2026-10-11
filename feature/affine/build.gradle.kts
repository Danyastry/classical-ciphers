plugins {
    alias(libs.plugins.encryption.android.feature)
}

android {
    namespace = "com.danyastry.encryption.feature.affine"
}

dependencies {
    implementation(projects.core.classical)
}
