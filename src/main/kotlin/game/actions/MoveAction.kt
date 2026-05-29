package game.actions

import game.gameControl.GameContext
import game.Direction

class ActionMove(
    val context: GameContext,
    val direction: Direction,
): Action {

    override val availableStates = emptyList<ActionStates>()
    override val isAvailable: Boolean
        get() {

        }

    override fun action(): Boolean {

    }
}