package game.actions

import game.FINISH
import game.Player
import game.board.Board
import game.board.Direction
import game.cards.Entity
import game.cards.ItemType
import game.gameControl.GameContext
import game.testBoard
import game.testHuman
import game.testItem
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UseWinConditionActionTest {
    private lateinit var entities: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: UseWinConditionAction

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
        players[0].inventory.addAll(
            listOf(
                testItem(ItemType.WIN_CONDITION),
                testItem(ItemType.WIN_CONDITION),
            ),
        )
        board = testBoard(cards = mapOf(FINISH to entities))
        context = GameContext(board, players, "ru")
        action = UseWinConditionAction(context)
    }

    @Test
    fun `win condition usable only on finish cells`() {
        context.spin()
        context.activePlayerStartStepping()

        assertTrue(action.isAvailable)

        context.activePlayerMove(Direction.DOWN)

        assertFalse(action.isAvailable)
    }

    @Test
    fun `when win conditions used in right amount context isWin set on true`() {
        context.spin()
        context.activePlayerStartStepping()

        assertFalse(context.isWin)
        action.execute()
        assertFalse(context.isWin)
        action.execute()
        assertTrue(context.isWin)
    }
}
