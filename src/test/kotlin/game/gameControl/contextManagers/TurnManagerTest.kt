package game.gameControl.contextManagers

import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class TurnManagerTest {
    private lateinit var manager: TurnManager
    private val players = listOf(testPlayer("Alice"), testPlayer("Bob"))

    @BeforeEach
    fun initializeManager() {
        manager = TurnManager(players)
    }

    @Test
    fun `turn manager correctly gets active player and his index`() {
        assertEquals(manager.activePlayer, players[0])
        assertEquals(manager.activePlayerIndex, 0)

        manager.nextTurn()

        assertEquals(manager.activePlayer, players[1])
        assertEquals(manager.activePlayerIndex, 1)

        manager.nextTurn()

        assertEquals(manager.activePlayer, players[0])
        assertEquals(manager.activePlayerIndex, 0)
    }

    @Test
    fun `turn manager correctly works with one player`() {
        val onePlayerList = listOf(testPlayer("Alice"))
        val onePlayerManager = TurnManager(onePlayerList)

        assertEquals(onePlayerManager.activePlayer, onePlayerList[0])
        assertEquals(onePlayerManager.activePlayerIndex, 0)

        onePlayerManager.nextTurn()

        assertEquals(onePlayerManager.activePlayer, onePlayerList[0])
        assertEquals(onePlayerManager.activePlayerIndex, 0)
    }
}
