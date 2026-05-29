package game.actions

import game.cards.Entity
import game.cards.ItemType
import game.gameControl.GameContext
import game.gameControl.GameState

class GiveItemToOtherPlayerAction(
    private val context: GameContext,
    private val otherPlayerHuman: Entity,
    private val itemType: ItemType,
) : Action(
        listOf("actions.give_item_to_other_player.name"),
        listOf("actions.give_item_to_other_player.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() {
            return context.gameState == GameState.TURN_FINISH &&
                context.nonactivePlayerHasEntity(otherPlayerHuman) &&
                context.activePlayerHasItem(itemType) &&
                otherPlayerHuman.isHuman &&
                context.isEntityNeighbourOfActive(context.activePlayerCoordinates, otherPlayerHuman)
        }

    override fun execute(): Boolean {
        if (isAvailable) {
            context.playerWithEntityAddItem(otherPlayerHuman, context.getActivePlayerItemWithType(itemType))
            context.activePlayerRemoveItem(itemType)
            return true
        }
        return false
    }
}
