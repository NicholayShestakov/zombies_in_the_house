package game.actions

import game.FINISH
import game.Player
import game.board.Board
import game.cards.Entity
import game.gameControl.GameContext
import game.testBoard
import game.testHuman
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EndGameActionTest {
    private lateinit var entities: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: EndGameAction

    @BeforeEach
    fun initialization() {
        entities =
            listOf(
                testHuman(),
            )
        players =
            listOf(
                testPlayer("Alice", entities[0]),
            )
        board = testBoard(cards = mapOf(FINISH to entities))
        context = GameContext(board, players, "ru")
        action = EndGameAction(context)
    }

    @Test
    fun `win end game works correctly`() {
        context.incrementWinConditionsCount()
        context.incrementWinConditionsCount()
        assertTrue(action.isAvailable)
        try {
            action.execute()
        } catch (e: Exception) {
            assertEquals("Game finished. Players win.", e.message)
        }
    }

    @Test
    fun `lose end game works correctly`() {
        context.activePlayerAddHealth(-3)
        assertTrue(action.isAvailable)
        try {
            action.execute()
        } catch (e: Exception) {
            assertEquals("Game finished. Players lose.", e.message)
        }
    }
}
