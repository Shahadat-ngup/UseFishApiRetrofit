package com.example.fishapi

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FishScreen(navController: NavController) {
    val viewModel: FishViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        Log.d("FishScreen", "Fish count: ${uiState.filteredFishList.size}")
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.resetErrorMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = { viewModel.searchFish(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    FilterChips(
                        currentFilter = uiState.currentFilter,
                        onFilterSelected = { viewModel.filterFish(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (uiState.isLoading) {
                    item {
                        LoadingIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp)
                        )
                    }
                } else {
                    items(uiState.filteredFishList) { fish ->
                        val commentMap = uiState.comments.groupBy { it.fishId }
                        FishCard(
                            fish = fish,
                            comments = commentMap[fish.id] ?: emptyList(),
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                navController.navigate("fishDetail/${fish.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}
