package com.vipin.harrypotter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vipin.harrypotter.ui.character_list.CharacterListScreen
import com.vipin.harrypotter.ui.character_list.CharacterListViewModel
import com.vipin.harrypotter.ui.theme.HarryPotterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HarryPotterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavgraph()
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavgraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "character_list") {
        composable("character_list") {
            val viewModel: CharacterListViewModel = hiltViewModel()
            val uiState = viewModel.uiState.collectAsState().value

            CharacterListScreen(
                uiState = uiState,
                onSearchQueryChanged = viewModel::onSearchQueryChanged,
                onLoadMoreClicked = viewModel::loadMoreCharacters,
                onRetryClicked = viewModel::retryInitialLoad,
                navController = navController
            )
        }

        composable("character_detail/{characterId}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            // CharacterDetailScreen(characterId = characterId)
        }
    }
}