package game.actions

import game.gameControl.GameContext

class EndGameAction(
    private val context: GameContext,
) : Action(
        listOf("actions.end_game.name"),
        listOf("actions.end_game.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() = context.isWin || context.isLose

    override fun execute(): Boolean {
        if (isAvailable) {
            throw (Exception("Game finished. Players ${if (context.isWin) "win" else "lose"}."))
        }
        return false
    }
}
