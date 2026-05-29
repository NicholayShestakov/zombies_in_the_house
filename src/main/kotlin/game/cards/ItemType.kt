package game.cards

import game.SpinnerValue
import game.gameControl.GameState

enum class ItemType(
    val nameKey: String,
    val descriptionKey: String,
    val damageType: DamageType? = null,
    val spinnerValuesToUse: List<SpinnerValue>? = null,
    val statesToUse: List<GameState>? = null,
    val isSingleUsable: Boolean = true,
) {
    FIRST_AID_KIT(
        "enum.item_type.first_aid_kit.name",
        "enum.item_type.first_aid_kit.description",
    ),
    WIN_CONDITION(
        "enum.item_type.win_condition.name",
        "enum.item_type.win_condition.description",
    ),
    KNIFE(
        "enum.item_type.knife.name",
        "enum.item_type.knife.description",
        DamageType.KNIFE,
        listOf(SpinnerValue.BLADES),
        listOf(GameState.BATTLE),
        false,
    ),
    GUN(
        "enum.item_type.gun.name",
        "enum.item_type.gun.description",
        DamageType.GUN,
        listOf(SpinnerValue.AIM),
        listOf(GameState.BATTLE),
    ),
    GRENADE(
        "enum.item_type.grenade.name",
        "enum.item_type.grenade.description",
        DamageType.GRENADE,
        listOf(SpinnerValue.ESCAPE, SpinnerValue.FANGS, SpinnerValue.BLADES, SpinnerValue.AIM),
        listOf(GameState.BATTLE, GameState.BATTLE_SPINNER_SPINNING),
    ),
    GRENADE_LAUNCHER(
        "enum.item_type.grenade_launcher.name",
        "enum.item_type.grenade_launcher.description",
        DamageType.GRENADE_LAUNCHER,
        listOf(SpinnerValue.ESCAPE, SpinnerValue.FANGS, SpinnerValue.BLADES, SpinnerValue.AIM),
        listOf(GameState.BATTLE, GameState.BATTLE_SPINNER_SPINNING),
    ),
}
