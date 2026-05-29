package actions

import GameContext
import Direction

class ActionManager(
    gameContext: GameContext
) {

    val automaticList = listOf<Action>( // Чем выше в списке, тем больше приоритет

    )

    val nonautomaticList = listOf<Action>(
        ActionMove(gameContext, Direction.UP),
        ActionMove(gameContext, Direction.DOWN),
        ActionMove(gameContext, Direction.RIGHT),
        ActionMove(gameContext, Direction.LEFT)
    )

    val higherAvailableAutomatic: Action?
        get() = automaticList.find { it.isAvailable }

    val availableNonautomaticList: List<Action>
        get() = nonautomaticList.filter { it.isAvailable }
}