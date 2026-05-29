package game.actions

import game.DEFAULT_MONSTER_DAMAGE_TYPES
import game.Player
import game.SPINNER_AIM_SEED
import game.SPINNER_BLADES_SEED
import game.START
import game.board.Board
import game.cards.DamageType
import game.cards.Entity
import game.cards.ItemType
import game.gameControl.GameContext
import game.gameControl.GameState
import game.testBoard
import game.testHuman
import game.testItem
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UseWeaponActionTest {
    private lateinit var entities: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: List<UseWeaponAction>

    @BeforeEach
    fun initialization() {
        entities =
            listOf(
                testHuman(),
                testMonster(canDamagedBy = DEFAULT_MONSTER_DAMAGE_TYPES + DamageType.GRENADE_LAUNCHER),
            )
        players =
            listOf(
                testPlayer("Alice", entities[0]),
            )
        players[0].inventory.addAll(
            listOf(
                testItem(ItemType.KNIFE),
                testItem(ItemType.GUN),
                testItem(ItemType.GRENADE),
                testItem(ItemType.GRENADE_LAUNCHER),
            ),
        )
        board = testBoard(cards = mapOf(START to entities))
        context = GameContext(board, players, "ru")
        action = players[0].inventory.map { UseWeaponAction(context, it.itemType) }
    }

    @Test
    fun `grenade and grenade launcher available in BATTLE_SPINNER_SPINNED and BATTLE`() {
        context.spin()
        context.activePlayerStartStepping()
        context.startBattle()

        assertTrue(action[2].isAvailable)
        assertTrue(action[3].isAvailable)

        context.spin(SPINNER_BLADES_SEED)
        context.goInBattleState()

        assertTrue(action[2].isAvailable)
        assertTrue(action[3].isAvailable)
    }

    @Test
    fun `knife and gun available in BATTLE with their own spinner value`() {
        context.spin()
        context.activePlayerStartStepping()
        context.startBattle()

        assertFalse(action[0].isAvailable)
        assertFalse(action[1].isAvailable)

        context.spin(SPINNER_BLADES_SEED)
        context.goInBattleState()

        assertTrue(action[0].isAvailable)
        assertFalse(action[1].isAvailable)

        context.goToBattleSpinner()
        context.spin(SPINNER_AIM_SEED)
        context.goInBattleState()

        assertFalse(action[0].isAvailable)
        assertTrue(action[1].isAvailable)
    }

    @Test
    fun `weapon usage changes battle game state to SPINNER_SPINNING`() {
        context.spin()
        context.activePlayerStartStepping()
        context.startBattle()
        context.spin(SPINNER_BLADES_SEED)
        context.goInBattleState()

        assertEquals(GameState.BATTLE, context.gameState)
        action[0].execute()
        assertEquals(GameState.BATTLE_SPINNER_SPINNING, context.gameState)
    }

    @Test
    fun `single use weapons removes from player inventory after use`() {
        context.spin()
        context.activePlayerStartStepping()
        context.startBattle()
        context.spin(SPINNER_BLADES_SEED)
        context.goInBattleState()

        assertTrue(context.activePlayerHasItem(ItemType.GRENADE))
        action[2].execute()
        assertFalse(context.activePlayerHasItem(ItemType.GRENADE))
    }
}
