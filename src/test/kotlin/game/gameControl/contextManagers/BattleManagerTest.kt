package game.gameControl.contextManagers

import game.Player
import game.RIGHT
import game.START
import game.SpinnerValue
import game.board.Board
import game.board.Direction
import game.cards.Entity
import game.testBoard
import game.testHuman
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattleManagerTest {
    private lateinit var startHuman: Entity
    private lateinit var startMonster: Entity
    private lateinit var startHumanPlayer: Player
    private lateinit var rightHuman: Entity
    private lateinit var rightMonster: Entity
    private lateinit var rightHumanPlayer: Player
    private lateinit var rightMonsterPlayer: Player
    private lateinit var board: Board
    private lateinit var turnManager: TurnManager
    private lateinit var playersManager: PlayersManager
    private lateinit var movementManager: MovementManager
    private lateinit var healthManager: HealthManager
    private lateinit var manager: BattleManager

    @BeforeEach
    fun initialize() {
        startHuman = testHuman()
        startMonster = testMonster()
        startHumanPlayer = testPlayer("Alice", startHuman)
        rightHuman = testHuman()
        rightMonster = testMonster()
        rightHumanPlayer = testPlayer("Bob", rightHuman)
        rightMonsterPlayer = testPlayer("Cartoshka", rightMonster)
        board =
            testBoard(
                cards =
                    mapOf(
                        START to listOf(startHuman, startMonster),
                        RIGHT to listOf(rightHuman, rightMonster),
                    ),
            )
        turnManager = TurnManager(listOf(startHumanPlayer, rightHumanPlayer, rightMonsterPlayer))
        playersManager = PlayersManager(listOf(startHumanPlayer, rightHumanPlayer, rightMonsterPlayer))
        movementManager = MovementManager(turnManager, board)
        healthManager = HealthManager(playersManager, InventoryManager(), board)
        manager = BattleManager(turnManager, board, playersManager, movementManager, healthManager)
    }

    @Test
    fun `battle manager correctly gives human opponent existence`() {
        assertTrue(manager.activePlayerHasOpponent)
        board.existCellRemoveCard(START, startMonster)
        assertFalse(manager.activePlayerHasOpponent)
    }

    @Test
    fun `battle manager correctly gives monster opponent existence`() {
        turnManager.activePlayerIndex = 2
        assertTrue(manager.activePlayerHasOpponent)
        board.existCellRemoveCard(RIGHT, rightHuman)
        assertFalse(manager.activePlayerHasOpponent)
    }

    @Test
    fun `battle manager isActive works correctly`() {
        assertFalse(manager.isActive)
        manager.startBattle()
        assertTrue(manager.isActive)
        manager.endBattle()
        assertFalse(manager.isActive)
    }

    @Test
    fun `battle manager starts battle against monster correctly`() {
        manager.startBattle()
        assertTrue(manager.isActive)
        assertEquals(startMonster, manager.monster)
        assertEquals(null, manager.tempIndex)
        assertTrue(manager.monsterIsAlive)
        assertFalse(manager.isEndBattle(SpinnerValue.FANGS))
    }

    @Test
    fun `battle manager starts battle against human correctly`() {
        turnManager.activePlayerIndex = 2
        manager.startBattle()
        assertTrue(manager.isActive)
        assertEquals(rightMonster, manager.monster)
        assertEquals(2, manager.tempIndex)
        assertEquals(1, turnManager.activePlayerIndex)
        assertTrue(manager.monsterIsAlive)
        assertFalse(manager.isEndBattle(SpinnerValue.FANGS))
    }

    @Test
    fun `battle manager chooses correct opponent monster if more than one on cell`() {
        val higherMonster = testMonster()
        board.existCellAddCard(START, higherMonster)
        manager.startBattle()
        assertEquals(higherMonster, manager.monster)
    }

    @Test
    fun `battle manager chooses correct opponent human if more than one on cell`() {
        board.move(startHuman, Direction.RIGHT)
        turnManager.activePlayerIndex = 2
        manager.startBattle()
        assertEquals(0, turnManager.activePlayerIndex)
    }

    @Test
    fun `battle manager cannot start battle if already started`() {
        manager.startBattle()
        assertFailsWith<IllegalArgumentException> { manager.startBattle() }
    }

    @Test
    fun `battle manager cannot start battle if no opponents`() {
        board.existCellRemoveCard(START, startMonster)
        assertFailsWith<IllegalArgumentException> { manager.startBattle() }
    }

    @Test
    fun `battle manager monsterIsAlive works correctly`() {
        manager.startBattle()
        assertTrue(manager.monsterIsAlive)
        manager.monsterAddHealth(-1)
        assertFalse(manager.monsterIsAlive)
    }

    @Test
    fun `battle manager monsterIsAlive fails when battle is not active`() {
        assertFailsWith<IllegalArgumentException> { manager.monsterIsAlive }
    }

    @Test
    fun `battle manager monsterAddHealth works correctly`() {
        manager.startBattle()
        assertEquals(1, manager.monster!!.healthPoints)
        manager.monsterAddHealth(-1)
        assertEquals(0, manager.monster!!.healthPoints)
    }

    @Test
    fun `battle manager monsterAddHealth fails when battle is not active`() {
        assertFailsWith<IllegalArgumentException> { manager.monsterAddHealth(1) }
    }

    @Test
    fun `battle manager isEndBattle works correctly`() {
        manager.startBattle()
        assertFalse(manager.isEndBattle(SpinnerValue.FANGS))
        assertTrue(manager.isEndBattle(SpinnerValue.ESCAPE))
        manager.monsterAddHealth(-1)
        assertTrue(manager.isEndBattle(SpinnerValue.FANGS))
        manager.monsterAddHealth(1)
        assertFalse(manager.isEndBattle(SpinnerValue.FANGS))
        healthManager.addHealth(startHuman, -3)
        assertTrue(manager.isEndBattle(SpinnerValue.FANGS))
    }

    @Test
    fun `battle manager isEndBattle fails when battle is not active`() {
        assertFailsWith<IllegalArgumentException> { manager.isEndBattle(SpinnerValue.ESCAPE) }
    }

    @Test
    fun `battle manager endBattle works correctly`() {
        manager.startBattle()
        assertEquals(startMonster, manager.monster)
        manager.endBattle()
        assertEquals(null, manager.monster)
    }

    @Test
    fun `battle manager endBattle fails when battle is not active`() {
        assertFailsWith<IllegalArgumentException> { manager.endBattle() }
    }

    @Test
    fun `battle manager switchToTempIndex works correctly`() {
        turnManager.activePlayerIndex = 2
        manager.startBattle()
        assertEquals(1, turnManager.activePlayerIndex)
        manager.switchToTempIndex()
        assertEquals(2, turnManager.activePlayerIndex)
    }

    @Test
    fun `battle manager switchToTempIndex works correctly with null tempIndex`() {
        repeat(10) {
            val randomIndex = Random.nextInt(0, 2)
            turnManager.activePlayerIndex = randomIndex
            manager.switchToTempIndex()
            assertEquals(randomIndex, turnManager.activePlayerIndex)
        }
    }
}
