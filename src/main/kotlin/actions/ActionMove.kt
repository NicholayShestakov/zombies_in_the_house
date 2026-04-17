package actions

import Context
import Direction

class ActionMove(
    val context: Context,
    val direction: Direction
): Action {

    override val automatic: Boolean
        get() = false
    override val isAvailable: Boolean
        get() {
            return currentCell != null && context.board.getNeighbour(currentCell, direction) != null
        }
    val currentCell = context.turn.currentCell

    fun actionMove(): Boolean {
        if (isAvailable) {
            val neighbourCell = context.board.getNeighbour(currentCell!!, direction)
            val currentEntity = context.turn.currentPlayer.currentEntity
            currentCell.removeCard(currentEntity!!)
            neighbourCell!!.addCard(currentEntity)
            return true
        }
        return false
    }
}