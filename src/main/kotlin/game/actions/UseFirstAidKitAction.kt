package game.actions

import game.cards.EntityAbility
import game.cards.ItemType
import game.gameControl.GameContext

class UseFirstAidKitAction(
    private val context: GameContext,
) : Action(
        listOf("actions.use_first_aid_kit.name"),
        listOf("actions.use_first_aid_kit.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() {
            return context.activePlayerHasItem(ItemType.FIRST_AID_KIT)
        }

    override fun execute(): Boolean {
        if (isAvailable) {
            context.activePlayerRemoveItem(ItemType.FIRST_AID_KIT)
            context.activePlayerAddHealth(1)
            if (context.activePlayerHasAbility(EntityAbility.DOUBLE_HEAL)) {
                context.activePlayerAddHealth(1)
            }
            return true
        }
        return false
    }
}
