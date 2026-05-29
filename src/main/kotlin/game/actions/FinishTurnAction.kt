package game.actions

import game.gameControl.GameContext
import game.gameControl.GameState

class FinishTurnAction(
    val context: GameContext,
) : Action(
        listOf("actions.finish_turn.name"),
        listOf("actions.finish_turn.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() {
            return context.gameState == GameState.TURN_FINISH
        }

    override fun execute(): Boolean {
        if (isAvailable) {
            context.nextTurn()
            return true
        }
        return false
    }
}
