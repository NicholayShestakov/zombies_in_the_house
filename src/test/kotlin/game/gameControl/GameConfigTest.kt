package game.gameControl

import kotlin.test.Test
import kotlin.test.assertEquals

class GameConfigTest {
    @Test
    fun `config stores startup settings and default localization`() {
        val defaultConfig = GameConfig(1, listOf("Alice"), 3)
        val customConfig = GameConfig(2, listOf("Alice", "Bob"), 5, "en")

        assertEquals("ru", defaultConfig.localization)
        assertEquals(2, customConfig.playerCount)
        assertEquals(listOf("Alice", "Bob"), customConfig.playerNames)
        assertEquals(5, customConfig.startHealth)
        assertEquals("en", customConfig.localization)
    }
}
