package game.gameControl

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GameInitializationTest {
    private lateinit var initialization: GameInitialization

    @BeforeEach
    fun initializeInitialization() {
        initialization = GameInitialization()
    }

    @Test
    fun `initialization builds valid config with valid data`() {
        assertEquals(1, initialization.playerCount)
        assertEquals(listOf("Player 1"), initialization.playerNamesList)
        assertEquals(3, initialization.startHealth)
        assertEquals(3, initialization.setPlayerCount(3))
        assertEquals("Alice", initialization.setPlayerName(0, "Alice"))
        assertEquals("Bob", initialization.setPlayerName(1, "Bob"))
        assertEquals("Carol", initialization.setPlayerName(2, "Carol"))
        assertEquals(5, initialization.setPlayerStartHealth(5))
        assertEquals(GameConfig(3, listOf("Alice", "Bob", "Carol"), 5), initialization.buildGameConfig())
    }

    @Test
    fun `initialization fails when player count is illegal`() {
        assertFailsWith<IllegalArgumentException> { initialization.setPlayerCount(0) }
        assertFailsWith<IllegalArgumentException> { initialization.setPlayerCount(6) }
    }

    @Test
    fun `initialization fails when player start health is illegal`() {
        assertFailsWith<IllegalArgumentException> { initialization.setPlayerStartHealth(0) }
    }

    @Test
    fun `initialization fails when player name index is out of range`() {
        assertFailsWith<IllegalArgumentException> { initialization.setPlayerName(-1, "Bob") }
        assertFailsWith<IllegalArgumentException> { initialization.setPlayerName(1, "Bob") }
    }

    @Test
    fun `initialization fails when player name is already exists`() {
        initialization.setPlayerCount(2)
        initialization.setPlayerName(0, "Alice")
        assertFailsWith<IllegalArgumentException> { initialization.setPlayerName(1, "Alice") }
    }

    @Test
    fun `initialization not fails when player name is already exists on this player`() {
        initialization.setPlayerName(0, "Alice")
        initialization.setPlayerName(0, "Alice")
    }

    @Test
    fun `initialization fails when config builds with empty player name`() {
        initialization.setPlayerName(0, "")
        assertFailsWith<IllegalArgumentException> { initialization.buildGameConfig() }
    }
}
