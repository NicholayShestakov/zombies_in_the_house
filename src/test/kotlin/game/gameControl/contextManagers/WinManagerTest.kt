package game.gameControl.contextManagers

import game.testHuman
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WinManagerTest {
    private lateinit var manager: WinManager

    @BeforeEach
    fun initializeManager() {
        manager = WinManager(2)
    }

    @Test
    fun `win manager detects win by collected conditions`() {
        assertFalse(manager.isWin())
        manager.incrementWinConditions()
        assertFalse(manager.isWin())
        manager.incrementWinConditions()
        assertTrue(manager.isWin())
    }

    @Test
    fun `win manager detects lose by all player humans are dead`() {
        val player0 = testPlayer("0 health", testHuman(healthPoints = 0))
        val player1 = testPlayer("1 health", testHuman(healthPoints = 1))

        assertTrue(manager.isLose(listOf(player0)))
        assertFalse(manager.isLose(listOf(player1)))
        assertFalse(manager.isLose(listOf(player0, player1)))

        player1.currentEntity.healthPoints--

        assertTrue(manager.isLose(listOf(player1)))
        assertTrue(manager.isLose(listOf(player0, player1)))
    }

    @Test
    fun `win manager detects lose by players with monsters`() {
        val playerMonster = testPlayer("monster", testMonster())

        assertTrue(manager.isLose(listOf(playerMonster)))
    }
}
