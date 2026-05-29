package game.gameControl

import game.FINISH
import game.RIGHT
import game.START
import game.UP
import game.board.CellType
import game.cards.ItemType
import game.testBoard
import game.testHuman
import game.testItem
import game.testPlayer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameContextReadOnlyAdditionsTest {
    @Test
    fun `playersOnFinishNames returns only player names whose current humans are on finish`() {
        val alice = testHuman("cards.entity.human.alice.name")
        val bob = testHuman("cards.entity.human.bob.name")
        val inactiveHuman = testHuman("cards.entity.human.inactive.name")
        val context =
            GameContext(
                board = testBoard(cards = mapOf(FINISH to listOf(alice, inactiveHuman), START to listOf(bob))),
                players = listOf(testPlayer("Alice", alice), testPlayer("Bob", bob)),
                localization = "ru",
            )

        assertEquals(listOf("Alice"), context.playersOnFinishNames)
    }

    @Test
    fun `playersOnFinishNames updates after player moves onto finish`() {
        val alice = testHuman("cards.entity.human.alice.name")
        val startCoordinates = 1 to 2
        val board = testBoard(cards = mapOf(startCoordinates to listOf(alice)))
        val context =
            GameContext(
                board = board,
                players = listOf(testPlayer("Alice", alice)),
                localization = "ru",
            )

        assertEquals(emptyList(), context.playersOnFinishNames)
        board.existCellRemoveCard(startCoordinates, alice)
        context.cellAddCard(FINISH, alice)

        assertEquals(listOf("Alice"), context.playersOnFinishNames)
    }

    @Test
    fun `activePlayerEntityNameKey returns key of currently controlled entity`() {
        val alice = testHuman("cards.entity.human.alice.name")
        val context =
            GameContext(
                board = testBoard(cards = mapOf(START to listOf(alice))),
                players = listOf(testPlayer("Alice", alice)),
                localization = "ru",
            )

        assertEquals("cards.entity.human.alice.name", context.activePlayerEntityNameKey)
    }

    @Test
    fun `board read only getters delegate board dimensions cells and walls`() {
        val human = testHuman("cards.entity.human.alice.name")
        val item = testItem(ItemType.WIN_CONDITION)
        val walls = listOf(START to RIGHT, START to UP)
        val context =
            GameContext(
                board = testBoard(width = 4, height = 2, walls = walls, cards = mapOf(START to listOf(human, item))),
                players = listOf(testPlayer("Alice", human)),
                localization = "ru",
            )

        assertEquals(4, context.boardWidth)
        assertEquals(2, context.boardHeight)
        assertEquals(listOf(human, item), context.boardCellsCardsAndConditions.first().first)
        assertEquals(listOf(CellType.START), context.boardCellsCardsAndConditions.first().second)
        assertEquals(walls, context.boardWalls)
    }

    @Test
    fun `read only context exposes final state and board snapshots`() {
        val alice = testHuman("cards.entity.human.alice.name")
        val context =
            GameContext(
                board = testBoard(walls = listOf(START to RIGHT), cards = mapOf(FINISH to listOf(alice))),
                players = listOf(testPlayer("Alice", alice)),
                localization = "ru",
                winConditionsToWin = 1,
            )
        val readOnlyContext = ReadOnlyGameContext(context)

        assertEquals(listOf("Alice"), readOnlyContext.playersNames)
        assertEquals(listOf("Alice"), readOnlyContext.playersOnFinishNames)
        assertEquals("Alice", readOnlyContext.activePlayerName)
        assertEquals("cards.entity.human.alice.name", readOnlyContext.activePlayerEntityNameKey)
        assertEquals(FINISH, readOnlyContext.activePlayerCoordinates)
        assertEquals(listOf(START to RIGHT), readOnlyContext.boardWalls)
        assertEquals(context.boardCellsCardsAndConditions, readOnlyContext.boardCellsCardsAndConditions)
        assertFalse(readOnlyContext.isWin)
        context.incrementWinConditionsCount()
        assertTrue(readOnlyContext.isWin)
    }
}
