package game.actions

import game.Player
import game.board.Board
import game.cards.Entity
import game.gameControl.GameContext
import game.testBoard
import game.testHuman
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActionTest {
    private lateinit var humans: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: TestAction

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
        board = testBoard()
        context = GameContext(board, players, "ru")
    }

    @Test
    fun `actions execution returns true if action available`() {
        action = TestAction(context, true)
        assertTrue(action.isAvailable)
        assertTrue(action.execute())
    }

    @Test
    fun `actions execution returns false if action not available`() {
        action = TestAction(context, false)
        assertFalse(action.isAvailable)
        assertFalse(action.execute())
    }

    @Test
    fun `action name and desc getters returns name and desc keys if cannot find name and desc`() {
        action = TestAction(context, true)
        assertEquals("actions.test.name", action.getName())
        assertEquals("actions.test.description", action.getDescription())
    }

    class TestAction(
        context: GameContext,
        val availability: Boolean,
    ) : Action(
            listOf("actions.test.name"),
            listOf("actions.test.description"),
            context,
        ) {
        override val isAvailable: Boolean
            get() = availability

        override fun execute(): Boolean {
            if (isAvailable) {
                return true
            }
            return false
        }
    }
}
