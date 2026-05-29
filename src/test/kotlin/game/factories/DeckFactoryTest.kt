package game.factories

import game.cards.Entity
import game.cards.Item
import game.cards.ItemType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeckFactoryTest {
    @Test
    fun `createDefaultCardDeck returns documented monster and item distribution`() {
        val deck = DeckFactory().createDefaultCardDeck()
        val entities = deck.filterIsInstance<Entity>()
        val items = deck.filterIsInstance<Item>()
        val itemCounts = items.groupingBy { it.itemType }.eachCount()

        assertEquals(DEFAULT_DECK_SIZE, deck.size)
        assertEquals(DEFAULT_MONSTER_COUNT, entities.size)
        assertEquals(DEFAULT_ITEM_COUNT, items.size)
        assertEquals(ZOMBIE_COUNT, entities.count { it.cardNameKey.contains("zombie") && it.plusSpeed == -1 })
        assertEquals(SPIDER_COUNT, entities.count { it.cardNameKey.contains("spider") })
        assertEquals(DOG_COUNT, entities.count { it.cardNameKey.contains("dog") && it.plusSpeed == 1 })
        assertEquals(BOSS_COUNT, entities.count { it.cardNameKey.contains("boss") })
        assertEquals(GRENADE_COUNT, itemCounts[ItemType.GRENADE])
        assertEquals(KNIFE_COUNT, itemCounts[ItemType.KNIFE])
        assertEquals(GUN_COUNT, itemCounts[ItemType.GUN])
        assertEquals(GRENADE_LAUNCHER_COUNT, itemCounts[ItemType.GRENADE_LAUNCHER])
        assertEquals(FIRST_AID_KIT_COUNT, itemCounts[ItemType.FIRST_AID_KIT])
        assertEquals(WIN_CONDITION_COUNT, itemCounts[ItemType.WIN_CONDITION])
        assertTrue(deck.all { it.isFaceDown })
    }

    private companion object {
        const val DEFAULT_DECK_SIZE = 47
        const val DEFAULT_MONSTER_COUNT = 28
        const val DEFAULT_ITEM_COUNT = 19
        const val ZOMBIE_COUNT = 17
        const val SPIDER_COUNT = 5
        const val DOG_COUNT = 5
        const val BOSS_COUNT = 1
        const val GRENADE_COUNT = 4
        const val KNIFE_COUNT = 3
        const val GUN_COUNT = 3
        const val GRENADE_LAUNCHER_COUNT = 1
        const val FIRST_AID_KIT_COUNT = 6
        const val WIN_CONDITION_COUNT = 2
    }
}
