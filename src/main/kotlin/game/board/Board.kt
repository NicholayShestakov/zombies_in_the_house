package game

import game.cards.Card
import game.cards.Entity
import game.cards.Item
import kotlin.collections.filterIsInstance

class Board(
    private val cellList: List<List<Cell>>,
    private val wallList: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>
) {

    fun cellExists(coordinates: Pair<Int, Int>): Boolean {
        return coordinates.first in cellList.indices && coordinates.second in cellList[0].indices
    }

    fun cellWithCardExists(card: Card): Boolean {
        return cellList.flatten().any { it.cards.contains(card) }
    }

    private fun getExistCell(coordinates: Pair<Int, Int>): Cell {
        require(cellExists(coordinates)) { "Cell with given coordinates does not exists" }
        return cellList[coordinates.first][coordinates.second]
    }

    private fun getExistCellWithCard(card: Card): Cell {
        require(cellWithCardExists(card)) { "Cell with given card does not exists" }
        return cellList.flatten().first { it.cards.contains(card) }
    }

    fun getExistCellWithCardCoordinates(card: Card): Pair<Int, Int> {
        require(cellWithCardExists(card)) { "Cell with given card does not exists" }
        for (x in cellList.indices) {
            for (y in cellList[0].indices) {
                if (cellList[x][y].cards.contains(card)) {
                    return Pair(x, y)
                }
            }
        }
        return Pair(-1, -1) // Заглушка, до которой оно никогда не дойдёт
    }

    fun cellHasCondition(coordinates: Pair<Int, Int>, condition: CellType): Boolean {
        return cellExists(coordinates) && getExistCell(coordinates).conditions.contains(condition)
    }

    fun wallExists(coordinates1: Pair<Int, Int>, coordinates2: Pair<Int, Int>): Boolean {
        return Pair(coordinates1, coordinates2) in wallList
    }

    fun existCellHasNeighbour(coordinates: Pair<Int, Int>, direction: Direction): Boolean {
        require(cellExists(coordinates)) { "Cell with given coordinates does not exists" }
        val neighbourCoordinates = Pair(
            coordinates.first + direction.delta.first,
            coordinates.second + direction.delta.second
        )
        return cellExists(neighbourCoordinates) && !wallExists(coordinates, neighbourCoordinates)
    }

    private fun getExistCellExistNeighbour(coordinates: Pair<Int, Int>, direction: Direction): Cell {
        require(existCellHasNeighbour(coordinates, direction)) { "Neighbour cell does not exists" }
        val neighbourCoordinates = Pair(
            coordinates.first + direction.delta.first,
            coordinates.second + direction.delta.second
        )
        return getExistCell(neighbourCoordinates)
    }

    fun isCardNeighbour(coordinates: Pair<Int, Int>, card: Card): Boolean {
        return existCellHasCard(coordinates, card) ||
                existCellHasNeighbour(coordinates, Direction.UP) &&
                getExistCellExistNeighbour(coordinates, Direction.UP).cards.contains(card) ||
                existCellHasNeighbour(coordinates, Direction.DOWN) &&
                getExistCellExistNeighbour(coordinates, Direction.DOWN).cards.contains(card) ||
                existCellHasNeighbour(coordinates, Direction.LEFT) &&
                getExistCellExistNeighbour(coordinates, Direction.LEFT).cards.contains(card) ||
                existCellHasNeighbour(coordinates, Direction.RIGHT) &&
                getExistCellExistNeighbour(coordinates, Direction.RIGHT).cards.contains(card)
    }

    fun existCellHasCard(coordinates: Pair<Int, Int>, card: Card): Boolean {
        return getExistCell(coordinates).cards.contains(card)
    }

    fun existCellAddCard(coordinates: Pair<Int, Int>, card: Card) {
        require(!existCellHasCard(coordinates, card)) { "Cell already has given card" }
        getExistCell(coordinates).cards.add(card)
    }

    fun existCellHasCards(coordinates: Pair<Int, Int>, cards: List<Card>): Boolean {
        return cards.any { getExistCell(coordinates).cards.contains(it) }
    }

    fun existCellAddCards(coordinates: Pair<Int, Int>, cards: List<Card>) {
        require(!existCellHasCards(coordinates, cards)) { "Cell already has given card" }
        getExistCell(coordinates).cards.addAll(cards)
    }

    fun existCellRemoveCard(coordinates: Pair<Int, Int>, card: Card) {
        require(existCellHasCard(coordinates, card)) { "Cell does not have given card" }
        getExistCell(coordinates).cards.remove(card)
    }

    fun move(card: Card, direction: Direction) {
        val coordinates = getExistCellWithCardCoordinates(card)
        require(existCellHasNeighbour(coordinates, direction)) { "Neighbour cell does not exists" }
        require(existCellHasCard(coordinates, card)) { "Cell does not have given card" }
        val neighbourCoordinates = Pair(
            coordinates.first + direction.delta.first,
            coordinates.second + direction.delta.second
        )
        require(!existCellHasCard(neighbourCoordinates, card)) { "Neighbour cell already has given card" }
        existCellRemoveCard(coordinates, card)
        existCellAddCard(neighbourCoordinates, card)
    }

    fun hasOpponents(coordinates: Pair<Int, Int>, playsAsHuman: Boolean): Boolean {
        return if (playsAsHuman) {
            getExistCell(coordinates).cards.filterIsInstance<Entity>().any { !it.isHuman }
        } else {
            getExistCell(coordinates).cards.filterIsInstance<Entity>().any { it.isHuman }
        }
    }

    fun getHighestOpponent(coordinates: Pair<Int, Int>, playsAsHuman: Boolean): Entity {
        require(hasOpponents(coordinates, playsAsHuman)) { "Cell with given coordinates has not opponents" }
        return if (playsAsHuman) {
            getExistCell(coordinates).cards.filterIsInstance<Entity>().last { !it.isHuman }
        } else {
            getExistCell(coordinates).cards.filterIsInstance<Entity>().last { it.isHuman }
        }
    }

    fun hasItems(coordinates: Pair<Int, Int>): Boolean {
        return getExistCell(coordinates).cards.filterIsInstance<Item>().isNotEmpty()
    }

    fun getHighestItem(coordinates: Pair<Int, Int>): Item {
        require(hasItems(coordinates)) { "Cell with given coordinates has not items" }
        val item = getExistCell(coordinates).cards.first { it is Item }
        getExistCell(coordinates).cards.remove(item)
        return item as Item
    }

    fun hasFaceDown(coordinates: Pair<Int, Int>): Boolean {
        return getExistCell(coordinates).cards.any { it.isFaceDown }
    }

    fun flipFaceDown(coordinates: Pair<Int, Int>) {
        require(hasFaceDown(coordinates)) { "Cell with given coordinates has not face down cards" }
        getExistCell(coordinates).cards.last { it.isFaceDown }.flip()
    }

    fun isEmpty(coordinates: Pair<Int, Int>): Boolean {
        return getExistCell(coordinates).cards.isEmpty()
    }

    val cardsOnBoard: List<Card>
        get() = cellList.flatten().flatMap { it.cards }

    val monstersOnBoard: List<Entity>
        get() = cardsOnBoard.filterIsInstance<Entity>().filter { !it.isHuman }

    val humansOnBoard: List<Entity>
        get() = cardsOnBoard.filterIsInstance<Entity>().filter { it.isHuman }
}