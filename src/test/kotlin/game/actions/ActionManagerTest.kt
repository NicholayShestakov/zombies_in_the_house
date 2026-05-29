package game.actions

import game.Player
import game.START
import game.board.Board
import game.cards.Entity
import game.gameControl.GameContext
import game.testBoard
import game.testHuman
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ActionManagerTest {
    private lateinit var humans: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var manager: ActionManager

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
        board = testBoard(cards = mapOf(START to humans))
        context = GameContext(board, players, "ru")
        manager = ActionManager(context)
    }

    @Test
    fun `action manager gets null when automatic actions do not exist`() {
        assertEquals(null, manager.higherAvailableAutomatic)
    }

    @Test
    fun `action manager gets non null when automatic actions exists`() {
        context.spin()
        assertNotEquals(null, manager.higherAvailableAutomatic)
    }

    @Test
    fun `action manager nonautomatic action list contains actions`() {
        assertNotEquals(listOf(), manager.availableNonautomaticList)
    }
}
