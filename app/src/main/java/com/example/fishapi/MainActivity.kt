package com.example.fishapi

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fishapi.ui.theme.FishAppTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedGetBackStackEntry")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FishAppTheme {
                // Wrap NavHost in another Surface to ensure proper sizing
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "fishList",
                        modifier = Modifier.fillMaxSize() // Add this modifier
                    ) {
                        composable("fishList") {
                            FishScreen(navController)
                        }
                        composable(
                            "fishDetail/{fishId}",
                            arguments = listOf(navArgument("fishId") { type = NavType.IntType })
                        ) {
                            val parentEntry = remember {
                                navController.getBackStackEntry("fishList")
                            }
                            val viewModel: FishViewModel = viewModel(parentEntry)
                            val fishId = it.arguments?.getInt("fishId") ?: -1
                            FishDetailScreen(navController, fishId, viewModel)
                        }
                    }
                }
            }
        }
    }
}