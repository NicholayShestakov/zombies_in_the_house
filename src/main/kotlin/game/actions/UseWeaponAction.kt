package game.actions

import game.cards.ItemType
import game.gameControl.GameContext

class UseWeaponAction(
    val context: GameContext,
    val itemType: ItemType,
) : Action(
        listOf("actions.use_weapon.name", itemType.nameKey),
        listOf("actions.use_weapon.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() {
            return itemType.statesToUse?.contains(context.gameState) ?: false &&
                context.activePlayerHasItem(itemType) &&
                context.monsterCanDamagedBy(itemType.damageType!!) &&
                itemType.spinnerValuesToUse?.contains(context.spinnerValue) ?: false
        }

    override fun execute(): Boolean {
        if (isAvailable) {
            context.monsterAddHealth(-1)
            context.goToBattleSpinner()
            if (itemType.isSingleUsable) {
                context.activePlayerRemoveItem(itemType)
            }
            return true
        }
        return false
    }
}
