package com.vipin.harrypotter.ui.characterlist

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import com.vipin.domain.entities.CharacterEntity
import com.vipin.harrypotter.ui.theme.HarryPotterTheme
import org.junit.Rule
import org.junit.Test

class CharacterListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

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
            val navController = TestNavHostController(composeTestRule.activity)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            HarryPotterTheme {
                NavHost(navController = navController, startDestination = "test_list") {
                    composable("test_list") {
                        CharacterListScreen(
                            uiState = uiState,
                            onLoadMoreClicked = { },
                            onRetryClicked = { },
                            navController = navController
                        )
                    }
                }
            }
        }

        composeTestRule.onNodeWithText("Harry Potter").assertIsDisplayed()
        composeTestRule.onNodeWithText("Actor: Daniel Radcliffe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hermione Granger").assertIsDisplayed()
        composeTestRule.onNodeWithText("Actor: Emma Watson").assertIsDisplayed()
    }
}
