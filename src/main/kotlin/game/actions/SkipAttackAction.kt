package game.actions

import game.gameControl.GameContext
import game.gameControl.GameState

class SkipAttackAction(
    private val context: GameContext,
) : Action(
        listOf("actions.skip_attack.name"),
        listOf("actions.skip_attack.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() {
            return context.gameState == GameState.BATTLE
        }

    override fun execute(): Boolean {
        if (isAvailable) {
            context.goToBattleSpinner()
            return true
        }
        return false
    }
}
