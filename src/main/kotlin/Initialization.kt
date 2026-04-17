import cards.Card
import cards.Human

class Initialization(
    private val cardDeck: MutableList<Card>,
    private val cellList: List<List<Cell>>,
    private val wallList: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>
) {

    private var playerCount = 1
    private var playerList = mutableListOf<Player>()
    private val availableHumanList: MutableList<Human> = cardDeck.filterIsInstance<Human>() as MutableList<Human>
    private var activeHumanList = mutableListOf<Human>()
    private lateinit var board: Board

    init {
        require(cardDeck.isNotEmpty()) { "Card deck cannot be empty" }
        require(cardDeck.filterIsInstance<Human>().isNotEmpty()) { "Card deck must contain humans" }
        require(cellList.flatten().isNotEmpty()) { "Cell list cannot be empty" }
        require(cellList.flatten().any { CellCondition.START in it.conditions }) { "Cell list must contain start cell" }
        require(
            cellList.flatten().any { CellCondition.FINISH in it.conditions }) { "Cell list must contain finish cell" }
        require(cardDeck.size <= cellList.flatten().filterNot { CellCondition.START in it.conditions }
            .toSet().size) { "Cells count must be bigger than cards count" }
    }

    fun setPlayerCount(count: Int): Boolean {
        if (count in 1..availableHumanList.size) {
            playerCount = count
            return true
        }
        return false
    }

    fun initializePlayers() {
        for (i in 0..<playerCount) {
            val randomHuman = availableHumanList.random()
            availableHumanList.remove(randomHuman)
            activeHumanList.add(randomHuman)
            playerList.add(Player(i, randomHuman))
        }
    }

    fun createEmptyBoardWithWalls(width: Int, height: Int) {
        board = Board(width, height, cellList, wallList)
    }

    fun fillCellsWithCards() {
        for (card in cardDeck) {
            if (card in activeHumanList) {
                cellList.flatten().filter { CellCondition.START in it.conditions }.random().addContent(card)
                card.flip()
            } else {
                cellList.flatten().filterNot { CellCondition.START in it.conditions || it.getContent().isEmpty() }
                    .random().addContent(card)
            }
        }
    }

    fun getContext(): Context {
        return Context()
    }
}