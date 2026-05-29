package game.gameControl.contextManagers

import game.testHuman
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayersManagerTest {
    private lateinit var manager: PlayersManager
    private val players = listOf(testPlayer("Alice", testHuman("first")), testPlayer("Bob", testHuman("second")))

    @BeforeEach
    fun initializeManager() {
        manager = PlayersManager(players)
    }

    @Test
    fun `manager finds player and index by entity`() {
        assertEquals(players[0], manager.getPlayerWithEntity(players[0].currentEntity))
        assertEquals(1, manager.getPlayerWithEntityIndex(players[1].currentEntity))
    }

    @Test
    fun `manager gets null and -1 if player with entity does not exists`() {
        assertEquals(null, manager.getPlayerWithEntity(testHuman("not exists")))
        assertEquals(-1, manager.getPlayerWithEntityIndex(testHuman("not exists")))
    }
}
