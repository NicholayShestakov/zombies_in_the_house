package game.gameControl.contextManagers

import game.cards.ItemType
import game.testHuman
import game.testItem
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InventoryManagerTest {
    private lateinit var manager: InventoryManager
    private val item = testItem(ItemType.GUN)

    @BeforeEach
    fun initializeManager() {
        manager = InventoryManager()
    }

    @Test
    fun `inventory manager hasItem works correctly`() {
        val player = testPlayer("Alice", testHuman())

        assertFalse(manager.hasItem(player, item.itemType))
        assertFalse(manager.hasItem(player, ItemType.GUN))
        player.inventory.add(item)
        assertTrue(manager.hasItem(player, item.itemType))
        assertTrue(manager.hasItem(player, ItemType.GUN))
    }

    @Test
    fun `inventory manager adds item to human player correctly and fails when item already exists`() {
        val player = testPlayer("Alice", testHuman())

        assertFalse(manager.hasItem(player, item.itemType))
        manager.addItem(player, item)
        assertTrue(manager.hasItem(player, item.itemType))
        assertFailsWith<IllegalArgumentException> { manager.addItem(player, item) }
    }

    @Test
    fun `inventory manager add not works with monster players`() {
        val player = testPlayer("Alice", testMonster())

        assertFailsWith<IllegalArgumentException> { manager.addItem(player, item) }
    }

    @Test
    fun `inventory manager removes item from human player correctly and fails when item does not exists`() {
        val player = testPlayer("Alice", testHuman())

        assertFailsWith<IllegalArgumentException> { manager.removeItem(player, item.itemType) }
        assertFailsWith<IllegalArgumentException> { manager.removeItem(player, ItemType.GUN) }
        manager.addItem(player, item)
        manager.removeItem(player, item.itemType)
        assertFalse(manager.hasItem(player, item.itemType))
        manager.addItem(player, item)
        manager.removeItem(player, ItemType.GUN)
        assertFalse(manager.hasItem(player, item.itemType))
    }

    @Test
    fun `inventory manager remove not works with monster players`() {
        val player = testPlayer("Alice", testMonster())
        player.inventory.add(item)

        assertFailsWith<IllegalArgumentException> { manager.removeItem(player, item.itemType) }
    }

    @Test
    fun `inventory manager gets item with given type from human player inventory and fails if item does not exists`() {
        val player = testPlayer("Alice", testHuman())
        val otherItem = testItem(ItemType.GUN)

        manager.addItem(player, item)
        manager.addItem(player, otherItem)
        assertEquals(item, manager.getPlayerItemWithType(player, ItemType.GUN))
        assertFailsWith<IllegalArgumentException> { manager.getPlayerItemWithType(player, ItemType.KNIFE) }
    }

    @Test
    fun `inventory manager get item with type not works with monster players`() {
        val player = testPlayer("Alice", testMonster())
        player.inventory.add(item)

        assertFailsWith<IllegalArgumentException> { manager.getPlayerItemWithType(player, item.itemType) }
    }

    @Test
    fun `inventory manager correctly gets inventory of player`() {
        val player = testPlayer("Alice", testHuman())
        val itemList =
            listOf(
                testItem(ItemType.KNIFE),
                testItem(ItemType.GUN),
                testItem(ItemType.GUN),
                testItem(ItemType.GRENADE_LAUNCHER),
            )

        itemList.forEach { manager.addItem(player, it) }

        assertEquals(itemList, manager.getInventoryItems(player))
    }

    @Test
    fun `inventory manager correctly gets inventory types of player`() {
        val player = testPlayer("Alice", testHuman())
        val itemList =
            listOf(
                testItem(ItemType.KNIFE),
                testItem(ItemType.GUN),
                testItem(ItemType.GUN),
                testItem(ItemType.GRENADE_LAUNCHER),
            )

        itemList.forEach { manager.addItem(player, it) }

        assertEquals(itemList.map { it.itemType }, manager.getInventoryItemTypes(player))
    }
}
