package game.gameControl.contextManagers

import game.Player
import game.cards.Item
import game.cards.ItemType

class InventoryManager {
    fun hasItem(
        player: Player,
        type: ItemType,
    ): Boolean {
        return player.inventory.any { it.itemType == type }
    }

    fun getPlayerItemWithType(
        player: Player,
        type: ItemType,
    ): Item {
        require(player.playsAsHuman) { "Only human players can hold items" }
        require(hasItem(player, type)) { "Player has not item with given type" }
        return player.inventory.first { it.itemType == type }
    }

    fun addItem(
        player: Player,
        item: Item,
    ) {
        require(player.playsAsHuman) { "Only human players can hold items" }
        require(!player.inventory.contains(item)) { "Player already has given item" }
        player.inventory.add(item)
    }

    fun removeItem(
        player: Player,
        type: ItemType,
    ) {
        require(player.playsAsHuman) { "Only human players can hold items" }
        require(hasItem(player, type)) { "Player has not item with given type" }
        player.inventory.remove(getPlayerItemWithType(player, type))
    }

    fun getInventoryItems(player: Player): List<Item> {
        return player.inventory
    }

    fun getInventoryItemTypes(player: Player): List<ItemType> {
        return player.inventory.map { it.itemType }
    }
}
