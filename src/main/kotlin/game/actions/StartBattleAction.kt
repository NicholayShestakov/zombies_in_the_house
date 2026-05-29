package game.actions

import game.gameControl.GameContext

class StartBattleAction(
    private val context: GameContext,
) : Action(
        listOf("actions.start_battle.name"),
        listOf("actions.start_battle.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() {
            try {
                return context.isStateForStartBattle &&
                    context.activePlayerHasOpponent
            } catch (e: IllegalStateException) {
                if (e.message == "Active player entity is not on board") {
                    return false
                }
                throw (e)
            }
        }

    override fun execute(): Boolean {
        if (isAvailable) {
            context.startBattle()
            return true
        }
        return false
    }
}
