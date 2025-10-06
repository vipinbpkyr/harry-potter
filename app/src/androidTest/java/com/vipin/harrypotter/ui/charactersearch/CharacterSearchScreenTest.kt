package com.vipin.harrypotter.ui.charactersearch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.vipin.domain.entities.CharacterEntity
import com.vipin.harrypotter.R
import com.vipin.harrypotter.ui.theme.HarryPotterTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class CharacterSearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun characterSearchScreen_whenLoading_showsProgressIndicator() {
        // Given
        val uiState = CharacterSearchUiState(isLoading = true)

        // When
        composeTestRule.setContent {
            HarryPotterTheme {
                CharacterSearchScreen(
                    uiState = uiState,
                    onQueryChange = {},
                    onNavigateBack = {},
                    onCharacterClick = {}
                )
            }
        }

        // Then
        val loadingDescription = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.loading)
        composeTestRule.onNodeWithContentDescription(loadingDescription).assertIsDisplayed()
    }

    @Test
    fun characterSearchScreen_whenSuccess_showsCharacterList() {
        // Given
        val characters = listOf(
            CharacterEntity(id = "1", name = "Harry Potter", actor = "Daniel Radcliffe", species = "human", house = "Gryffindor", image = "", dateOfBirth = "", alive = true),
            CharacterEntity(id = "2", name = "Ron Weasley", actor = "Rupert Grint", species = "human", house = "Gryffindor", image = "", dateOfBirth = "", alive = true)
        )
        val uiState = CharacterSearchUiState(characters = characters)

        // When
        composeTestRule.setContent {
            HarryPotterTheme {
                CharacterSearchScreen(
                    uiState = uiState,
                    onQueryChange = {},
                    onNavigateBack = {},
                    onCharacterClick = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Harry Potter").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ron Weasley").assertIsDisplayed()
    }

    @Test
    fun characterSearchScreen_whenError_showsErrorMessage() {
        // Given
        val errorMessage = "Network error"
        val uiState = CharacterSearchUiState(error = errorMessage)

        // When
        composeTestRule.setContent {
            HarryPotterTheme {
                CharacterSearchScreen(
                    uiState = uiState,
                    onQueryChange = {},
                    onNavigateBack = {},
                    onCharacterClick = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun whenCharacterClicked_callsOnCharacterClick() {
        // Given
        val onCharacterClick: (String) -> Unit = mockk(relaxed = true)
        val characters = listOf(CharacterEntity(id = "1", name = "Harry Potter", actor = "Daniel Radcliffe", species = "human", house = "Gryffindor", image = "", dateOfBirth = "", alive = true))
        val uiState = CharacterSearchUiState(characters = characters)

        // When
        composeTestRule.setContent {
            HarryPotterTheme {
                CharacterSearchScreen(
                    uiState = uiState,
                    onQueryChange = {},
                    onNavigateBack = {},
                    onCharacterClick = onCharacterClick
                )
            }
        }
        composeTestRule.onNodeWithText("Harry Potter").performClick()

        // Then
        verify { onCharacterClick("1") }
    }

    @Test
    fun whenNavigateBackClicked_callsOnNavigateBack() {
        // Given
        val onNavigateBack: () -> Unit = mockk(relaxed = true)
        val uiState = CharacterSearchUiState()

        // When
        composeTestRule.setContent {
            HarryPotterTheme {
                CharacterSearchScreen(
                    uiState = uiState,
                    onQueryChange = {},
                    onNavigateBack = onNavigateBack,
                    onCharacterClick = {}
                )
            }
        }
        val navigateBackDescription = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.navigate_back)
        composeTestRule.onNodeWithContentDescription(navigateBackDescription).performClick()

        // Then
        verify { onNavigateBack() }
    }
}
