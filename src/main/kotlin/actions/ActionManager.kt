package actions

import Context
import Direction

class ActionManager(
    context: Context
) {
    val actionsList = listOf<Action>(
        ActionMove(context, Direction.UP),
        ActionMove(context, Direction.DOWN),
        ActionMove(context, Direction.RIGHT),
        ActionMove(context, Direction.LEFT)
    )

    val availableList = mutableListOf<Action>()

    fun generateAvailableList() {
        availableList.clear()
        for (action in actionsList) {
            if (action.isAvailable) {
                availableList.add(action)
            }
        }
    }
}