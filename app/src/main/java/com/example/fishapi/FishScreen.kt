package com.example.fishapi

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FishScreen() {
    val viewModel: FishViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value // Changed to collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.resetErrorMessage() // Add this function to your ViewModel
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.searchFish(it) }
            )
            FilterChips(
                currentFilter = uiState.currentFilter,
                onFilterSelected = { viewModel.filterFish(it) }
            )
            if (uiState.isLoading) {
                LoadingIndicator()
            } else {
                FishList(fishList = uiState.filteredFishList)
            }
        }
    }
}