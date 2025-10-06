package com.vipin.harrypotter.ui.characterdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vipin.domain.entities.CharacterEntity
import com.vipin.harrypotter.R
import com.vipin.harrypotter.ui.theme.HarryPotterTheme
import com.vipin.harrypotter.utils.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    uiState: CharacterDetailUiState,
    onNavigateBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.character?.name ?: stringResource(id = R.string.character_details_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading && uiState.character == null -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.error,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                        Button(onClick = onRetry) {
                            Text(stringResource(id = R.string.retry))
                        }
                    }
                }
                uiState.character != null -> {
                    CharacterDetailsContent(character = uiState.character)
                }
                else -> {
                    Text(stringResource(id = R.string.no_characters_found), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailsContent(character: CharacterEntity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.image)
                .crossfade(true)
                .error(android.R.drawable.ic_menu_gallery)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .build(),
            contentDescription = character.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.image_size))
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
        )
        Text(text = character.name, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

        Text(
            text = stringResource(id = R.string.character_actor, character.actor.takeIf { it.isNotBlank() } ?: stringResource(id = R.string.unknown)),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = stringResource(id = R.string.character_species, character.species.takeIf { it.isNotBlank() } ?: stringResource(id = R.string.unknown)),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = stringResource(
                id = R.string.character_status,
                if (character.alive) stringResource(id = R.string.character_status_alive) else stringResource(id = R.string.character_status_dead)
            ),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(text = stringResource(id = R.string.character_date_of_birth, formatDate(character.dateOfBirth)), style = MaterialTheme.typography.bodyLarge)

    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun CharacterDetailScreenPreview_Loading() {
    HarryPotterTheme {
        CharacterDetailScreen(
            uiState = CharacterDetailUiState(isLoading = true),
            onNavigateBack = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun CharacterDetailScreenPreview_Error() {
    HarryPotterTheme {
        CharacterDetailScreen(
            uiState = CharacterDetailUiState(error = stringResource(id = R.string.character_detail_preview_error)),
            onNavigateBack = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Not Found State")
@Composable
fun CharacterDetailScreenPreview_NotFound() {
    HarryPotterTheme {
        CharacterDetailScreen(
            uiState = CharacterDetailUiState(character = null, isLoading = false, error = null),
            onNavigateBack = {},
            onRetry = {}
        )
    }
}
