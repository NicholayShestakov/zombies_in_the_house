package game.actions

import game.gameControl.GameContext

class EndBattleAction(
    val context: GameContext,
) : Action(
        listOf("actions.end_battle.name"),
        listOf("actions.end_battle.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() = context.isEndBattle

    override fun execute(): Boolean {
        if (isAvailable) {
            context.endBattle()
            return true
        }
        return false
    }
}
