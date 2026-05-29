package game.factories

import game.cards.Card
import game.cards.DamageType
import game.cards.Entity
import game.cards.Item
import game.cards.ItemType

class DeckFactory {
    fun createDefaultCardDeck(): List<Card> {
        return List(17) {
            Entity(
                "cards.entity.monster.zombie.name",
                "cards.entity.monster.zombie.description",
                false,
                listOf(DamageType.KNIFE, DamageType.GUN, DamageType.GRENADE),
                plusSpeed = -1,
            )
        } +
            List(5) {
                Entity(
                    "cards.entity.monster.spider.name",
                    "cards.entity.monster.spider.description",
                    false,
                    listOf(DamageType.KNIFE, DamageType.GUN, DamageType.GRENADE),
                )
            } +
            List(5) {
                Entity(
                    "cards.entity.monster.dog.name",
                    "cards.entity.monster.dog.description",
                    false,
                    listOf(DamageType.KNIFE, DamageType.GUN, DamageType.GRENADE),
                    plusSpeed = 1,
                )
            } +
            Entity(
                "cards.entity.monster.boss.name",
                "cards.entity.monster.boss.description",
                false,
                listOf(DamageType.GRENADE_LAUNCHER),
            ) +
            List(4) {
                Item(ItemType.GRENADE)
            } +
            List(3) {
                Item(ItemType.KNIFE)
            } +
            List(3) {
                Item(ItemType.GUN)
            } +
            List(1) {
                Item(ItemType.GRENADE_LAUNCHER)
            } +
            List(6) {
                Item(ItemType.FIRST_AID_KIT)
            } +
            List(2) {
                Item(ItemType.WIN_CONDITION)
            }
    }
}
