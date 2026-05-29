package game.actions

import game.gameControl.GameContext
import game.gameControl.GameState

class SpinnerAction(
    private val context: GameContext,
) : Action(
        listOf("actions.spinner.name"),
        listOf("actions.spinner.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() =
            context.gameState == GameState.STEP_SPINNER_SPINNING ||
                context.gameState == GameState.ESCAPE_SPINNER_SPINNING ||
                context.gameState == GameState.BATTLE_SPINNER_SPINNING

    override fun execute(): Boolean {
        if (isAvailable) {
            context.spin()
            return true
        }
        return false
    }
}
