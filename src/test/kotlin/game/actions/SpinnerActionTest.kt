package game.actions

import game.Player
import game.SPINNER_ESCAPE_SEED
import game.START
import game.board.Board
import game.cards.Entity
import game.gameControl.GameContext
import game.gameControl.GameState
import game.testBoard
import game.testHuman
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SpinnerActionTest {
    private lateinit var entities: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: SpinnerAction

    @BeforeEach
    fun initialization() {
        entities =
            listOf(
                testHuman(),
                testMonster(),
            )
        players =
            listOf(
                testPlayer("Alice", entities[0]),
                testPlayer("Bob", entities[1]),
            )
        board = testBoard(cards = mapOf(START to entities))
        context = GameContext(board, players, "ru")
        action = SpinnerAction(context)
    }

    @Test
    fun `spinner action available in SPINNING states`() {
        assertEquals(GameState.STEP_SPINNER_SPINNING, context.gameState)
        assertTrue(action.isAvailable)

        context.spin()
        context.activePlayerStartStepping()
        context.startBattle()
        assertEquals(GameState.BATTLE_SPINNER_SPINNING, context.gameState)
        assertTrue(action.isAvailable)

        context.spin(SPINNER_ESCAPE_SEED)
        context.endBattle()
        assertEquals(GameState.ESCAPE_SPINNER_SPINNING, context.gameState)
        assertTrue(action.isAvailable)
    }
}
