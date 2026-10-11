package com.danyastry.encryption.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.theme.EncryptionTheme

@Composable
fun SecurityStatusBadge(status: SecurityStatus, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val (container, content) = when (status) {
        SecurityStatus.RECOMMENDED -> colors.primaryContainer to colors.onPrimaryContainer
        SecurityStatus.PERFECT_SECRECY -> colors.secondaryContainer to colors.onSecondaryContainer
        SecurityStatus.LEGACY -> colors.tertiaryContainer to colors.onTertiaryContainer
        SecurityStatus.BROKEN -> colors.errorContainer to colors.onErrorContainer
        SecurityStatus.NO_SECURITY -> colors.surfaceVariant to colors.onSurfaceVariant
    }
    StatusPill(
        text = stringResource(status.labelRes),
        container = container,
        content = content,
        modifier = modifier,
    )
}

@Composable
private fun StatusPill(text: String, container: Color, content: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = container,
        contentColor = content,
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

@Preview
@Composable
private fun SecurityStatusBadgePreview() {
    EncryptionTheme {
        SecurityStatusBadge(SecurityStatus.RECOMMENDED)
    }
}
