package pipelines

import game.gameControl.GameInitialization
import game.gameControl.GameStarter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class GameInitializationPipeline {
    @Test
    fun `game initialization components correctly creates game session`() {
        val initialization = GameInitialization()
        initialization.setPlayerStartHealth(4)
        initialization.setPlayerCount(2)
        initialization.setPlayerName(0, "Alice")
        initialization.setPlayerName(1, "Bob")
        val config = initialization.buildGameConfig()
        val starter = GameStarter()
        val manager = starter.start(config, 42)

        assertEquals(null, manager.higherAvailableAutomatic)
        assertNotEquals(listOf(), manager.availableNonautomaticList)
    }
}
