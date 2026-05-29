package game.factories

import game.START
import game.board.CellType
import game.board.Direction
import game.cards.ItemType
import game.testHuman
import game.testItem
import game.testMonster
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BoardFactoryTest {
    @Test
    fun `createDefaultBoard creates default cells walls and places cards`() {
        val humans = listOf(testHuman("first"), testHuman("second"))
        val deck = listOf(testItem(ItemType.KNIFE), testMonster())

        val board = BoardFactory().createDefaultBoard(humans, deck)

        assertTrue(board.cellHasCondition(START, CellType.START))
        assertTrue(board.cellHasCondition(8 to 10, CellType.FINISH))
        assertTrue(board.cellHasCondition(9 to 10, CellType.FINISH))
        assertTrue(board.cellHasCondition(10 to 10, CellType.FINISH))
        assertTrue(board.cellExists(11 to 11))
        assertFalse(board.cellExists(12 to 0))
        assertTrue(board.wallExists(2 to 1, Direction.UP))
        assertFalse(board.wallExists(1 to 4, Direction.RIGHT))
        assertTrue(humans.all { board.getCellWithCardCoordinates(it) == START })
    }
}
