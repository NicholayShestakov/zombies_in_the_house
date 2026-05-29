package game.actions

import game.Player
import game.SPINNER_ESCAPE_SEED
import game.START
import game.board.Board
import game.cards.Entity
import game.cards.ItemType
import game.gameControl.GameContext
import game.testBoard
import game.testHuman
import game.testItem
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActionManagerDynamicActionsTest {
    private lateinit var humans: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var manager: ActionManager

    @BeforeEach
    fun initialization() {
        humans =
            listOf(
                testHuman("cards.entity.human.alice.name", plusSpeed = -1),
                testHuman("cards.entity.human.bob.name", plusSpeed = -1),
            )
        players = listOf(testPlayer("Alice", humans[0]), testPlayer("Bob", humans[1]))
        board = testBoard(cards = mapOf(START to humans))
        context = GameContext(board, players, "ru")
        manager = ActionManager(context)
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
    }

    @Test
    fun `availableNonautomaticList creates give item action after active inventory changes`() {
        assertFalse(manager.availableNonautomaticList.hasGiveItemAction())

        context.playerWithEntityAddItem(humans[0], testItem(ItemType.KNIFE))

        assertTrue(manager.availableNonautomaticList.hasGiveItemAction())
    }

    @Test
    fun `availableNonautomaticList removes give item action after active inventory item is removed`() {
        val item = testItem(ItemType.KNIFE)
        context.playerWithEntityAddItem(humans[0], item)
        assertTrue(manager.availableNonautomaticList.hasGiveItemAction())

        context.activePlayerRemoveItem(ItemType.KNIFE)

        assertFalse(manager.availableNonautomaticList.hasGiveItemAction())
    }

    @Test
    fun `availableNonautomaticList creates one give item action for every active inventory type and nonactive human`() {
        context.playerWithEntityAddItem(humans[0], testItem(ItemType.KNIFE))
        context.playerWithEntityAddItem(humans[0], testItem(ItemType.GUN))

        val giveItemActions = manager.availableNonautomaticList.filterIsInstance<GiveItemToOtherPlayerAction>()

        assertEquals(2, giveItemActions.size)
    }

    @Test
    fun `availableNonautomaticList does not duplicate give item actions for duplicated item type`() {
        context.playerWithEntityAddItem(humans[0], testItem(ItemType.KNIFE))
        context.playerWithEntityAddItem(humans[0], testItem(ItemType.KNIFE))

        val giveItemActions = manager.availableNonautomaticList.filterIsInstance<GiveItemToOtherPlayerAction>()

        assertEquals(1, giveItemActions.size)
    }

    private fun List<Action>.hasGiveItemAction(): Boolean {
        return any { it is GiveItemToOtherPlayerAction }
    }
}
