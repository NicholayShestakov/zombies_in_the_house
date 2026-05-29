package game.gameControl

import game.Player
import game.board.Board
import game.board.CellType
import game.board.Direction
import game.cards.Card
import game.cards.DamageType
import game.cards.Entity
import game.cards.EntityAbility
import game.cards.Item
import game.cards.ItemType
import game.gameControl.contextManagers.BattleManager
import game.gameControl.contextManagers.HealthManager
import game.gameControl.contextManagers.InventoryManager
import game.gameControl.contextManagers.MovementManager
import game.gameControl.contextManagers.PlayersManager
import game.gameControl.contextManagers.SpinnerManager
import game.gameControl.contextManagers.TurnManager
import game.gameControl.contextManagers.WinManager

class GameContext(
    private val board: Board,
    players: List<Player>,
    val localization: String,
    val winConditionsToWin: Int = 2,
    private val seed: Int? = null,
) {
    val playersSize = players.size

    private val turnManager = TurnManager(players)
    private val playersManager = PlayersManager(players)
    private val winManager = WinManager(winConditionsToWin)
    private val inventoryManager = InventoryManager()
    private val movementManager = MovementManager(turnManager, board)
    private val healthManager = HealthManager(playersManager, inventoryManager, board)
    private val battleManager = BattleManager(turnManager, board, playersManager, movementManager, healthManager)
    private val spinnerManager = SpinnerManager()

    var gameState = GameState.STEP_SPINNER_SPINNING
        private set

    /*
     * Spinner section
     */
    val spinnerValue get() = spinnerManager.spinnerValue

    fun spin(spinSeed: Int? = null) {
        gameState =
            when (gameState) {
                GameState.STEP_SPINNER_SPINNING -> GameState.STEP_SPINNER_SPINNED
                GameState.ESCAPE_SPINNER_SPINNING -> GameState.ESCAPE_SPINNER_SPINNED
                GameState.BATTLE_SPINNER_SPINNING -> GameState.BATTLE_SPINNER_SPINNED
                else -> error("Incorrect game state")
            }
        spinnerManager.spin(spinSeed ?: seed)
    }

    /*
     * Turn section
     */
    val activePlayerIndex: Int get() = turnManager.activePlayerIndex

    fun nextTurn() {
        require(
            gameState in
                listOf(
                    GameState.MONSTER_CHOOSING,
                    GameState.TURN_FINISH,
                ),
        ) { "Next turn can be used only on TURN_FINISH or MONSTER_CHOOSING state" }
        battleManager.switchToTempIndex()
        movementManager.stopStepping()
        turnManager.nextTurn()
        gameState = if (activePlayerPlaysAsHuman) GameState.STEP_SPINNER_SPINNING else GameState.MONSTER_CHOOSING
    }

    /*
     * Win section
     */
    fun incrementWinConditionsCount() = winManager.incrementWinConditions()

    val winConditions: Int get() = winManager.winConditions
    val isWin: Boolean get() = winManager.isWin()
    val isLose: Boolean get() = winManager.isLose(turnManager.players)

    /*
     * Players section
     */
    fun nonactivePlayerHasEntity(entity: Entity): Boolean =
        playersManager.getPlayerWithEntityIndex(entity) !in listOf(-1, activePlayerIndex)

    fun playerWithEntityAddItem(
        entity: Entity,
        item: Item,
    ) = inventoryManager.addItem(
        playersManager.getPlayerWithEntity(entity) ?: error("Player with entity does not exists"),
        item,
    )

    val playersNames: List<String> get() = turnManager.players.map { it.name }
    val playersOnFinishNames: List<String>
        get() =
            board.onFinishHumans.mapNotNull { human ->
                playersManager.getPlayerWithEntity(human)?.name
            }

    /*
     * Active player section
     */
    private val activePlayer: Player get() = turnManager.activePlayer
    val activePlayerName: String get() = activePlayer.name
    val activePlayerPlaysAsHuman: Boolean get() = activePlayer.playsAsHuman
    val activePlayerInventoryList: List<Item>
        get() = inventoryManager.getInventoryItems(activePlayer)
    val activePlayerInventoryTypesSet: Set<ItemType>
        get() = inventoryManager.getInventoryItemTypes(activePlayer).toSet()

    private val activePlayerEntity: Entity get() = turnManager.activePlayer.currentEntity
    val activePlayerEntityNameKey: String get() = activePlayerEntity.cardNameKey
    val activePlayerHealth: Int get() = activePlayerEntity.healthPoints
    val activePlayerEntityIsAlive: Boolean get() = activePlayerEntity.healthPoints > 0

    fun activePlayerHasAbility(ability: EntityAbility): Boolean = activePlayerEntity.abilities.contains(ability)

    fun activePlayerSetEntity(entity: Entity) {
        require(gameState == GameState.MONSTER_CHOOSING) { "Entity can be set only in MONSTER_CHOOSING state" }
        activePlayer.currentEntity = entity
        gameState = GameState.STEP_SPINNER_SPINNING
    }

    fun activePlayerAddHealth(count: Int) = healthManager.addHealth(activePlayerEntity, count)

    val activePlayerCoordinates: Pair<Int, Int> get() = movementManager.currentCoordinates
    val activePlayerStepCount: Int get() = movementManager.currentStepCount

    fun activePlayerStartStepping() {
        gameState =
            when (gameState) {
                GameState.STEP_SPINNER_SPINNED -> GameState.STEPPING
                GameState.ESCAPE_SPINNER_SPINNED -> GameState.ESCAPE
                else -> error("Incorrect game state")
            }
        movementManager.startStepping(spinnerValue.number)
        if (activePlayerStepCount <= 0) {
            gameState = GameState.TURN_FINISH
        }
    }

    fun activePlayerMove(direction: Direction) {
        require(gameState in listOf(GameState.STEPPING, GameState.ESCAPE)) { "Incorrect game state to move" }
        gameState = GameState.STEPPING // Для ESCAPE из битвы
        movementManager.move(direction)
        if (activePlayerStepCount == 0) {
            gameState = GameState.TURN_FINISH
        }
    }

    fun activePlayerHasItem(type: ItemType): Boolean = inventoryManager.hasItem(activePlayer, type)

    fun activePlayerRemoveItem(type: ItemType) = inventoryManager.removeItem(activePlayer, type)

    fun getActivePlayerItemWithType(type: ItemType): Item = inventoryManager.getPlayerItemWithType(activePlayer, type)

    /*
     * Battle section
     */
    val activePlayerHasOpponent: Boolean get() = battleManager.activePlayerHasOpponent
    val isStateForStartBattle: Boolean get() = gameState in listOf(GameState.STEPPING, GameState.TURN_FINISH)

    fun startBattle() {
        require(isStateForStartBattle) { "Current state does not satisfies to start battle states" }
        spinnerManager.resetForBattle()
        battleManager.startBattle()
        gameState = GameState.BATTLE_SPINNER_SPINNING
    }

    val monsterIsAlive: Boolean get() = battleManager.monsterIsAlive

    fun monsterAddHealth(count: Int) = battleManager.monsterAddHealth(count)

    fun monsterCanDamagedBy(damageType: DamageType): Boolean = battleManager.monsterCanDamagedBy(damageType)

    val isEndBattle: Boolean
        get() =
            gameState in listOf(GameState.BATTLE_SPINNER_SPINNING, GameState.BATTLE_SPINNER_SPINNED) &&
                battleManager.isEndBattle(spinnerValue)

    fun goToBattleSpinner() {
        require(
            gameState in
                listOf(
                    GameState.BATTLE,
                    GameState.BATTLE_SPINNER_SPINNED,
                ),
        ) { "Game state must be BATTLE or BATTLE_SPINNER_SPINNED for attack skip" }
        gameState = GameState.BATTLE_SPINNER_SPINNING
    }

    fun goInBattleState() {
        require(gameState == GameState.BATTLE_SPINNER_SPINNED) {
            "Game state must be BATTLE_SPINNER_SPINNED to go in battle"
        }
        gameState = GameState.BATTLE
    }

    fun endBattle() {
        require(
            gameState in
                listOf(
                    GameState.BATTLE_SPINNER_SPINNING,
                    GameState.BATTLE_SPINNER_SPINNED,
                ),
        ) { "Current game state is not a battle" }
        val somebodyDead = !activePlayerEntityIsAlive || !monsterIsAlive
        battleManager.endBattle()
        gameState =
            if (somebodyDead) {
                GameState.TURN_FINISH
            } else {
                GameState.ESCAPE_SPINNER_SPINNING
            }
    }

    /*
     * Item pick up section
     */
    val activePlayerOnItem: Boolean get() = board.hasItems(activePlayerCoordinates)

    fun activePlayerPickUpItem() {
        inventoryManager.addItem(activePlayer, board.takeHighestItem(activePlayerCoordinates))
    }

    /*
     * Card flip section
     */
    val activePlayerOnFaceDownCard: Boolean get() = board.hasFaceDown(activePlayerCoordinates)

    fun flip() {
        gameState = GameState.TURN_FINISH
        movementManager.stopStepping()
        board.flipFaceDown(activePlayerCoordinates)
    }

    /*
     * Cells section
     */
    val boardWidth: Int get() = board.width
    val boardHeight: Int get() = board.height
    val boardCellsCardsAndConditions: List<Pair<List<Card>, List<CellType>>>
        get() = board.cellsCardsAndConditions
    val boardWalls: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>
        get() = board.walls
    val monstersOnBoard: List<Entity> get() = board.monstersOnBoard
    val nonactiveHumansOnBoard: List<Entity> get() = board.humansOnBoard.filter { it != activePlayerEntity }

    fun wallExists(
        coordinates: Pair<Int, Int>,
        direction: Direction,
    ): Boolean = board.wallExists(coordinates, direction)

    fun cellExists(coordinates: Pair<Int, Int>): Boolean = board.cellExists(coordinates)

    fun cellHasCard(
        coordinates: Pair<Int, Int>,
        card: Card,
    ) = board.existCellHasCard(coordinates, card)

    fun cellAddCard(
        coordinates: Pair<Int, Int>,
        card: Card,
    ) = board.existCellAddCard(coordinates, card)

    fun cellHasCondition(
        coordinates: Pair<Int, Int>,
        condition: CellType,
    ): Boolean = board.cellHasCondition(coordinates, condition)

    fun isEntityNeighbourOfActive(
        coordinates: Pair<Int, Int>,
        entity: Entity,
    ): Boolean = board.isCardNeighbour(coordinates, entity)
}
