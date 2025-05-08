package com.example.fishapi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FishList(fishList: List<Fish>, comments: List<Comment>, viewModel: FishViewModel) {
    val commentMap = comments.groupBy { it.fishId }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(fishList) { fish ->
            FishCard(
                fish = fish,
                comments = commentMap[fish.id] ?: emptyList(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

