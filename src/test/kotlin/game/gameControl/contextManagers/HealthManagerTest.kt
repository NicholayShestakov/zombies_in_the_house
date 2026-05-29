package game.gameControl.contextManagers

import game.Player
import game.START
import game.board.Board
import game.cards.Entity
import game.testBoard
import game.testHuman
import game.testItem
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HealthManagerTest {
    private lateinit var manager: HealthManager
    private lateinit var human: Entity
    private lateinit var player: Player
    private lateinit var playersManager: PlayersManager
    val inventoryManager = InventoryManager()
    private lateinit var board: Board

    @BeforeEach
    fun initializeManager() {
        human = testHuman(healthPoints = 1)
        player = testPlayer("Alice", human)
        playersManager = PlayersManager(listOf(player))
        board = testBoard(cards = mapOf(START to listOf(human)))
        manager = HealthManager(playersManager, inventoryManager, board)
    }

    @Test
    fun `health manager adds health correctly`() {
        manager.addHealth(human, 1)
        assertEquals(2, human.healthPoints)
    }

    @Test
    fun `health manager correctly removes entity when entity is dead`() {
        assertTrue(board.existCellHasCard(START, human))
        manager.addHealth(human, -1)
        assertFalse(board.existCellHasCard(START, human))
    }

    @Test
    fun `health manager puts player inventory on cell with dead human`() {
        val item = testItem()
        inventoryManager.addItem(player, item)
        assertFalse(board.existCellHasCard(START, item))
        manager.addHealth(human, -1)
        assertTrue(board.existCellHasCard(START, item))
    }
}
