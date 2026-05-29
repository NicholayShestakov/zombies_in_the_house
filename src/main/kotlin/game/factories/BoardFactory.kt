package game.factories

import game.board.Board
import game.board.Cell
import game.board.CellType
import game.cards.Card
import game.cards.Entity
import kotlin.random.Random

class BoardFactory {
    private fun getDefaultCellList(): List<List<Cell>> {
        val cellList = MutableList(12) { MutableList(12) { Cell() } }

        cellList[0][0] = Cell(listOf(CellType.START))

        cellList[8][10] = Cell(listOf(CellType.FINISH))
        cellList[9][10] = Cell(listOf(CellType.FINISH))
        cellList[10][10] = Cell(listOf(CellType.FINISH))

        return cellList.map { it.toList() }.toList()
    }

    private fun getDefaultWallList(): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        val wallList = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

        for (x in 0..11) {
            for (y in 0..11) {
                if (x in 2..10 && (y in listOf(1, 5, 8))) {
                    wallList.add(Pair(Pair(x, y), Pair(x, y + 1)))
                }
                if (y in 2..8 && (x in listOf(1, 6, 10))) {
                    wallList.add(Pair(Pair(x, y), Pair(x + 1, y)))
                }
            }
        }

        wallList.add(Pair(Pair(3, 7), Pair(4, 7)))
        wallList.add(Pair(Pair(3, 8), Pair(4, 8)))

        wallList.remove(Pair(Pair(1, 4), Pair(2, 4)))
        wallList.remove(Pair(Pair(1, 7), Pair(2, 7)))
        wallList.remove(Pair(Pair(4, 1), Pair(4, 2)))
        wallList.remove(Pair(Pair(4, 5), Pair(4, 6)))
        wallList.remove(Pair(Pair(5, 1), Pair(5, 2)))
        wallList.remove(Pair(Pair(5, 8), Pair(5, 9)))
        wallList.remove(Pair(Pair(6, 4), Pair(7, 4)))
        wallList.remove(Pair(Pair(6, 7), Pair(7, 7)))
        wallList.remove(Pair(Pair(8, 1), Pair(8, 2)))
        wallList.remove(Pair(Pair(9, 5), Pair(9, 6)))
        wallList.remove(Pair(Pair(10, 4), Pair(11, 4)))
        wallList.remove(Pair(Pair(10, 7), Pair(11, 7)))

        return wallList.toList()
    }

    private fun fillCellListWithDefaultCards(
        cellList: List<List<Cell>>,
        cardDeck: List<Card>,
        seed: Int? = null,
    ) {
        val shuffledCells =
            cellList.flatten().filterNot {
                it.conditions.contains(
                    CellType.START,
                )
            }.shuffled(seed?.let { Random(seed) } ?: Random)
        for (i in cardDeck.indices) {
            shuffledCells[i].cards.add(cardDeck[i])
        }
    }

    private fun fillCellListWithPlayerHumans(
        cellList: List<List<Cell>>,
        humans: List<Entity>,
        seed: Int? = null,
    ) {
        val cells = cellList.flatten().filter { it.conditions.contains(CellType.START) }
        for (human in humans) {
            cells.random(seed?.let { Random(seed) } ?: Random).cards.add(human)
        }
    }

    fun createDefaultBoard(
        humans: List<Entity>,
        deck: List<Card>,
        seed: Int? = null,
    ): Board {
        val cellList = getDefaultCellList()
        val wallList = getDefaultWallList()
        fillCellListWithDefaultCards(cellList, deck, seed)
        fillCellListWithPlayerHumans(cellList, humans)

        return Board(cellList, wallList)
    }
}
