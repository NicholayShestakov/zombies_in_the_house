package game.cards

class Entity(
    cardNameKey: String,
    cardDescriptionKey: String,
    val isHuman: Boolean,
    val canDamagedBy: List<DamageType>,
    var healthPoints: Int = 1,
    val abilities: List<EntityAbility> = emptyList(),
    val plusSpeed: Int = 0,
    isFaceDown: Boolean = true,
) : Card(cardNameKey, cardDescriptionKey, isFaceDown)
