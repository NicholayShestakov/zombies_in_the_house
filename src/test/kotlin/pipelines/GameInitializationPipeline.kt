package pipelines

import game.SpinnerValue
import game.board.CellType
import game.gameControl.GameInitialization
import game.gameControl.GameStarter
import game.gameControl.GameState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private const val EXPECTED_BOARD_SIZE = 12
private const val EXPECTED_BOARD_CELL_COUNT = EXPECTED_BOARD_SIZE * EXPECTED_BOARD_SIZE
private const val EXPECTED_DEFAULT_WALL_COUNT = 38
private const val EXPECTED_WIN_CONDITIONS_TO_WIN = 2

class GameInitializationPipeline {
    @Test
    fun `game initialization components correctly creates game session`() {
        val initialization = GameInitialization()
        initialization.setPlayerStartHealth(4)
        initialization.setPlayerCount(2)
        initialization.setPlayerName(0, "Alice")
        initialization.setPlayerName(1, "Bob")
        val config = initialization.buildGameConfig()
        val starter = GameStarter()
        val (manager, readOnlyContext) = starter.start(config, 42)

        assertEquals(null, manager.higherAvailableAutomatic)
        assertNotEquals(listOf(), manager.availableNonautomaticList)
        assertTrue(manager.availableNonautomaticList.any { it.getName() == "Крутить спиннер" })

        assertEquals(GameState.STEP_SPINNER_SPINNING, readOnlyContext.gameState)
        assertEquals("ru", readOnlyContext.localization)
        assertEquals(listOf("Alice", "Bob"), readOnlyContext.playersNames)
        assertEquals(emptyList(), readOnlyContext.playersOnFinishNames)
        assertFalse(readOnlyContext.isWin)
        assertFalse(readOnlyContext.isLose)
        assertEquals(0, readOnlyContext.activePlayerIndex)
        assertEquals("Alice", readOnlyContext.activePlayerName)
        assertTrue(readOnlyContext.activePlayerEntityNameKey.startsWith("cards.entity.human."))
        assertEquals(4, readOnlyContext.activePlayerHealth)
        assertEquals(0, readOnlyContext.activePlayerStepCount)
        assertNotNull(readOnlyContext.activePlayerCoordinates)
        assertTrue(readOnlyContext.activePlayerInventory.size <= 1)
        assertTrue(readOnlyContext.activePlayerInventory.all { !it.isFaceDown })
        assertEquals(EXPECTED_WIN_CONDITIONS_TO_WIN, readOnlyContext.winConditionsToWin)
        assertEquals(0, readOnlyContext.winConditions)
        assertEquals(SpinnerValue.ESCAPE, readOnlyContext.spinnerValue)

        assertEquals(EXPECTED_BOARD_SIZE, readOnlyContext.boardWidth)
        assertEquals(EXPECTED_BOARD_SIZE, readOnlyContext.boardHeight)
        assertEquals(EXPECTED_BOARD_CELL_COUNT, readOnlyContext.boardCellsCardsAndConditions.size)
        assertEquals(EXPECTED_DEFAULT_WALL_COUNT, readOnlyContext.boardWalls.size)
        assertTrue((2 to 1) to (2 to 2) in readOnlyContext.boardWalls)
        assertTrue((3 to 7) to (4 to 7) in readOnlyContext.boardWalls)
        assertFalse((1 to 4) to (2 to 4) in readOnlyContext.boardWalls)
        assertTrue(readOnlyContext.boardCellsCardsAndConditions.any { CellType.START in it.second })
        assertEquals(3, readOnlyContext.boardCellsCardsAndConditions.count { CellType.FINISH in it.second })
        assertTrue(readOnlyContext.boardCellsCardsAndConditions.any { it.first.isNotEmpty() })
    }
}
