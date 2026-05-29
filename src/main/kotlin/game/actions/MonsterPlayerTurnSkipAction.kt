package game.actions

import game.gameControl.GameContext
import game.gameControl.GameState

class MonsterPlayerTurnSkipAction(
    private val context: GameContext,
) : Action(
        listOf("actions.monster_player_turn_skip.name"),
        listOf("actions.monster_player_turn_skip.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() =
            context.gameState == GameState.MONSTER_CHOOSING &&
                context.monstersOnBoard.none { !context.nonactivePlayerHasEntity(it) }

    override fun execute(): Boolean {
        if (isAvailable) {
            context.nextTurn()
            return true
        }
        return false
    }
}
