package pipelines

import game.FINISH
import game.START
import game.actions.ActionManager
import game.gameControl.GameContext
import game.gameControl.ReadOnlyGameContext
import game.testBoard
import game.testHuman
import game.testPlayer
import gui.FinalGameUiModel
import gui.GameViewModel
import gui.InitViewModel
import gui.PlayerStatisticsRepository
import gui.PlayerStatisticsUiModel
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals

class GuiFinalStatisticsPipeline {
    @Test
    fun `winning game gui state is saved and shown on initialization screen`() {
        val statisticsFile = Files.createTempDirectory("zombies-gui-final-statistics").resolve("statistics.properties")
        val statisticsRepository = PlayerStatisticsRepository(statisticsFile)
        val human = testHuman("cards.entity.human.test.name")
        val context =
            GameContext(
                board = testBoard(cards = mapOf(FINISH to listOf(human))),
                players = listOf(testPlayer("Alice", human)),
                localization = "ru",
            )
        repeat(context.winConditionsToWin) {
            context.incrementWinConditionsCount()
        }
        val gameViewModel = GameViewModel(ActionManager(context), ReadOnlyGameContext(context))

        val finalGame = gameViewModel.uiState.value.finalGame ?: error("Final game is expected")
        statisticsRepository.recordFinalGame(finalGame)
        val initViewModel = InitViewModel(statisticsRepository)

        assertEquals(FinalGameUiModel(isWin = true, winnerNames = listOf("Alice")), finalGame)
        assertEquals(
            listOf(PlayerStatisticsUiModel(name = "Alice", wins = 1)),
            initViewModel.uiState.value.statistics,
        )
    }

    @Test
    fun `losing game gui state is not saved into initialization statistics`() {
        val statisticsFile =
            Files.createTempDirectory("zombies-gui-final-lose-statistics").resolve("statistics.properties")
        val statisticsRepository = PlayerStatisticsRepository(statisticsFile)
        val human = testHuman("cards.entity.human.test.name", healthPoints = 1)
        val context =
            GameContext(
                board = testBoard(cards = mapOf(START to listOf(human))),
                players = listOf(testPlayer("Alice", human)),
                localization = "ru",
            )
        context.activePlayerAddHealth(-1)
        val gameViewModel = GameViewModel(ActionManager(context), ReadOnlyGameContext(context))

        val finalGame = gameViewModel.uiState.value.finalGame ?: error("Final game is expected")
        statisticsRepository.recordFinalGame(finalGame)
        val initViewModel = InitViewModel(statisticsRepository)

        assertEquals(FinalGameUiModel(isWin = false), finalGame)
        assertEquals(emptyList(), initViewModel.uiState.value.statistics)
    }
}
