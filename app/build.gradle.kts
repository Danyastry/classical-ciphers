plugins {
    alias(libs.plugins.encryption.android.application)
    alias(libs.plugins.encryption.android.application.compose)
}

android {
    namespace = "com.danyastry.encryption"

    defaultConfig {
        applicationId = "com.danyastry.encryption"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.core.cryptoApi)
    implementation(projects.core.ui)

    implementation(projects.feature.caesar)
    implementation(projects.feature.rot)
    implementation(projects.feature.atbash)
    implementation(projects.feature.affine)
    implementation(projects.feature.substitution)
    implementation(projects.feature.vigenere)
    implementation(projects.feature.beaufort)
    implementation(projects.feature.autokey)
    implementation(projects.feature.playfair)
    implementation(projects.feature.hill)
    implementation(projects.feature.railfence)
    implementation(projects.feature.columnar)
    implementation(projects.feature.scytale)
    implementation(projects.feature.bacon)
    implementation(projects.feature.polybius)
    implementation(projects.feature.adfgvx)
    implementation(projects.feature.bifid)
    implementation(projects.feature.trifid)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3)
}
