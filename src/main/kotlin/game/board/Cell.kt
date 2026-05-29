package game.board

import game.cards.Card

class Cell(
    val conditions: List<CellType> = listOf(),
    val cards: MutableList<Card> = mutableListOf<Card>(),
)
