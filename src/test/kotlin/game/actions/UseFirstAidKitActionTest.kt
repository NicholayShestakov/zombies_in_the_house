package game.actions

import game.Player
import game.SPINNER_ESCAPE_SEED
import game.START
import game.board.Board
import game.board.Direction
import game.cards.Entity
import game.cards.EntityAbility
import game.cards.ItemType
import game.gameControl.GameContext
import game.testBoard
import game.testHuman
import game.testItem
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UseFirstAidKitActionTest {
    private lateinit var entities: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: UseFirstAidKitAction

    @BeforeEach
    fun initialization() {
        entities =
            listOf(
                testHuman(),
                testHuman(abilities = listOf(EntityAbility.DOUBLE_HEAL)),
            )
        players =
            listOf(
                testPlayer("Alice", entities[0]),
                testPlayer("Bob", entities[1]),
            )
        board = testBoard(cards = mapOf(START to entities))
        context = GameContext(board, players, "ru")
        action = UseFirstAidKitAction(context)
    }

    @Test
    fun `first aid kit can used anywhere`() {
        players[0].inventory.add(testItem(ItemType.FIRST_AID_KIT))

        assertTrue(action.isAvailable)
        context.spin(SPINNER_ESCAPE_SEED)
        assertTrue(action.isAvailable)
        context.activePlayerStartStepping()
        assertTrue(action.isAvailable)
        context.activePlayerMove(Direction.RIGHT)
        assertTrue(action.isAvailable)
    }

    @Test
    fun `first aid kit heals 1 health point to usual player and 2 to nurse`() {
        players[0].inventory.add(testItem(ItemType.FIRST_AID_KIT))
        assertEquals(3, context.activePlayerHealth)
        action.execute()
        assertEquals(4, context.activePlayerHealth)

        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.nextTurn()

        players[1].inventory.add(testItem(ItemType.FIRST_AID_KIT))
        assertEquals(3, context.activePlayerHealth)
        action.execute()
        assertEquals(5, context.activePlayerHealth)
    }
}
