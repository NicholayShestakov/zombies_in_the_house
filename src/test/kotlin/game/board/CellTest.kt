package game.board

import game.testItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CellTest {
    @Test
    fun `cell stores conditions and mutable cards`() {
        val item = testItem()
        val cell = Cell(conditions = listOf(CellType.START))

        assertEquals(listOf(CellType.START), cell.conditions)
        assertTrue(cell.cards.isEmpty())
        cell.cards.add(item)
        assertEquals(item, cell.cards[0])
    }
}
