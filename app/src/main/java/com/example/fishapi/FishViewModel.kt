package com.example.fishapi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FishViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FishUiState())
    val uiState: StateFlow<FishUiState> = _uiState.asStateFlow()

    init {
        loadFish()
    }

    private fun loadFish() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val fishList = ApiClient.instance.getAllFish()
                _uiState.update {
                    it.copy(
                        fishList = fishList,
                        filteredFishList = fishList,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Failed to load fish",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun searchFish(query: String) {
        _uiState.update { currentState ->
            val filteredList = if (query.isBlank()) {
                currentState.fishList
            } else {
                currentState.fishList.filter { fish ->
                    fish.title.contains(query, ignoreCase = true)
                }
            }
            currentState.copy(
                searchQuery = query,
                filteredFishList = filteredList
            )
        }
    }

    fun filterFish(filterType: FilterType) {
        _uiState.update { currentState ->
            val filteredList = when (filterType) {
                FilterType.ALL -> currentState.fishList
                FilterType.SMALL -> currentState.fishList.filter { fish ->
                    fish.title.length <= 5
                }
                FilterType.LARGE -> currentState.fishList.filter { fish ->
                    fish.title.length > 5
                }
            }
            currentState.copy(
                currentFilter = filterType,
                filteredFishList = filteredList
            )
        }
    }

    // Add this function to reset error message
    fun resetErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

data class FishUiState(
    val fishList: List<Fish> = emptyList(),
    val filteredFishList: List<Fish> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val currentFilter: FilterType = FilterType.ALL
)

enum class FilterType {
    ALL, SMALL, LARGE
}