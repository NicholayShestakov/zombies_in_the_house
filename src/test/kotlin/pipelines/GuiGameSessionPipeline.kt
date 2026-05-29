package pipelines

import game.gameControl.GameInitialization
import game.gameControl.GameStarter
import gui.GameViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GuiGameSessionPipeline {
    @Test
    fun `initialized game session is represented in game gui state`() {
        val initialization = GameInitialization()
        initialization.setPlayerCount(2)
        initialization.setPlayerName(0, "Alice")
        initialization.setPlayerName(1, "Bob")
        initialization.setPlayerStartHealth(4)
        val config = initialization.buildGameConfig()
        val (manager, readOnlyContext) = GameStarter().start(config, 42)
        val viewModel = GameViewModel(manager, readOnlyContext)

        val uiState = viewModel.uiState.value

        assertEquals(listOf("Alice", "Bob"), uiState.players.map { it.name })
        assertEquals("Alice", uiState.activePlayerName)
        assertEquals(4, uiState.activePlayerHealth)
        assertTrue(uiState.boardRows.isNotEmpty())
        assertTrue(uiState.boardRows.all { it.isNotEmpty() })
        assertTrue(uiState.actions.any { it.name == "Крутить спиннер" })
        assertNotNull(uiState.selectedCell)
    }

    @Test
    fun `cell selection in game gui state exposes clicked cell details`() {
        val initialization = GameInitialization()
        val config = initialization.buildGameConfig()
        val (manager, readOnlyContext) = GameStarter().start(config, 42)
        val viewModel = GameViewModel(manager, readOnlyContext)

        val finishCell =
            viewModel.uiState.value.boardRows.flatten().first { cell ->
                cell.conditions.contains("Финиш")
            }
        viewModel.onCellClicked(finishCell.x, finishCell.y)
        val selectedCell = viewModel.uiState.value.selectedCell

        assertNotNull(selectedCell)
        assertEquals(finishCell.x, selectedCell.x)
        assertEquals(finishCell.y, selectedCell.y)
        assertTrue(selectedCell.conditions.contains("Финиш"))
    }
}
