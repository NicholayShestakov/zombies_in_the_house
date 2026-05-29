package game.cards

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ItemTest {
    @Test
    fun `item delegates keys and stores type`() {
        val item = Item(ItemType.GUN, isFaceDown = false)

        assertEquals(ItemType.GUN, item.itemType)
        assertEquals(ItemType.GUN.nameKey, item.cardNameKey)
        assertEquals(ItemType.GUN.descriptionKey, item.cardDescriptionKey)
        assertFalse(item.isFaceDown)
    }
}
