package com.vipin.harrypotter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
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
import com.vipin.harrypotter.ui.theme.HarryPotterTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute

@Serializable
object CharacterListRoute : AppRoute

@Serializable
data class CharacterDetailRoute(val characterId: String) : AppRoute

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
        composable<CharacterListRoute>(
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(300)
                )
            },
        ) {
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

        composable<CharacterDetailRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 1000 },
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            val viewModel: CharacterDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            CharacterDetailScreen(
                uiState = uiState,
                onNavigateBack = navController::popBackStack,
                onRetry = {})
        }
    }
}
