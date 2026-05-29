package game.actions

import game.board.Direction
import game.cards.ItemType
import game.gameControl.GameContext

class ActionManager(
    private val context: GameContext,
) {
    private val automaticActions = // Чем выше, тем больше приоритет
        listOf(
            EndGameAction(context),
            MonsterPlayerTurnSkipAction(context),
            SpinnerSpinnedAction(context),
            EndBattleAction(context),
            StartBattleAction(context),
            PickUpAction(context),
            FlipCardAction(context),
        )

    private val nonautomaticActions =
        context.monstersOnBoard.map { ChooseMonsterAction(context, it) } +
            FinishTurnAction(context) +
            context.nonactiveHumansOnBoard.flatMap { human ->
                context.activePlayerInventoryTypesSet.map {
                    GiveItemToOtherPlayerAction(
                        context,
                        human,
                        it,
                    )
                }
            } +
            Direction.entries.map { MoveAction(context, it) } +
            SkipAttackAction(context) +
            SpinnerAction(context) +
            UseFirstAidKitAction(context) +
            listOf(ItemType.KNIFE, ItemType.GUN, ItemType.GRENADE, ItemType.GRENADE_LAUNCHER).map {
                UseWeaponAction(
                    context,
                    it,
                )
            } +
            UseWinConditionAction(context)

    val higherAvailableAutomatic: Action?
        get() = automaticActions.firstOrNull { it.isAvailable }

    val availableNonautomaticList: List<Action>
        get() = nonautomaticActions.filter { it.isAvailable }
}
