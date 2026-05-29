package game

import game.board.Board
import game.board.Cell
import game.board.CellType
import game.cards.Card
import game.cards.DamageType
import game.cards.Entity
import game.cards.EntityAbility
import game.cards.Item
import game.cards.ItemType

internal val START = 0 to 0
internal val RIGHT = 1 to 0
internal val UP = 0 to 1
internal val FINISH = 2 to 2
internal val DEFAULT_MONSTER_DAMAGE_TYPES = listOf(DamageType.KNIFE, DamageType.GUN, DamageType.GRENADE)
internal const val SPINNER_ESCAPE_SEED = 3
internal const val SPINNER_FANGS_SEED = 0
internal const val SPINNER_BLADES_SEED = 4
internal const val SPINNER_AIM_SEED = 2

internal fun testHuman(
    nameKey: String = "cards.entity.human.test.name",
    healthPoints: Int = 3,
    abilities: List<EntityAbility> = emptyList(),
    plusSpeed: Int = 0,
    isFaceDown: Boolean = false,
): Entity =
    Entity(
        nameKey,
        "cards.entity.human.test.description",
        isHuman = true,
        canDamagedBy = listOf(DamageType.FANGS),
        healthPoints = healthPoints,
        abilities = abilities,
        plusSpeed = plusSpeed,
        isFaceDown = isFaceDown,
    )

internal fun testMonster(
    nameKey: String = "cards.entity.monster.test.name",
    healthPoints: Int = 1,
    canDamagedBy: List<DamageType> = DEFAULT_MONSTER_DAMAGE_TYPES,
    plusSpeed: Int = 0,
    isFaceDown: Boolean = false,
): Entity =
    Entity(
        nameKey,
        "cards.entity.monster.test.description",
        isHuman = false,
        canDamagedBy = canDamagedBy,
        healthPoints = healthPoints,
        plusSpeed = plusSpeed,
        isFaceDown = isFaceDown,
    )

internal fun testItem(
    itemType: ItemType = ItemType.KNIFE,
    isFaceDown: Boolean = false,
): Item = Item(itemType, isFaceDown)

internal fun testPlayer(
    name: String = "Player",
    entity: Entity = testHuman(),
): Player = Player(name, entity)

internal fun testBoard(
    width: Int = 3,
    height: Int = 3,
    walls: List<Pair<Pair<Int, Int>, Pair<Int, Int>>> = emptyList(),
    conditions: Map<Pair<Int, Int>, List<CellType>> =
        mapOf(
            START to listOf(CellType.START),
            FINISH to listOf(CellType.FINISH),
        ),
    cards: Map<Pair<Int, Int>, List<Card>> = emptyMap(),
): Board {
    val cells =
        List(width) { x ->
            List(height) { y ->
                val coordinates = x to y
                Cell(
                    conditions = conditions[coordinates] ?: emptyList(),
                    cards = cards[coordinates]?.toMutableList() ?: mutableListOf(),
                )
            }
        }
    return Board(cells, walls)
}
