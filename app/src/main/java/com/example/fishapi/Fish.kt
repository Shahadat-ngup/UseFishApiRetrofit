package com.example.fishapi

data class Fish(
    val id: Int,
    val title: String,
    val imageUrl: String
) {
    // Calculate size based on title (fallback)
    val sizeCm: Int
        get() = when {
            title.contains("Betta", ignoreCase = true) -> 5
            title.contains("Goldfish", ignoreCase = true) -> 15
            title.contains("Guppy", ignoreCase = true) -> 4
            title.contains("Clownfish", ignoreCase = true) -> 8
            title.contains("Angelfish", ignoreCase = true) -> 12
            title.contains("Neon Tetra", ignoreCase = true) -> 3
            title.contains("Oscar", ignoreCase = true) -> 25
            title.contains("Zebra", ignoreCase = true) -> 5
            title.contains("Discus", ignoreCase = true) -> 15
            title.contains("Platies", ignoreCase = true) -> 5
            title.contains("Molly", ignoreCase = true) -> 6
            title.contains("Rainbow", ignoreCase = true) -> 8
            title.contains("Koi", ignoreCase = true) -> 30
            title.contains("Corydoras", ignoreCase = true) -> 5
            title.contains("Tetra", ignoreCase = true) -> 4
            else -> 10 // Default size
        }
}
