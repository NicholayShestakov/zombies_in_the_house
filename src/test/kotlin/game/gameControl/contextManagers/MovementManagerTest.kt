package game.gameControl.contextManagers

import game.RIGHT
import game.START
import game.board.Direction
import game.testBoard
import game.testHuman
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MovementManagerTest {
    private lateinit var manager: MovementManager
    private val humanList = listOf(testHuman("human"), testHuman("runnerHuman", plusSpeed = 1))
    private val playerList = listOf(testPlayer("player", humanList[0]), testPlayer("runnerPlayer", humanList[1]))
    private val turnManager = TurnManager(playerList)
    private val board = testBoard(cards = mapOf(START to humanList))

    @BeforeEach
    fun initializeManager() {
        manager = MovementManager(turnManager, board)
    }

    @Test
    fun `movement manager gets correct coordinates`() {
        assertEquals(START, manager.currentCoordinates)
    }

    @Test
    fun `movement manager correctly initializes step count with and without bonus speed and correctly stops`() {
        manager.startStepping(2)
        assertEquals(2, manager.currentStepCount)
        manager.stopStepping()
        assertEquals(0, manager.currentStepCount)

        turnManager.activePlayerIndex = 1

        manager.startStepping(2)
        assertEquals(3, manager.currentStepCount)
        manager.stopStepping()
        assertEquals(0, manager.currentStepCount)
    }

    @Test
    fun `movement manager correctly moves entity`() {
        manager.startStepping(2)
        manager.move(Direction.RIGHT)
        assertEquals(RIGHT, manager.currentCoordinates)
        assertEquals(1, manager.currentStepCount)
    }

    @Test
    fun `movement manager fails with error when moving with 0 steps`() {
        assertFailsWith<IllegalArgumentException> { manager.move(Direction.RIGHT) }
    }
}
