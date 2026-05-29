package game.gameControl.contextManagers

import game.board.Board
import game.board.Direction
import game.cards.Entity

class MovementManager(val turnManager: TurnManager, val board: Board) {
    private val entity: Entity get() = turnManager.activePlayer.currentEntity

    val currentCoordinates: Pair<Int, Int>
        get() =
            board.getCellWithCardCoordinates(entity)
                ?: error("Active player entity is not on board")

    var currentStepCount = 0
        private set
    val hasSteps: Boolean get() = currentStepCount > 0

    fun move(direction: Direction) {
        require(hasSteps) { "No steps left to move" }
        currentStepCount--
        board.move(entity, direction)
    }

    fun startStepping(stepCount: Int) {
        currentStepCount = stepCount + entity.plusSpeed
    }

    fun stopStepping() {
        currentStepCount = 0
    }
}
