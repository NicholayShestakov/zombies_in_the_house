package game.actions

import game.Player
import game.RIGHT
import game.SPINNER_ESCAPE_SEED
import game.START
import game.board.Board
import game.board.Direction
import game.cards.Entity
import game.gameControl.GameContext
import game.gameControl.GameState
import game.testBoard
import game.testHuman
import game.testItem
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FlipCardActionTest {
    private lateinit var humans: List<Entity>
    private lateinit var monsters: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: FlipCardAction

    @BeforeEach
    fun initialization() {
        humans =
            listOf(
                testHuman("AliceHuman"),
            )
        monsters =
            listOf(
                testMonster(),
            )
        players =
            listOf(
                testPlayer("Alice", humans[0]),
                testPlayer("Bob", monsters[0]),
            )
        board =
            testBoard(
                cards =
                    mapOf(
                        START to humans + monsters + testItem(isFaceDown = true),
                        RIGHT to listOf(testItem(isFaceDown = true)),
                    ),
            )
        context = GameContext(board, players, "ru")
        action = FlipCardAction(context)
    }

    @Test
    fun `flip card action available only in correct states`() {
        assertEquals(GameState.STEP_SPINNER_SPINNING, context.gameState)
        assertFalse(action.isAvailable)
        context.spin(SPINNER_ESCAPE_SEED)
        assertEquals(GameState.STEP_SPINNER_SPINNED, context.gameState)
        assertFalse(action.isAvailable)
        context.activePlayerStartStepping()
        assertEquals(GameState.STEPPING, context.gameState)
        assertTrue(action.isAvailable)
        context.activePlayerMove(Direction.RIGHT)
        assertEquals(GameState.TURN_FINISH, context.gameState)
        assertTrue(action.isAvailable)
    }

    @Test
    fun `monsters cannot flip cards`() {
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.nextTurn()

        context.activePlayerSetEntity(monsters[0])
        assertEquals(GameState.STEP_SPINNER_SPINNING, context.gameState)
        assertFalse(action.isAvailable)
        context.spin(SPINNER_ESCAPE_SEED)
        assertEquals(GameState.STEP_SPINNER_SPINNED, context.gameState)
        assertFalse(action.isAvailable)
        context.activePlayerStartStepping()
        assertEquals(GameState.STEPPING, context.gameState)
        assertFalse(action.isAvailable)
        context.activePlayerMove(Direction.RIGHT)
        assertEquals(GameState.TURN_FINISH, context.gameState)
        assertFalse(action.isAvailable)
    }
}
