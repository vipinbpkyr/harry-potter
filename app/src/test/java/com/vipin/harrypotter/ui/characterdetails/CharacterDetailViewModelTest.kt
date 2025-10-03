package com.vipin.harrypotter.ui.characterdetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.usecase.GetCharacterDetailsUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
class CharacterDetailViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCharacterDetailsUseCase: GetCharacterDetailsUseCase
    private lateinit var viewModel: CharacterDetailViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        Dispatchers.setMain(testDispatcher)
        getCharacterDetailsUseCase = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test view model init, character details are fetched and ui state is updated`() = runTest {
        // Given
        val character = CharacterEntity(
            id = "123",
            name = "Harry Potter",
            actor = "Daniel Radcliffe",
            species = "human",
            house = "Gryffindor",
            image = "image_url",
            dateOfBirth = "31-07-1980",
            alive = true
        )
        val savedStateHandle = SavedStateHandle().apply {
            set("characterId", "123")
        }
        every { getCharacterDetailsUseCase("123") } returns flowOf(character)

        // When
        viewModel = CharacterDetailViewModel(getCharacterDetailsUseCase, savedStateHandle)

        // Then
        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            val successState = awaitItem()
            assertEquals(character, successState.character)
            assertEquals(false, successState.isLoading)
            assertEquals(null, successState.error)
        }
    }
}
