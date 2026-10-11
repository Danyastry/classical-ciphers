package com.danyastry.encryption

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.danyastry.encryption.catalog.FeatureCatalog
import com.danyastry.encryption.catalog.FeatureRegistry
import com.danyastry.encryption.core.ui.theme.EncryptionTheme
import com.danyastry.encryption.navigation.EncryptionNavHost

class MainActivity : ComponentActivity() {

    private val catalog = FeatureCatalog(FeatureRegistry.features)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EncryptionTheme {
                EncryptionNavHost(catalog)
            }
        }
    }
}
