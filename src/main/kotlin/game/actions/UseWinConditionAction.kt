package game.actions

import game.board.CellType
import game.cards.ItemType
import game.gameControl.GameContext
import game.gameControl.GameState

class UseWinConditionAction(
    val context: GameContext,
) : Action(
        listOf("actions.use_win_condition.name"),
        listOf("actions.use_win_condition.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() {
            return context.gameState in listOf(GameState.STEPPING, GameState.TURN_FINISH) &&
                context.activePlayerHasItem(ItemType.WIN_CONDITION) &&
                context.cellHasCondition(context.activePlayerCoordinates, CellType.FINISH)
        }

    override fun execute(): Boolean {
        if (isAvailable) {
            context.incrementWinConditionsCount()
            context.activePlayerRemoveItem(ItemType.WIN_CONDITION)
            return true
        }
        return false
    }
}
