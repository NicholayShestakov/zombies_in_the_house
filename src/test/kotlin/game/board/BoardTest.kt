package game.board

import game.FINISH
import game.RIGHT
import game.START
import game.UP
import game.cards.ItemType
import game.testBoard
import game.testHuman
import game.testItem
import game.testMonster
import org.junit.jupiter.api.BeforeEach
import kotlin.collections.sortedBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BoardTest {
    private lateinit var board: Board

    @BeforeEach
    fun boardInitialization() {
        board = testBoard(walls = listOf(START to UP, START to RIGHT))
    }

    @Test
    fun `cellExists return true if cell exists and false if not`() {
        assertTrue(board.cellExists(0 to 0))
        assertTrue(board.cellExists(2 to 2))
        assertFalse(board.cellExists(0 to -1))
        assertFalse(board.cellExists(-1 to 0))
        assertFalse(board.cellExists(2 to 3))
        assertFalse(board.cellExists(3 to 2))
    }

    @Test
    fun `getCellWithCardCoordinates gets correct coordinates for exist card and null for not exists`() {
        val human = testHuman()
        val monster = testMonster()
        val item = testItem()
        board.existCellAddCard(0 to 0, human)
        board.existCellAddCard(1 to 0, monster)
        board.existCellAddCard(2 to 0, item)

        assertEquals(0 to 0, board.getCellWithCardCoordinates(human))
        assertEquals(1 to 0, board.getCellWithCardCoordinates(monster))
        assertEquals(2 to 0, board.getCellWithCardCoordinates(item))
        assertEquals(null, board.getCellWithCardCoordinates(testItem()))
    }

    @Test
    fun `cellCondition gets correct results with exist cells`() {
        assertTrue(board.cellHasCondition(START, CellType.START))
        assertTrue(board.cellHasCondition(FINISH, CellType.FINISH))
        assertFalse(board.cellHasCondition(RIGHT, CellType.START))
    }

    @Test
    fun `cellCondition returns false if cell does not exists`() {
        assertFalse(board.cellHasCondition(-1 to -1, CellType.START))
    }

    @Test
    fun `wallExists gets correct results with exist cells`() {
        assertTrue(board.wallExists(START, Direction.UP))
        assertTrue(board.wallExists(UP, Direction.DOWN))
        assertTrue(board.wallExists(START, Direction.RIGHT))
        assertTrue(board.wallExists(RIGHT, Direction.LEFT))
        assertFalse(board.wallExists(RIGHT, Direction.UP))
        assertFalse(board.wallExists(UP, Direction.RIGHT))
    }

    @Test
    fun `wallExists returns false if one of cells does not exists`() {
        assertFalse(board.wallExists(START, Direction.DOWN))
        assertFalse(board.wallExists(START, Direction.LEFT))
        assertFalse(board.wallExists(0 to -1, Direction.UP))
        assertFalse(board.wallExists(-1 to 0, Direction.RIGHT))
    }

    @Test
    fun `existCellHasNeighbour gets correct results with exist given cell`() {
        assertFalse(board.existCellHasNeighbour(START, Direction.UP))
        assertFalse(board.existCellHasNeighbour(START, Direction.DOWN))
        assertFalse(board.existCellHasNeighbour(START, Direction.RIGHT))
        assertFalse(board.existCellHasNeighbour(START, Direction.LEFT))
        assertTrue(board.existCellHasNeighbour(UP, Direction.UP))
        assertTrue(board.existCellHasNeighbour(UP, Direction.RIGHT))
    }

    @Test
    fun `existCellHasNeighbour fails if given cell does not exists`() {
        assertFailsWith<IllegalArgumentException> { board.existCellHasNeighbour(0 to -1, Direction.UP) }
    }

    @Test
    fun `isCardNeighbour gets correct results with exist cell and exist card`() {
        val knife = testItem(ItemType.KNIFE)
        val firstAidKit = testItem(ItemType.FIRST_AID_KIT)
        val monster = testMonster()
        val human = testHuman()
        board.existCellAddCard(START, knife)
        board.existCellAddCard(RIGHT, firstAidKit)
        board.existCellAddCard(UP, monster)
        board.existCellAddCard(1 to 1, human)

        assertFalse(board.isCardNeighbour(RIGHT, knife))
        assertTrue(board.isCardNeighbour(RIGHT, firstAidKit))
        assertFalse(board.isCardNeighbour(RIGHT, monster))
        assertTrue(board.isCardNeighbour(RIGHT, human))
    }

    @Test
    fun `isCardNeighbour gets false with exist cell and not exist card`() {
        assertFalse(board.isCardNeighbour(RIGHT, testItem()))
        assertFalse(board.isCardNeighbour(RIGHT, testHuman()))
    }

    @Test
    fun `isCardNeighbour fails when cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.isCardNeighbour(-1 to -1, testItem()) }
        assertFailsWith<IllegalArgumentException> { board.isCardNeighbour(-1 to -1, testHuman()) }
    }

    @Test
    fun `existCellHasCard returns correct results when cell exists`() {
        val item1 = testItem()
        val item2 = testItem()
        board.existCellAddCard(START, item1)
        board.existCellAddCard(START, item2)

        assertTrue(board.existCellHasCard(START, item1))
        assertTrue(board.existCellHasCard(START, item2))
        assertFalse(board.existCellHasCard(START, testItem()))
    }

    @Test
    fun `existCellHasCard fails when cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.existCellHasCard(-1 to -1, testItem()) }
    }

    @Test
    fun `existCellAddCard adds card correctly when cell exists and this card in this cell does not exist`() {
        val item = testItem()
        assertFalse(board.existCellHasCard(START, item))
        board.existCellAddCard(START, item)
        assertTrue(board.existCellHasCard(START, item))
    }

    @Test
    fun `existCellAddCard fails if given card already exists on given cell`() {
        val item = testItem()
        board.existCellAddCard(START, item)
        assertFailsWith<IllegalArgumentException> { board.existCellAddCard(START, item) }
    }

    @Test
    fun `existCellAddCard fails if given cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.existCellAddCard(-1 to -1, testItem()) }
    }

    @Test
    fun `existCellRemoveCard removes card correctly if card and cell are exists`() {
        val item = testItem()
        board.existCellAddCard(START, item)
        assertTrue(board.existCellHasCard(START, item))
        board.existCellRemoveCard(START, item)
        assertFalse(board.existCellHasCard(START, item))
    }

    @Test
    fun `existCellRemoveCard fails if card on cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.existCellRemoveCard(START, testItem()) }
    }

    @Test
    fun `existCellRemoveCard fails if given cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.existCellRemoveCard(-1 to -1, testItem()) }
    }

    @Test
    fun `existCellHasAnyCard works correctly with exist cell`() {
        val item1 = testItem()
        val item2 = testItem()
        val item3 = testItem()
        board.existCellAddCard(START, item1)
        board.existCellAddCard(START, item2)

        assertTrue(board.existCellHasAnyCard(START, listOf(item1, item2, item3)))
        assertTrue(board.existCellHasAnyCard(START, listOf(item1, item2)))
        assertTrue(board.existCellHasAnyCard(START, listOf(item1, item3)))
        assertFalse(board.existCellHasAnyCard(START, listOf(item3)))
    }

    @Test
    fun `existCellHasAnyCard fails if given cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.existCellHasAnyCard(-1 to -1, listOf(testItem())) }
    }

    @Test
    fun `existCellAddCards works correctly with exist cell which not contains given cards`() {
        val item1 = testItem()
        val item2 = testItem()
        val item3 = testItem()
        val itemList = listOf(item1, item2, item3)

        assertFalse(board.existCellHasAnyCard(START, itemList))
        board.existCellAddCards(START, itemList)
        assertTrue(board.existCellHasCard(START, item1))
        assertTrue(board.existCellHasCard(START, item2))
        assertTrue(board.existCellHasCard(START, item3))
    }

    @Test
    fun `existCellAddCards works correctly with empty card list`() {
        board.existCellAddCards(START, listOf())
        assertTrue(board.isEmpty(START))
    }

    @Test
    fun `existCellAddCards fails if any card is exists`() {
        val item1 = testItem()
        val item2 = testItem()
        val item3 = testItem()
        val itemList = listOf(item1, item2, item3)
        board.existCellAddCard(START, item3)

        assertFailsWith<IllegalArgumentException> { board.existCellAddCards(START, itemList) }
    }

    @Test
    fun `existCellAddCards fails if cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.existCellAddCards(-1 to -1, listOf()) }
    }

    @Test
    fun `move works correctly when it is correct operation`() {
        val human = testHuman()
        board.existCellAddCard(RIGHT, human)

        board.move(human, Direction.UP)
        assertEquals(1 to 1, board.getCellWithCardCoordinates(human))
        assertFalse(board.existCellHasCard(RIGHT, human))

        board.move(human, Direction.LEFT)
        assertEquals(0 to 1, board.getCellWithCardCoordinates(human))
        assertFalse(board.existCellHasCard(1 to 1, human))
    }

    @Test
    fun `move fails when on move path stay some barrier`() {
        val human = testHuman()
        board.existCellAddCard(START, human)

        assertFailsWith<IllegalArgumentException> { board.move(human, Direction.UP) }
        assertFailsWith<IllegalArgumentException> { board.move(human, Direction.LEFT) }
    }

    @Test
    fun `move fails when card does not lays on board`() {
        assertFailsWith<IllegalStateException> { board.move(testHuman(), Direction.UP) }
    }

    @Test
    fun `hasOpponents works correctly with exist cells`() {
        board.existCellAddCard(START, testHuman())
        board.existCellAddCard(START, testMonster())
        board.existCellAddCard(FINISH, testMonster(isFaceDown = true))
        assertTrue(board.hasOpponents(START, true))
        assertTrue(board.hasOpponents(START, false))
        assertFalse(board.hasOpponents(FINISH, true))
        assertFalse(board.hasOpponents(FINISH, false))
    }

    @Test
    fun `hasOpponents fails when given cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.hasOpponents(-1 to -1, true) }
        assertFailsWith<IllegalArgumentException> { board.hasOpponents(-1 to -1, false) }
    }

    @Test
    fun `getHighestOpponent returns highest exist opponent`() {
        val lowHuman = testHuman()
        val highHuman = testHuman()
        val lowMonster = testMonster()
        val highMonster = testMonster()
        board.existCellAddCard(START, lowHuman)
        board.existCellAddCard(START, highHuman)
        board.existCellAddCard(START, lowMonster)
        board.existCellAddCard(START, highMonster)

        assertEquals(highHuman, board.getHighestOpponent(START, false))
        assertEquals(highMonster, board.getHighestOpponent(START, true))
    }

    @Test
    fun `getHighestOpponent fails when on given cell no opponents`() {
        assertFailsWith<IllegalArgumentException> { board.getHighestOpponent(START, true) }
        assertFailsWith<IllegalArgumentException> { board.getHighestOpponent(START, false) }
    }

    @Test
    fun `getHighestOpponent fails when given cell does not exists`() {
        assertFailsWith<IllegalArgumentException> { board.getHighestOpponent(-1 to -1, true) }
        assertFailsWith<IllegalArgumentException> { board.getHighestOpponent(-1 to -1, false) }
    }

    @Test
    fun `hasItems gets correct results when cell exists`() {
        board.existCellAddCard(START, testItem())
        board.existCellAddCard(FINISH, testItem(isFaceDown = true))
        board.existCellAddCard(FINISH, testHuman())
        assertTrue(board.hasItems(START))
        assertFalse(board.hasItems(FINISH))
    }

    @Test
    fun `hasItems fails if given cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.hasItems(-1 to -1) }
    }

    @Test
    fun `takeHighestItem correctly removes highest item and returns it if item exists`() {
        val lowestItem = testItem()
        val highestItem = testItem()
        board.existCellAddCard(START, lowestItem)
        board.existCellAddCard(START, highestItem)
        assertEquals(highestItem, board.takeHighestItem(START))
        assertFalse(board.existCellHasCard(START, highestItem))
    }

    @Test
    fun `takeHighestItem not removes entities`() {
        val item = testItem()
        val entity = testHuman()
        board.existCellAddCard(START, item)
        board.existCellAddCard(START, entity)
        assertEquals(item, board.takeHighestItem(START))
        assertFalse(board.existCellHasCard(START, item))
        assertTrue(board.existCellHasCard(START, entity))
    }

    @Test
    fun `takeHighestItem fails if cell is empty`() {
        assertFailsWith<IllegalArgumentException> { board.takeHighestItem(START) }
    }

    @Test
    fun `takeHighestItem fails if cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.takeHighestItem(-1 to -1) }
    }

    @Test
    fun `hasFaceDown gets correct results on exist cells`() {
        board.existCellAddCard(START, testItem(isFaceDown = true))
        board.existCellAddCard(FINISH, testItem())

        assertTrue(board.hasFaceDown(START))
        assertFalse(board.hasFaceDown(FINISH))
    }

    @Test
    fun `hasFaceDown fails if cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.hasFaceDown(-1 to -1) }
    }

    @Test
    fun `flipFaceDown flips highest card if face down card exists`() {
        val lowestItem = testItem(isFaceDown = true)
        val highestItem = testItem(isFaceDown = true)
        board.existCellAddCard(START, lowestItem)
        board.existCellAddCard(START, highestItem)

        board.flipFaceDown(START)
        assertFalse(highestItem.isFaceDown)
        assertTrue(lowestItem.isFaceDown)
    }

    @Test
    fun `flipFaceDown fails if face down cards do not exist`() {
        board.existCellAddCard(START, testItem())
        assertFailsWith<IllegalArgumentException> { board.flipFaceDown(START) }
    }

    @Test
    fun `flipFaceDown fails if cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.flipFaceDown(-1 to -1) }
    }

    @Test
    fun `isEmpty works correct on exist cells`() {
        board.existCellAddCard(START, testItem())
        assertFalse(board.isEmpty(START))
        assertTrue(board.isEmpty(FINISH))
    }

    @Test
    fun `isEmpty fails if cell does not exist`() {
        assertFailsWith<IllegalArgumentException> { board.isEmpty(-1 to -1) }
    }

    @Test
    fun `cardsOnBoard returns all cards on board`() {
        assertEquals(listOf(), board.cardsOnBoard)

        val item = testItem()
        val human = testHuman()
        val monster = testMonster()
        val cardList = listOf(item, human, monster)
        board.existCellAddCard(START, item)
        board.existCellAddCard(UP, human)
        board.existCellAddCard(RIGHT, monster)
        assertEquals(cardList.sortedBy { it.cardNameKey }, board.cardsOnBoard.sortedBy { it.cardNameKey })
    }

    @Test
    fun `monstersOnBoard returns all monsters on board`() {
        assertEquals(listOf(), board.monstersOnBoard)

        val item = testItem()
        val human = testHuman()
        val monster = testMonster()
        val monster2 = testMonster()
        board.existCellAddCard(START, item)
        board.existCellAddCard(UP, human)
        board.existCellAddCard(RIGHT, monster)
        board.existCellAddCard(FINISH, monster2)
        assertEquals(
            listOf(monster, monster2).sortedBy { it.cardNameKey },
            board.monstersOnBoard.sortedBy { it.cardNameKey },
        )
    }

    @Test
    fun `humansOnBoard returns all humans on board`() {
        assertEquals(listOf(), board.humansOnBoard)

        val item = testItem()
        val human = testHuman()
        val human2 = testHuman()
        val monster = testMonster()
        board.existCellAddCard(START, item)
        board.existCellAddCard(UP, human)
        board.existCellAddCard(FINISH, human2)
        board.existCellAddCard(RIGHT, monster)
        assertEquals(listOf(human, human2).sortedBy { it.cardNameKey }, board.humansOnBoard.sortedBy { it.cardNameKey })
    }

    @Test
    fun `onFinishHumans returns humans on finish cells`() {
        val item = testItem()
        val human = testHuman()
        val human2 = testHuman()
        val monster = testMonster()
        board.existCellAddCard(FINISH, item)
        board.existCellAddCard(FINISH, human)
        board.existCellAddCard(FINISH, human2)
        board.existCellAddCard(FINISH, monster)
        assertEquals(
            listOf(human, human2).sortedBy { it.cardNameKey },
            board.onFinishHumans.sortedBy { it.cardNameKey },
        )
    }
}
