package game.actions

import game.gameControl.GameContext
import game.gameControl.GameState

class FlipCardAction(
    private val context: GameContext,
) : Action(
        listOf("actions.flip_card.name"),
        listOf("actions.flip_card.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() {
            try {
                return context.activePlayerOnFaceDownCard &&
                    context.activePlayerPlaysAsHuman &&
                    context.gameState in listOf(GameState.STEPPING, GameState.TURN_FINISH)
            } catch (e: IllegalStateException) {
                if (e.message == "Active player entity is not on board") {
                    return false
                }
                throw (e)
            }
        }

    override fun execute(): Boolean {
        if (isAvailable) {
            context.flip()
            return true
        }
        return false
    }
}
