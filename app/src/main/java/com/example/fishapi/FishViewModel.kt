package com.example.fishapi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class FilterType {
    ALL, SMALL, LARGE;

    fun displayName(): String = when(this) {
        ALL -> "All Fish"
        SMALL -> "Small Fish (<10cm)"
        LARGE -> "Large Fish (≥10cm)"
    }
}


data class FishUiState(
    val fishList: List<Fish> = emptyList(),
    val filteredFishList: List<Fish> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val currentFilter: FilterType = FilterType.ALL
)

class FishViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FishUiState())
    val uiState: StateFlow<FishUiState> = _uiState.asStateFlow()

    private var allFish: List<Fish> = emptyList()
    private var allComments: List<Comment> = emptyList()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                allFish = ApiClient.instance.getAllFish()
                allComments = ApiClient.instance.getAllComments()

                _uiState.update {
                    it.copy(
                        fishList = allFish,
                        filteredFishList = allFish,
                        comments = allComments,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Failed to load data",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun searchFish(query: String) {
        _uiState.update { currentState ->
            val filteredList = if (query.isBlank()) {
                allFish
            } else {
                allFish.filter { fish ->
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
                FilterType.ALL -> allFish
                FilterType.SMALL -> allFish.filter { it.sizeCm < 10 }
                FilterType.LARGE -> allFish.filter { it.sizeCm >= 10 }
            }
            currentState.copy(
                currentFilter = filterType,
                filteredFishList = filteredList
            )
        }
    }

    fun getCommentsForFish(fishId: Int): List<Comment> {
        return allComments.filter { it.fishId == fishId }
    }

    fun resetErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}