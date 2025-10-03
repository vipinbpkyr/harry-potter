package com.vipin.harrypotter.ui.characterlist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.vipin.domain.entities.CharacterEntity
import com.vipin.harrypotter.ui.theme.HarryPotterTheme
import org.junit.Rule
import org.junit.Test

class CharacterListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun characterListScreen_displaysData_whenStateIsSuccess() {
        val characters = listOf(
            CharacterEntity(
                id = "1",
                name = "Harry Potter",
                actor = "Daniel Radcliffe",
                species = "human",
                dateOfBirth = "31-07-1980",
                alive = true,
                house = "Gryffindor",
                image = ""
            ),
            CharacterEntity(
                id = "2",
                name = "Hermione Granger",
                actor = "Emma Watson",
                species = "human",
                dateOfBirth = "19-09-1979",
                alive = true,
                house = "Gryffindor",
                image = ""
            )
        )
        val uiState = CharacterListUiState(
            characters = characters,
            isLoadingInitial = false,
            error = null,
            searchQuery = "",
            canLoadMore = false,
            isLoadingMore = false
        )

        composeTestRule.setContent {
            HarryPotterTheme {
                CharacterListScreen(
                    uiState = uiState,
                    onSearchQueryChanged = { },
                    onLoadMoreClicked = { },
                    onRetryClicked = { },
                    navController = rememberNavController()
                )
            }
        }

        composeTestRule.onNodeWithText("Harry Potter").assertIsDisplayed()
        composeTestRule.onNodeWithText("Actor: Daniel Radcliffe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hermione Granger").assertIsDisplayed()
        composeTestRule.onNodeWithText("Actor: Emma Watson").assertIsDisplayed()
    }
}
