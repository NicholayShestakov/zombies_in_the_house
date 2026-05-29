package game.actions

import game.board.Direction
import game.gameControl.GameContext
import game.gameControl.GameState

class MoveAction(
    private val context: GameContext,
    private val direction: Direction,
) : Action(
        listOf("actions.move.name", direction.nameKey),
        listOf("actions.move.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() {
            try {
                val coords = context.activePlayerCoordinates
                val neighbourCoords = Pair(coords.first + direction.delta.first, coords.second + direction.delta.second)
                return context.gameState in listOf(GameState.ESCAPE, GameState.STEPPING) &&
                    !context.wallExists(context.activePlayerCoordinates, direction) &&
                    context.cellExists(neighbourCoords)
            } catch (e: IllegalStateException) {
                if (e.message == "Active player entity is not on board") {
                    return false
                }
                throw (e)
            }
        }

    override fun execute(): Boolean {
        if (isAvailable) {
            context.activePlayerMove(direction)
            return true
        }
        return false
    }
}
