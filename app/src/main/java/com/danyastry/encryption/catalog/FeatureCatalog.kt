package com.danyastry.encryption.catalog

import com.danyastry.encryption.core.cryptoapi.AlgorithmCategory
import com.danyastry.encryption.core.ui.FeatureEntry

data class CatalogSection(
    val category: AlgorithmCategory,
    val entries: List<FeatureEntry>,
)

class FeatureCatalog(features: List<FeatureEntry>) {

    private val featuresById: Map<String, FeatureEntry>

    val sections: List<CatalogSection>

    init {
        val duplicateIds = features.groupingBy { it.info.id }.eachCount().filterValues { it > 1 }.keys
        require(duplicateIds.isEmpty()) { "Duplicate feature ids: ${duplicateIds.sorted()}" }

        featuresById = features.associateBy { it.info.id }
        sections = features
            .groupBy { it.info.category }
            .toSortedMap()
            .map { (category, entries) -> CatalogSection(category, entries.sortedBy { it.info.name }) }
    }

    val isEmpty: Boolean
        get() = featuresById.isEmpty()

    fun find(id: String): FeatureEntry? = featuresById[id]
}
