package game.cards

class Item(
    val itemType: ItemType,
    isFaceDown: Boolean = true,
) : Card(
        itemType.nameKey,
        itemType.descriptionKey,
        isFaceDown,
    )
