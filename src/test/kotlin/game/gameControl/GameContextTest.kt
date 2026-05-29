package game.gameControl

import game.Player
import game.RIGHT
import game.SPINNER_ESCAPE_SEED
import game.SPINNER_FANGS_SEED
import game.START
import game.UP
import game.board.Board
import game.board.Direction
import game.cards.Entity
import game.cards.EntityAbility
import game.cards.ItemType
import game.testBoard
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

class GameContextTest {
    private lateinit var humans: List<Entity>
    private lateinit var monsters: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext

    @BeforeEach
    fun initialization() {
        humans =
            listOf(
                testHuman("AliceHuman", abilities = listOf(EntityAbility.START_KNIFE)),
                testHuman("BobHuman"),
            )
        monsters =
            listOf(
                testMonster("CartoshkaMonster", plusSpeed = -1),
            )
        players =
            listOf(
                testPlayer("Alice", humans[0]),
                testPlayer("Bob", humans[1]),
                testPlayer("Cartoshka", monsters[0]),
            )
        board = testBoard(cards = mapOf(START to humans, RIGHT to monsters), walls = listOf(START to UP))
        context = GameContext(board, players, "ru")
    }

    @Test
    fun `spin changes game state correctly`() {
        assertEquals(GameState.STEP_SPINNER_SPINNING, context.gameState)
        context.spin()
        assertEquals(GameState.STEP_SPINNER_SPINNED, context.gameState)

        board.existCellAddCard(START, testMonster())
        context.activePlayerStartStepping()
        context.startBattle()
        assertEquals(GameState.BATTLE_SPINNER_SPINNING, context.gameState)
        context.spin(SPINNER_ESCAPE_SEED)
        assertEquals(GameState.BATTLE_SPINNER_SPINNED, context.gameState)

        context.endBattle()
        assertEquals(GameState.ESCAPE_SPINNER_SPINNING, context.gameState)
        context.spin()
        assertEquals(GameState.ESCAPE_SPINNER_SPINNED, context.gameState)
    }

    @Test
    fun `spin fails on incorrect game state`() {
        context.spin()
        assertFailsWith<IllegalStateException> { context.spin() }
    }

    @Test
    fun `nextTurn correctly changes context`() {
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.nextTurn()
        assertEquals(GameState.STEP_SPINNER_SPINNING, context.gameState)

        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.nextTurn()
        assertEquals(GameState.MONSTER_CHOOSING, context.gameState)

        context.nextTurn()
        assertEquals(GameState.STEP_SPINNER_SPINNING, context.gameState)
    }

    @Test
    fun `nonactivePlayerHasEntity gets correct results`() {
        assertFalse(context.nonactivePlayerHasEntity(humans[0]))
        assertTrue(context.nonactivePlayerHasEntity(humans[1]))
        assertFalse(context.nonactivePlayerHasEntity(testHuman()))
    }

    @Test
    fun `playerWithEntityGetItem fails with error if player with entity does not exist`() {
        assertFailsWith<IllegalStateException> { context.playerWithEntityAddItem(testHuman(), testItem()) }
    }

    @Test
    fun `nonactiveHumansOnBoard gets only humans and only nonactive`() {
        assertEquals(listOf(humans[1]), context.nonactiveHumansOnBoard)
    }

    @Test
    fun `activePlayerInventoryTypesSet gets set of active player item types`() {
        val item1 = testItem(ItemType.KNIFE)
        val item2 = testItem(ItemType.GUN)
        val item3 = testItem(ItemType.FIRST_AID_KIT)
        context.playerWithEntityAddItem(humans[0], item1)
        context.playerWithEntityAddItem(humans[0], item2)
        context.playerWithEntityAddItem(humans[1], item3)
        assertEquals(setOf(item1.itemType, item2.itemType), context.activePlayerInventoryTypesSet)

        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.nextTurn()
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.nextTurn()
        assertEquals(setOf(), context.activePlayerInventoryTypesSet)
    }

    @Test
    fun `activePlayerHasAbility works correctly`() {
        assertTrue(context.activePlayerHasAbility(EntityAbility.START_KNIFE))

        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.nextTurn()

        assertFalse(context.activePlayerHasAbility(EntityAbility.START_KNIFE))
    }

    @Test
    fun `activePlayerSetEntity sets entity of active player and only be used in monster choosing state`() {
        val entity = testMonster()
        assertFailsWith<IllegalArgumentException> { context.activePlayerSetEntity(entity) }
        context.spin(SPINNER_ESCAPE_SEED)
        assertFailsWith<IllegalArgumentException> { context.activePlayerSetEntity(entity) }
        context.activePlayerStartStepping()
        assertFailsWith<IllegalArgumentException> { context.activePlayerSetEntity(entity) }
        context.activePlayerMove(Direction.RIGHT)
        assertFailsWith<IllegalArgumentException> { context.activePlayerSetEntity(entity) }
        context.nextTurn()
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.nextTurn()

        context.activePlayerSetEntity(entity)
        assertEquals(entity, players[2].currentEntity)
        assertEquals(GameState.STEP_SPINNER_SPINNING, context.gameState)
    }

    @Test
    fun `activePlayerStartStepping correctly gets player steps and changes game state`() {
        context.spin(SPINNER_FANGS_SEED)
        context.activePlayerStartStepping()
        assertEquals(2, context.activePlayerStepCount)
        assertEquals(GameState.STEPPING, context.gameState)
    }

