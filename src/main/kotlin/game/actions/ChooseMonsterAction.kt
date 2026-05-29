package game.actions

import game.cards.Entity
import game.gameControl.GameContext
import game.gameControl.GameState

class ChooseMonsterAction(
    private val context: GameContext,
    private val monster: Entity,
) : Action(
        listOf("actions.choose_monster.name", monster.cardNameKey),
        listOf("actions.choose_monster.description", monster.cardDescriptionKey),
        context,
    ) {
    override val isAvailable: Boolean
        get() =
            context.gameState == GameState.MONSTER_CHOOSING &&
                !context.nonactivePlayerHasEntity(monster)

    override fun execute(): Boolean {
        if (isAvailable) {
            context.activePlayerSetEntity(monster)
            return true
        }
        return false
    }
}
