package com.vipin.harrypotter.ui.characterlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.vipin.domain.entities.CharacterEntity
import com.vipin.harrypotter.CharacterDetailRoute
import com.vipin.harrypotter.R
import com.vipin.harrypotter.ui.theme.Gryffindor
import com.vipin.harrypotter.ui.theme.HarryPotterTheme
import com.vipin.harrypotter.ui.theme.Hufflepuff
import com.vipin.harrypotter.ui.theme.Ravenclaw
import com.vipin.harrypotter.ui.theme.Slytherin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    uiState: CharacterListUiState,
    onSearchQueryChanged: (String) -> Unit,
    onLoadMoreClicked: () -> Unit,
    onRetryClicked: () -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.character_list_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = onSearchQueryChanged,
                onClear = {
                    onSearchQueryChanged("")
                }
            )

            when {
                uiState.isLoadingInitial -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null && uiState.characters.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.character_list_error, uiState.error),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRetryClicked) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
                uiState.characters.isEmpty() && !uiState.isLoadingInitial -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_characters_found))
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f) // Make LazyColumn take available space
                    ) {
                        items(uiState.characters) { character ->
                            CharacterListItem(character = character) {
                                navController.navigate(CharacterDetailRoute(character.id))
                            }
                        }
                        item {
                            if (uiState.searchQuery.isEmpty()) {
                                if (uiState.canLoadMore && !uiState.isLoadingMore) {
                                    Button(
                                        onClick = onLoadMoreClicked,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(stringResource(R.string.load_more))
                                    }
                                }
                                if (uiState.isLoadingMore) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, onClear: () -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text(stringResource(R.string.search_placeholder)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_search)
                    )
                }
            }
        }
    )
}

@Composable
fun CharacterListItem(character: CharacterEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HouseColorIndicator(house = character.house)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = character.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = stringResource(R.string.character_actor, character.actor),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.character_species, character.species),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun HouseColorIndicator(house: String?) {
    val color = when (house) {
        "Gryffindor" -> Gryffindor
        "Slytherin" -> Slytherin
        "Ravenclaw" -> Ravenclaw
        "Hufflepuff" -> Hufflepuff
        else -> Color.Transparent
    }
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(color = color, shape = CircleShape)
            .then(if (color == Color.Transparent) Modifier else Modifier)
    )
}

@Preview(showBackground = true, name = "Character List - Populated")
@Composable
fun CharacterListScreenPreview_Populated() {
    val navController = rememberNavController()
    val sampleCharacters = List(5) { i ->
        CharacterEntity(
            id = "$i",
            name = "Character Name ${i + 1}",
            actor = "Actor Name ${i + 1}",
            house = when (i % 4) {
                0 -> "Gryffindor"
                1 -> "Slytherin"
                2 -> "Ravenclaw"
                else -> "Hufflepuff"
            },
            species = "Human",
            image = "",
            dateOfBirth = null,
            alive = true
        )
    }
    val sampleUiState = CharacterListUiState(
        characters = sampleCharacters,
        isLoadingInitial = false,
        isLoadingMore = false,
        searchQuery = "",
        canLoadMore = true,
        error = null
    )
    HarryPotterTheme {
        CharacterListScreen(
            uiState = sampleUiState,
            onSearchQueryChanged = { /* No-op */ },
            onLoadMoreClicked = { /* No-op */ },
            onRetryClicked = { /* No-op */ },
            navController = navController
        )
    }
}

@Preview(showBackground = true, name = "Character List - Loading Initial")
@Composable
fun CharacterListScreenPreview_LoadingInitial() {
    val navController = rememberNavController()
    val sampleUiState = CharacterListUiState(
        characters = emptyList(),
        isLoadingInitial = true,
        searchQuery = "",
        error = null
    )
    HarryPotterTheme {
        CharacterListScreen(
            uiState = sampleUiState,
            onSearchQueryChanged = { /* No-op */ },
            onLoadMoreClicked = { /* No-op */ },
            onRetryClicked = { /* No-op */ },
            navController = navController
        )
    }
}

@Preview(showBackground = true, name = "Character List - Error")
@Composable
fun CharacterListScreenPreview_Error() {
    val navController = rememberNavController()
    val sampleUiState = CharacterListUiState(
        characters = emptyList(),
        isLoadingInitial = false,
        searchQuery = "",
        error = "Network request failed"
    )
    HarryPotterTheme {
        CharacterListScreen(
            uiState = sampleUiState,
            onSearchQueryChanged = { /* No-op */ },
            onLoadMoreClicked = { /* No-op */ },
            onRetryClicked = { /* No-op */ },
            navController = navController
        )
    }
}

@Preview(showBackground = true, name = "Character List - Empty")
@Composable
fun CharacterListScreenPreview_Empty() {
    val navController = rememberNavController()
    val sampleUiState = CharacterListUiState(
        characters = emptyList(),
        isLoadingInitial = false,
        searchQuery = "Unknown Character",
        canLoadMore = false, // Important for empty state
        error = null
    )
    HarryPotterTheme {
        CharacterListScreen(
            uiState = sampleUiState,
            onSearchQueryChanged = { /* No-op */ },
            onLoadMoreClicked = { /* No-op */ },
            onRetryClicked = { /* No-op */ },
            navController = navController
        )
    }
}

@Preview(showBackground = true, name = "Character List - Loading More")
@Composable
fun CharacterListScreenPreview_LoadingMore() {
    val navController = rememberNavController()
    val sampleCharacters = List(5) { i ->
        CharacterEntity(
            id = "$i",
            name = "Character Name ${i + 1}",
            actor = "Actor Name ${i + 1}",
            house = "Gryffindor",
            species = "Human",
            image = "",
            dateOfBirth = null,
            alive = true
        )
    }
    val sampleUiState = CharacterListUiState(
        characters = sampleCharacters,
        isLoadingInitial = false,
        isLoadingMore = true,
        searchQuery = "",
        canLoadMore = true,
        error = null
    )
    HarryPotterTheme {
        CharacterListScreen(
            uiState = sampleUiState,
            onSearchQueryChanged = { /* No-op */ },
            onLoadMoreClicked = { /* No-op */ },
            onRetryClicked = { /* No-op */ },
            navController = navController
        )
    }
}
