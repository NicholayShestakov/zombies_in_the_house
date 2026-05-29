package gui

import game.FINISH
import game.RIGHT
import game.START
import game.UP
import game.actions.ActionManager
import game.cards.ItemType
import game.gameControl.GameContext
import game.gameControl.ReadOnlyGameContext
import game.testBoard
import game.testHuman
import game.testItem
import game.testMonster
import game.testPlayer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GameViewModelTest {
    @Test
    fun `uiState maps active player progress inventory players and localized state`() {
        val human = testHuman("cards.entity.human.test.name")
        val inventoryItem = testItem(ItemType.KNIFE)
        val context =
            GameContext(
                board = testBoard(cards = mapOf(START to listOf(human))),
                players =
                    listOf(
                        testPlayer("Alice", human),
                        testPlayer("Bob", testHuman("cards.entity.human.bob.name")),
                    ),
                localization = "ru",
            )
        context.playerWithEntityAddItem(human, inventoryItem)

        val uiState = createViewModel(context).uiState.value

        assertEquals("Alice", uiState.activePlayerName)
        assertEquals("cards.entity.human.test.name", uiState.activePlayerEntityName)
        assertEquals(3, uiState.activePlayerHealth)
        assertEquals(0, uiState.activePlayerStepCount)
        assertEquals(START, uiState.activePlayerCoordinates)
        assertEquals(listOf(GamePlayerUiModel("Alice", true), GamePlayerUiModel("Bob", false)), uiState.players)
        assertEquals("Ожидание спиннера хода", uiState.gameStateName)
        assertEquals("Побег", uiState.spinnerValue)
        assertEquals(listOf("Нож"), uiState.inventory.map { it.name })
    }

    @Test
    fun `uiState builds board cells with top card label walls and selected cell`() {
        val human = testHuman("cards.entity.human.test.name")
        val zombie = testMonster("cards.entity.monster.zombie.name")
        val spider = testMonster("cards.entity.monster.spider.name")
        val context =
            GameContext(
                board =
                    testBoard(
                        walls = listOf(START to RIGHT, START to UP),
                        cards = mapOf(START to listOf(human, zombie), RIGHT to listOf(spider)),
                    ),
                players = listOf(testPlayer("Alice", human)),
                localization = "ru",
            )
        val viewModel = createViewModel(context)

        viewModel.onCellClicked(0, 0)
        val startCell = viewModel.uiState.value.selectedCell

        assertNotNull(startCell)
        assertEquals("🧟", startCell.topCardText)
        assertTrue(startCell.isActivePlayerCell)
        assertTrue(startCell.hasTopWall)
        assertTrue(startCell.hasRightWall)
        assertFalse(startCell.hasBottomWall)
        assertFalse(startCell.hasLeftWall)
        assertEquals(listOf("Старт"), startCell.conditions)
        assertEquals(listOf("сверху", "справа"), startCell.walls)
    }

    @Test
    fun `top card label uses specific monster item human hidden and fallback symbols`() {
        val human = testHuman("cards.entity.human.test.name")
        val unknownMonster = testMonster("cards.entity.monster.unknown.name")
        val context =
            GameContext(
                board =
                    testBoard(
                        width = 3,
                        height = 4,
                        cards =
                            mapOf(
                                START to listOf(human, testMonster("cards.entity.monster.boss.name")),
                                RIGHT to listOf(testItem(ItemType.GRENADE_LAUNCHER)),
                                FINISH to listOf(testItem(ItemType.WIN_CONDITION, isFaceDown = true)),
                                UP to listOf(human),
                                1 to 1 to listOf(unknownMonster),
                            ),
                    ),
                players = listOf(testPlayer("Alice", human)),
                localization = "ru",
            )

        val cells = createViewModel(context).uiState.value.boardRows.flatten()

        assertEquals("👑", cells.first { it.x to it.y == START }.topCardText)
        assertEquals("🚀", cells.first { it.x to it.y == RIGHT }.topCardText)
        assertEquals("?", cells.first { it.x to it.y == FINISH }.topCardText)
        assertEquals("🙂", cells.first { it.x to it.y == UP }.topCardText)
        assertEquals("🧟", cells.first { it.x to it.y == 1 to 1 }.topCardText)
        assertNull(cells.first { it.x to it.y == 2 to 1 }.topCardText)
    }

    @Test
    fun `card ui model hides face down card details`() {
        val human = testHuman("cards.entity.human.test.name")
        val hiddenItem = testItem(ItemType.KNIFE, isFaceDown = true)
        val context =
            GameContext(
                board = testBoard(cards = mapOf(START to listOf(human))),
                players = listOf(testPlayer("Alice", human)),
                localization = "ru",
            )
        context.playerWithEntityAddItem(human, hiddenItem)

        val inventoryItem = createViewModel(context).uiState.value.inventory.single()

        assertEquals("Закрытая карта", inventoryItem.name)
        assertEquals("Карта лежит рубашкой вверх.", inventoryItem.description)
        assertEquals(emptyList(), inventoryItem.details)
        assertTrue(inventoryItem.isFaceDown)
    }

    @Test
    fun `onActionClicked exposes unavailable action message`() {
        val human = testHuman("cards.entity.human.test.name")
        val context =
            GameContext(
                board = testBoard(cards = mapOf(START to listOf(human))),
                players = listOf(testPlayer("Alice", human)),
                localization = "ru",
            )
        val viewModel = createViewModel(context)

        viewModel.onActionClicked(Int.MAX_VALUE)

        assertEquals("Действие больше недоступно", viewModel.uiState.value.message)
    }

    @Test
    fun `uiState contains final win model with winner names`() {
        val human = testHuman("cards.entity.human.test.name")
        val context =
            GameContext(
                board = testBoard(cards = mapOf(FINISH to listOf(human))),
                players = listOf(testPlayer("Alice", human)),
                localization = "ru",
            )
        repeat(context.winConditionsToWin) {
            context.incrementWinConditionsCount()
        }

        val finalGame = createViewModel(context).uiState.value.finalGame

        assertEquals(FinalGameUiModel(isWin = true, winnerNames = listOf("Alice")), finalGame)
    }

    @Test
    fun `uiState contains final lose model without winners`() {
        val human = testHuman("cards.entity.human.test.name", healthPoints = 1)
        val context =
            GameContext(
                board = testBoard(cards = mapOf(START to listOf(human))),
                players = listOf(testPlayer("Alice", human)),
                localization = "ru",
            )
        context.activePlayerAddHealth(-1)

        val finalGame = createViewModel(context).uiState.value.finalGame

        assertEquals(FinalGameUiModel(isWin = false), finalGame)
    }

    @Test
    fun `uiState has no final model for running game`() {
        val human = testHuman("cards.entity.human.test.name")
        val context =
            GameContext(
                board = testBoard(cards = mapOf(START to listOf(human))),
                players = listOf(testPlayer("Alice", human)),
                localization = "ru",
            )

        assertNull(createViewModel(context).uiState.value.finalGame)
    }

    private fun createViewModel(context: GameContext): GameViewModel {
        return GameViewModel(ActionManager(context), ReadOnlyGameContext(context))
    }
}
