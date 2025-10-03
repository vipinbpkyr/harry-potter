package com.vipin.harrypotter.ui.character_detail

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vipin.domain.entities.CharacterEntity
import com.vipin.harrypotter.ui.theme.HarryPotterTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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
                title = { Text(uiState.character?.name ?: "Character Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
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
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRetry) {
                            Text("Retry")
                        }
                    }
                }
                uiState.character != null -> {
                    CharacterDetailsContent(character = uiState.character)
                }
                else -> {
                    Text("Character not found.", style = MaterialTheme.typography.bodyLarge)
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.image)
                .crossfade(true)
                .error(android.R.drawable.ic_menu_gallery) // Placeholder for error
                .placeholder(android.R.drawable.ic_menu_gallery) // Placeholder for loading
                .build(),
            contentDescription = character.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )
        Text(text = character.name, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Actor: ${character.actor.takeIf { it.isNotBlank() } ?: "Unknown"}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Species: ${character.species.takeIf { it.isNotBlank() } ?: "Unknown"}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Status: ${if (character.alive) "Alive" else "Dead"}",
            style = MaterialTheme.typography.bodyLarge
        )
        character.dateOfBirth?.let {
            val formattedDate = try {
                val inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
                LocalDate.parse(it, inputFormatter).format(outputFormatter)
            } catch (e: Exception) {
                "N/A"
            }
            Text(text = "Date of Birth: $formattedDate", style = MaterialTheme.typography.bodyLarge)
        } ?: Text(text = "Date of Birth: N/A", style = MaterialTheme.typography.bodyLarge)

        // Add more details
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
            uiState = CharacterDetailUiState(error = "Failed to load character details. Please try again."),
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
