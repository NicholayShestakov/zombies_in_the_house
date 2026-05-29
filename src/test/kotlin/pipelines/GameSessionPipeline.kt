package pipelines

import game.actions.ActionManager
import game.gameControl.GameInitialization
import game.gameControl.GameStarter
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GameSessionPipeline {
    fun getManager(): ActionManager {
        val initialization = GameInitialization()
        initialization.setPlayerStartHealth(2)
        initialization.setPlayerCount(2)
        initialization.setPlayerName(0, "Alice")
        initialization.setPlayerName(1, "Bob")
        val config = initialization.buildGameConfig()
        val starter = GameStarter()
        val (manager) = starter.start(config, 0) // Сид подобран таким образом, чтобы на волчке всегда выпадали клыки
        return manager
    }

    @Test
    fun `game session flows correctly to lose`() {
        val manager = getManager()

        // Turn 1
        manager.availableNonautomaticList[0].execute() // Spinner
        manager.higherAvailableAutomatic!!.execute() // Spinner spinned
        manager.availableNonautomaticList[0].execute() // Move up
        manager.higherAvailableAutomatic!!.execute() // Flip card
        manager.higherAvailableAutomatic!!.execute() // Start battle
        manager.availableNonautomaticList[0].execute() // Spinner
        manager.higherAvailableAutomatic!!.execute() // Spinner spinned
        manager.availableNonautomaticList[0].execute() // Spinner
        manager.higherAvailableAutomatic!!.execute() // Spinner spinned
        manager.higherAvailableAutomatic!!.execute() // End battle (player dead)
        manager.availableNonautomaticList[0].execute() // Finish turn

        // Turn 2
        manager.availableNonautomaticList[0].execute() // Spinner
        manager.higherAvailableAutomatic!!.execute() // Spinner spinned
        manager.availableNonautomaticList[0].execute() // Move up
        manager.higherAvailableAutomatic!!.execute() // Start battle
        manager.availableNonautomaticList[0].execute() // Spinner
        manager.higherAvailableAutomatic!!.execute() // Spinner spinned
        manager.availableNonautomaticList[0].execute() // Spinner
        manager.higherAvailableAutomatic!!.execute() // Spinner spinned
        try {
            manager.higherAvailableAutomatic!!.execute()
        } catch (e: Exception) {
            assertEquals("Game finished. Players lose.", e.message)
        }
    }
}
