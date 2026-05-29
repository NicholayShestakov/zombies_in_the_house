package game.gameControl.contextManagers

import game.Player
import game.SpinnerValue
import game.board.Board
import game.cards.DamageType
import game.cards.Entity

class BattleManager(
    val turnManager: TurnManager,
    val board: Board,
    val playersManager: PlayersManager,
    val movementManager: MovementManager,
    val healthManager: HealthManager,
) {
    private val activePlayer: Player get() = turnManager.activePlayer
    private val activePlayerEntity: Entity get() = activePlayer.currentEntity
    private val activePlayerCoordinates: Pair<Int, Int> get() = movementManager.currentCoordinates

    val activePlayerHasOpponent: Boolean get() = board.hasOpponents(activePlayerCoordinates, activePlayer.playsAsHuman)
    var monster: Entity? = null
        private set
    var tempIndex: Int? = null
        private set

    val isActive: Boolean get() = monster != null

    fun startBattle() {
        require(!isActive) { "Battle already active" }
        require(activePlayerHasOpponent) { "Active player has not any opponents" }
        movementManager.stopStepping()
        val opponent = board.getHighestOpponent(activePlayerCoordinates, activePlayer.playsAsHuman)

        // Делаем активым игрока, который играет за человека, для удобной обработки исопльзования предметов
        if (!activePlayer.playsAsHuman) {
            monster = activePlayerEntity
            tempIndex = turnManager.activePlayerIndex
            turnManager.activePlayerIndex = playersManager.getPlayerWithEntityIndex(opponent)
        } else {
            monster = opponent
        }
    }

    val monsterIsAlive: Boolean
        get() {
            require(isActive) { "Battle not active" }
            return monster!!.healthPoints > 0
        }

    fun monsterAddHealth(count: Int) {
        require(isActive) { "Battle not active" }
        healthManager.addHealth(monster!!, count)
    }

    fun monsterCanDamagedBy(damageType: DamageType): Boolean {
        require(isActive) { "Battle not active" }
        return monster!!.canDamagedBy.contains(damageType)
    }

    fun isEndBattle(spinnerValue: SpinnerValue): Boolean {
        require(isActive) { "Battle not active" }
        return activePlayerEntity.healthPoints <= 0 || !monsterIsAlive || spinnerValue == SpinnerValue.ESCAPE
    }

    fun endBattle() {
        require(isActive) { "Battle not active" }
        monster = null
    }

    fun switchToTempIndex() {
        if (tempIndex != null) {
            turnManager.activePlayerIndex = tempIndex!!
            tempIndex = null
        }
    }
}
