package com.danyastry.encryption.core.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danyastry.encryption.core.ui.theme.EncryptionTheme

@Immutable
data class LetterCell(val letter: Char, val color: Color)

@Immutable
data class LetterRow(val cells: List<LetterCell>, val label: String? = null) {
    companion object {
        fun uniform(letters: List<Char>, color: Color, label: String? = null) =
            LetterRow(letters.map { LetterCell(it, color) }, label)
    }
}

@Composable
fun LetterTable(
    title: String,
    rows: List<LetterRow>,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                if (rows.any { it.label != null }) {
                    Column {
                        rows.forEach { row -> RowLabel(row.label.orEmpty()) }
                    }
                }
                Column(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    rows.forEach { row -> LetterCells(row.cells) }
                }
            }
        }
    }
}

@Composable
private fun RowLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(end = 12.dp),
    )
}

@Composable
private fun LetterCells(cells: List<LetterCell>) {
    Row {
        cells.forEach { cell ->
            Text(
                text = cell.letter.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = cell.color,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(20.dp),
            )
        }
    }
}

@Preview
@Composable
private fun LetterTablePreview() {
    EncryptionTheme {
        LetterTable(
            title = "How the keyword lines up",
            rows = listOf(
                LetterRow.uniform("ATTACK".toList(), MaterialTheme.colorScheme.onSurface, "Plaintext"),
                LetterRow.uniform("LEMONL".toList(), MaterialTheme.colorScheme.tertiary, "Key"),
                LetterRow.uniform("LXFOPV".toList(), MaterialTheme.colorScheme.primary, "Ciphertext"),
            ),
        )
    }
}
