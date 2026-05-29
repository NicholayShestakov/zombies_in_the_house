package game.actions

import game.Player
import game.RIGHT
import game.START
import game.board.Board
import game.board.Direction
import game.cards.Entity
import game.gameControl.GameContext
import game.testBoard
import game.testHuman
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MoveActionTest {
    private lateinit var humans: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: List<MoveAction>

    @BeforeEach
    fun initialization() {
        humans =
            listOf(
                testHuman("AliceHuman"),
            )
        players =
            listOf(
                testPlayer("Alice", humans[0]),
            )
        board = testBoard(cards = mapOf(START to humans), walls = listOf(START to RIGHT))
        context = GameContext(board, players, "ru")
        action = Direction.entries.map { MoveAction(context, it) }
    }

    @Test
    fun `move action available when in direction no walls and board edges`() {
        context.spin()
        context.activePlayerStartStepping()

        assertTrue(action[0].isAvailable)
        assertFalse(action[1].isAvailable)
        assertFalse(action[2].isAvailable)
        assertFalse(action[3].isAvailable)
    }
}