    @Test
    fun `activePlayerStartStepping changes gameState on TURN_FINISH if step count instantly equals 0`() {
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.nextTurn()
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.nextTurn()
        context.activePlayerSetEntity(monsters[0])
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        assertEquals(0, context.activePlayerStepCount)
        assertEquals(GameState.TURN_FINISH, context.gameState)
    }

    @Test
    fun `activePlayerStartStepping fails on incorrect game state`() {
        assertFailsWith<IllegalStateException> { context.activePlayerStartStepping() }
    }

    @Test
    fun `activePlayerMove correctly changes player steps and changes game state`() {
        context.spin(SPINNER_FANGS_SEED)
        context.activePlayerStartStepping()
        assertEquals(2, context.activePlayerStepCount)
        assertEquals(GameState.STEPPING, context.gameState)
        context.activePlayerMove(Direction.RIGHT)
        assertEquals(1, context.activePlayerStepCount)
        assertEquals(GameState.STEPPING, context.gameState)

        context.activePlayerMove(Direction.RIGHT)
        assertEquals(0, context.activePlayerStepCount)
        assertEquals(GameState.TURN_FINISH, context.gameState)
    }

    @Test
    fun `activePlayerMove correctly changes player steps and changes game state after battle escape`() {
        context.spin(SPINNER_ESCAPE_SEED)
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.startBattle()

        context.spin(SPINNER_ESCAPE_SEED)
        context.endBattle()
        assertEquals(GameState.ESCAPE_SPINNER_SPINNING, context.gameState)
        context.spin(SPINNER_FANGS_SEED)
        assertEquals(GameState.ESCAPE_SPINNER_SPINNED, context.gameState)
        context.activePlayerStartStepping()
        assertEquals(GameState.ESCAPE, context.gameState)
        context.activePlayerMove(Direction.RIGHT)
        assertEquals(1, context.activePlayerStepCount)
        assertEquals(GameState.STEPPING, context.gameState)
        context.activePlayerMove(Direction.UP)
        assertEquals(0, context.activePlayerStepCount)
        assertEquals(GameState.TURN_FINISH, context.gameState)
    }

    @Test
    fun `activePlayerMove cannot be used in not ESCAPE and STEPPING game states and fails`() {
        assertFailsWith<IllegalArgumentException> { context.activePlayerMove(Direction.RIGHT) }
    }

    @Test
    fun `isStateForStartBattle works correctly`() {
        assertFalse(context.isStateForStartBattle)
        context.spin(SPINNER_ESCAPE_SEED)
        assertFalse(context.isStateForStartBattle)
        context.activePlayerStartStepping()
        assertTrue(context.isStateForStartBattle)
        context.activePlayerMove(Direction.RIGHT)
        assertTrue(context.isStateForStartBattle)
    }

    @Test
    fun `startBattle changes game state`() {
        context.spin()
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.startBattle()
        assertEquals(GameState.BATTLE_SPINNER_SPINNING, context.gameState)
    }

    @Test
    fun `isEndBattle gets true in correct game state`() {
        context.spin()
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.startBattle()
        assertFalse(context.isEndBattle)
        context.spin(SPINNER_ESCAPE_SEED)
        assertTrue(context.isEndBattle)
    }

    @Test
    fun `goToBattleSpinner changes state to BATTLE_SPINNER_SPINNING`() {
        context.spin()
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.startBattle()
        context.spin()
        assertEquals(GameState.BATTLE_SPINNER_SPINNED, context.gameState)
        context.goToBattleSpinner()
        assertEquals(GameState.BATTLE_SPINNER_SPINNING, context.gameState)
    }

    @Test
    fun `goInBattleState changes state to BATTLE`() {
        context.spin()
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.startBattle()
        context.spin()
        assertEquals(GameState.BATTLE_SPINNER_SPINNED, context.gameState)
        context.goInBattleState()
        assertEquals(GameState.BATTLE, context.gameState)
    }

    @Test
    fun `endBattle changes game state to TURN_FINISH if somebody dead`() {
        context.spin()
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.startBattle()
        context.monsterAddHealth(-1)
        assertEquals(GameState.BATTLE_SPINNER_SPINNING, context.gameState)
        context.endBattle()
        assertEquals(GameState.TURN_FINISH, context.gameState)
    }

    @Test
    fun `endBattle changes game state to ESCAPE_SPINNER_SPINNING if spinner gets ESCAPE`() {
        context.spin()
        context.activePlayerStartStepping()
        context.activePlayerMove(Direction.RIGHT)
        context.startBattle()
        context.spin(SPINNER_ESCAPE_SEED)
        assertEquals(GameState.BATTLE_SPINNER_SPINNED, context.gameState)
        context.endBattle()
        assertEquals(GameState.ESCAPE_SPINNER_SPINNING, context.gameState)
    }

    @Test
    fun `activePlayerPickUpItem removes item from board and adds it to player inventory`() {
        val item = testItem()
        context.cellAddCard(START, item)
        context.activePlayerPickUpItem()
        assertTrue(context.activePlayerHasItem(item.itemType))
        assertFalse(context.cellHasCard(START, item))
    }

    @Test
    fun `flip changes game state to TURN_FINISH`() {
        context.cellAddCard(START, testItem(isFaceDown = true))
        context.flip()
        assertEquals(GameState.TURN_FINISH, context.gameState)
    }
}
