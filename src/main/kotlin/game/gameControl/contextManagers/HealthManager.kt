package game.gameControl.contextManagers

import game.board.Board
import game.cards.Entity

class HealthManager(
    val playersManager: PlayersManager,
    val inventoryManager: InventoryManager,
    val board: Board,
) {
    fun addHealth(
        entity: Entity,
        count: Int,
    ) {
        val player = playersManager.getPlayerWithEntity(entity)

        entity.healthPoints += count
        if (entity.healthPoints <= 0) {
            val coordinates =
                board.getCellWithCardCoordinates(entity)
                    ?: error("Entity not on board")
            board.existCellRemoveCard(coordinates, entity)
            if (player != null) {
                board.existCellAddCards(coordinates, inventoryManager.getInventoryItems(player))
            }
        }
    }
}
