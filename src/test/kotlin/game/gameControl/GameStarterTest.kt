package game.gameControl

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class GameStarterTest {
    @Test
    fun `start builds context from config`() {
        val manager =
            GameStarter().start(
                GameConfig(
                    playerCount = 2,
                    playerNames = listOf("Alice", "Bob"),
                    startHealth = 3,
                    localization = "ru",
                ),
            )

        assertEquals(null, manager.higherAvailableAutomatic)
        assertNotEquals(listOf(), manager.availableNonautomaticList)
    }
}
