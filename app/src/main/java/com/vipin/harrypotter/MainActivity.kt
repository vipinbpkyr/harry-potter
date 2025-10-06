package com.vipin.harrypotter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vipin.harrypotter.ui.characterdetails.CharacterDetailScreen
import com.vipin.harrypotter.ui.characterdetails.CharacterDetailViewModel
import com.vipin.harrypotter.ui.characterlist.CharacterListScreen
import com.vipin.harrypotter.ui.characterlist.CharacterListViewModel
import com.vipin.harrypotter.ui.charactersearch.CharacterSearchScreen
import com.vipin.harrypotter.ui.charactersearch.CharacterSearchViewModel
import com.vipin.harrypotter.ui.theme.HarryPotterTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute

@Serializable
object CharacterListRoute : AppRoute

@Serializable
data class CharacterDetailRoute(val characterId: String) : AppRoute

@Serializable
object SearchRoute : AppRoute

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HarryPotterTheme {
                AppNavgraph()
            }
        }
    }
}

@Composable
fun AppNavgraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CharacterListRoute,
    ) {
        composable<CharacterListRoute> {
            val viewModel: CharacterListViewModel = hiltViewModel()
            val uiState = viewModel.uiState.collectAsState().value

            CharacterListScreen(
                uiState = uiState,
                onLoadMoreClicked = viewModel::loadMoreCharacters,
                onRetryClicked = viewModel::retryInitialLoad,
                navController = navController
            )
        }

        composable<CharacterDetailRoute>(
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
        ) {
            val viewModel: CharacterDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            CharacterDetailScreen(
                uiState = uiState,
                onNavigateBack = navController::popBackStack,
                onRetry = {})
        }

        composable<SearchRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
            popExitTransition = { fadeOut() }
        ) {
            val viewModel: CharacterSearchViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            CharacterSearchScreen(
                uiState = uiState,
                onQueryChange = viewModel::onSearchQueryChanged,
                onNavigateBack = navController::popBackStack,
                onCharacterClick = { characterId ->
                    navController.navigate(CharacterDetailRoute(characterId))
                }
            )
        }
    }
}
