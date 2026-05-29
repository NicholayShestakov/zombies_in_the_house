package gui

import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class InitViewModelTest {
    @Test
    fun `uiState contains defaults and loaded statistics`() {
        val statisticsFile = Files.createTempDirectory("zombies-init-statistics").resolve("statistics.properties")
        val repository = PlayerStatisticsRepository(statisticsFile)
        repository.recordFinalGame(FinalGameUiModel(isWin = true, winnerNames = listOf("Alice")))

        val viewModel = InitViewModel(repository)
        val uiState = viewModel.uiState.value

        assertEquals(1, uiState.playerCount)
        assertEquals(listOf("Player 1"), uiState.playerNames)
        assertEquals(3, uiState.startHealth)
        assertEquals(listOf(PlayerStatisticsUiModel(name = "Alice", wins = 1)), uiState.statistics)
        assertNull(uiState.errorMessage)
    }

    @Test
    fun `changePlayerCount updates player names list`() {
        val viewModel = InitViewModel(PlayerStatisticsRepository(tempStatisticsFile()))

        viewModel.changePlayerCount(3)

        assertEquals(3, viewModel.uiState.value.playerCount)
        assertEquals(listOf("Player 1", "Player 2", "Player 3"), viewModel.uiState.value.playerNames)
    }

    @Test
    fun `changePlayerName and changeStartHealth update uiState`() {
        val viewModel = InitViewModel(PlayerStatisticsRepository(tempStatisticsFile()))

        viewModel.changePlayerName(0, "Alice")
        viewModel.changeStartHealth(5)

        assertEquals(listOf("Alice"), viewModel.uiState.value.playerNames)
        assertEquals(5, viewModel.uiState.value.startHealth)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `invalid player count keeps previous state and exposes error`() {
        val viewModel = InitViewModel(PlayerStatisticsRepository(tempStatisticsFile()))

        viewModel.changePlayerCount(0)

        assertEquals(1, viewModel.uiState.value.playerCount)
        assertNotNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `duplicate player name keeps previous name and exposes error`() {
        val viewModel = InitViewModel(PlayerStatisticsRepository(tempStatisticsFile()))
        viewModel.changePlayerCount(2)

        viewModel.changePlayerName(1, "Player 1")

        assertEquals(listOf("Player 1", "Player 2"), viewModel.uiState.value.playerNames)
        assertNotNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `invalid start health keeps previous health and exposes error`() {
        val viewModel = InitViewModel(PlayerStatisticsRepository(tempStatisticsFile()))

        viewModel.changeStartHealth(0)

        assertEquals(3, viewModel.uiState.value.startHealth)
        assertNotNull(viewModel.uiState.value.errorMessage)
    }

    private fun tempStatisticsFile() = Files.createTempDirectory("zombies-init-empty").resolve("statistics.properties")
}
