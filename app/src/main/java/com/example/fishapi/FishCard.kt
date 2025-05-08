package com.example.fishapi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishCard(fish: Fish, comments: List<Comment>, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { expanded = !expanded }
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(fish.imageUrl),
                contentDescription = fish.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = fish.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Size: ${fish.sizeCm} cm",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )

                if (expanded) {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        if (comments.isNotEmpty()) {
                            Text(
                                text = "Comments (${comments.size}):",
                                style = MaterialTheme.typography.bodySmall
                            )
                            comments.take(2).forEach { comment ->
                                Text(
                                    text = "• ${comment.body}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            if (comments.size > 2) {
                                Text(
                                    text = "+ ${comments.size - 2} more",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "No comments yet",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}