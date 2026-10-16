plugins {
    alias(libs.plugins.encryption.jvm.library)
}

dependencies {
    api(projects.core.cryptoApi)
    api(projects.core.classical)
    api(libs.junit)
    api(libs.truth)
}
