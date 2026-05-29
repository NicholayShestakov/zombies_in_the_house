package game.board

import game.FINISH
import game.RIGHT
import game.START
import game.UP
import game.testBoard
import game.testHuman
import game.testItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class BoardReadOnlyStateTest {
    @Test
    fun `cellsCardsAndConditions returns cells in x then y order`() {
        val startHuman = testHuman("cards.entity.human.start.name")
        val rightItem = testItem()
        val finishHuman = testHuman("cards.entity.human.finish.name")
        val cards =
            mapOf(
                START to listOf(startHuman),
                RIGHT to listOf(rightItem),
                FINISH to listOf(finishHuman),
            )
        val board = testBoard(cards = cards)

        val cells = board.cellsCardsAndConditions

        assertEquals(listOf(startHuman), cells[0].first)
        assertEquals(listOf(CellType.START), cells[0].second)
        assertEquals(listOf(rightItem), cells[3].first)
        assertEquals(emptyList(), cells[3].second)
        assertEquals(listOf(finishHuman), cells[8].first)
        assertEquals(listOf(CellType.FINISH), cells[8].second)
    }

    @Test
    fun `cellsCardsAndConditions returns immutable snapshots of cards and conditions`() {
        val human = testHuman()
        val item = testItem()
        val board = testBoard(cards = mapOf(START to listOf(human)))

        val cellsBeforeChange = board.cellsCardsAndConditions
        board.existCellAddCard(START, item)
        val cellsAfterChange = board.cellsCardsAndConditions

        assertEquals(listOf(human), cellsBeforeChange.first().first)
        assertEquals(listOf(human, item), cellsAfterChange.first().first)
        assertFalse(cellsBeforeChange.first().first === cellsAfterChange.first().first)
    }

    @Test
    fun `walls returns immutable snapshot of board walls`() {
        val walls = listOf(START to RIGHT, START to UP)
        val board = testBoard(walls = walls)

        val wallsBeforeChange = board.walls
        walls.toMutableList().clear()
        val wallsAfterChange = board.walls

        assertEquals(listOf(START to RIGHT, START to UP), wallsBeforeChange)
        assertEquals(listOf(START to RIGHT, START to UP), wallsAfterChange)
        assertFalse(wallsBeforeChange === wallsAfterChange)
    }
}
