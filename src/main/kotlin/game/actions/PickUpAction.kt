package game.actions

import game.gameControl.GameContext
import game.gameControl.GameState

class PickUpAction(
    val context: GameContext,
) : Action(
        listOf("actions.pick_up.name"),
        listOf("actions.pick_up.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() {
            return context.gameState in listOf(GameState.STEPPING, GameState.TURN_FINISH) &&
                context.activePlayerPlaysAsHuman &&
                context.activePlayerOnItem
        }

    override fun execute(): Boolean {
        if (isAvailable) {
            context.activePlayerPickUpItem()
            return true
        }
        return false
    }
}
