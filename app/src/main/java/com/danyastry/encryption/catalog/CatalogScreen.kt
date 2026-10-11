package com.danyastry.encryption.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.cryptoapi.SecurityStatus
import com.danyastry.encryption.core.ui.FeatureEntry
import com.danyastry.encryption.core.ui.SecurityStatusBadge
import com.danyastry.encryption.core.ui.descriptionRes
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
        item(key = LEGEND_KEY) {
            StatusLegend(catalog.statuses)
        }
        catalog.sections.forEach { section ->
            item(key = section.category.name) {
                SectionHeader(section.category)
            }
            items(section.entries, key = { it.info.id }) { entry ->
                FeatureRow(entry, onClick = { onFeatureClick(entry.info.id) })
            }
        }
    }
}

@Composable
private fun StatusLegend(statuses: List<SecurityStatus>) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.catalog_legend_title),
                style = MaterialTheme.typography.titleSmall,
            )
            statuses.forEach { status ->
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.width(LEGEND_BADGE_WIDTH)) {
                        SecurityStatusBadge(status)
                    }
                    Text(
                        text = stringResource(status.descriptionRes),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(category: AlgorithmCategory) {
    Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp)) {
        Text(
            text = stringResource(category.titleRes),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(category.descriptionRes),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private const val LEGEND_KEY = "legend"
private val LEGEND_BADGE_WIDTH = 108.dp

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
