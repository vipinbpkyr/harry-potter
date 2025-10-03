package com.vipin.harrypotter.ui.characterdetails

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vipin.domain.entities.CharacterEntity
import com.vipin.harrypotter.ui.theme.HarryPotterTheme
import org.junit.Rule
import org.junit.Test

class CharacterDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun characterDetailScreen_displaysData_whenStateIsSuccess() {
        val character = CharacterEntity(
            id = "1",
            name = "Harry Potter",
            actor = "Daniel Radcliffe",
            species = "human",
            dateOfBirth = "31-07-1980",
            alive = true,
            house = "Gryffindor",
            image = ""
        )
        val uiState = CharacterDetailUiState(
            character = character,
            isLoading = false,
            error = null
        )

        composeTestRule.setContent {
            HarryPotterTheme {
                CharacterDetailScreen(
                    uiState = uiState,
                    onNavigateBack = { },
                    onRetry = { }
                )
            }
        }

        composeTestRule.onNode(hasText("Harry Potter") and hasAnySibling(hasText("Actor: Daniel Radcliffe"))).assertIsDisplayed()
        composeTestRule.onNodeWithText("Actor: Daniel Radcliffe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Species: human").assertIsDisplayed()
        composeTestRule.onNodeWithText("Status: Alive").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date of Birth: 31 Jul 1980").assertIsDisplayed()
    }
}