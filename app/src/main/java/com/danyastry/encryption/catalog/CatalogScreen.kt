package com.danyastry.encryption.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.danyastry.encryption.R
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.core.ui.SecurityStatusBadge
import com.danyastry.encryption.core.ui.titleRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    catalog: FeatureCatalog,
    onFeatureClick: (String) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
    ) { innerPadding ->
        if (catalog.isEmpty) {
            EmptyCatalog(Modifier.padding(innerPadding))
        } else {
            CatalogList(catalog, onFeatureClick, innerPadding)
        }
    }
}

@Composable
private fun CatalogList(
    catalog: FeatureCatalog,
    onFeatureClick: (String) -> Unit,
    contentPadding: PaddingValues,
) {
    LazyColumn(contentPadding = contentPadding) {
        catalog.sections.forEach { section ->
            item(key = section.category.name) {
                Text(
                    text = stringResource(section.category.titleRes),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp),
                )
            }
            items(section.entries, key = { it.info.id }) { entry ->
                FeatureRow(entry, onClick = { onFeatureClick(entry.info.id) })
            }
        }
    }
}

@Composable
private fun FeatureRow(entry: FeatureEntry, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(entry.info.name) },
        supportingContent = { Text(entry.info.summary) },
        trailingContent = { SecurityStatusBadge(entry.info.securityStatus) },
        modifier = Modifier.clickable(onClick = onClick),
    )
}

@Composable
private fun EmptyCatalog(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.catalog_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
