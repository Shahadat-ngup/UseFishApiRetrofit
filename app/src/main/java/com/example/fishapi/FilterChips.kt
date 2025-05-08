package com.example.fishapi

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding

@Composable
fun FilterChips(
    currentFilter: FilterType,
    onFilterSelected: (FilterType) -> Unit,
    modifier: Modifier
) {
    Row {
        FilterType.values().forEach { filter ->
            AssistChip(
                onClick = { onFilterSelected(filter) },
                label = { Text(filter.name) },
                colors = AssistChipDefaults.assistChipColors(),
                elevation = AssistChipDefaults.assistChipElevation(),
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}
