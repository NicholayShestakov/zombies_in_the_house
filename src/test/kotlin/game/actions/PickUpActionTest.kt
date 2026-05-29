package game.actions

import game.Player
import game.SPINNER_ESCAPE_SEED
import game.START
import game.board.Board
import game.cards.Entity
import game.gameControl.GameContext
import game.testBoard
import game.testHuman
import game.testItem
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PickUpActionTest {
    private lateinit var entities: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: PickUpAction

    @BeforeEach
    fun initialization() {
        entities =
            listOf(
                testHuman(plusSpeed = -1),
                testMonster(),
            )
        players =
            listOf(
                testPlayer("Alice", entities[0]),
                testPlayer("Bob", entity = entities[1]),
            )
        board = testBoard(cards = mapOf(START to entities + testItem()))
        context = GameContext(board, players, "ru")
        action = PickUpAction(context)
    }

    @Test
    fun `action available only for human players only in stepping and finishing states`() {
        assertFalse(action.isAvailable)
        context.spin(SPINNER_ESCAPE_SEED)
        assertFalse(action.isAvailable)
        context.activePlayerStartStepping()
        assertTrue(action.isAvailable)

        context.nextTurn()
        context.activePlayerSetEntity(entities[1])
        assertFalse(action.isAvailable)
        context.spin()
        assertFalse(action.isAvailable)
    }
}
