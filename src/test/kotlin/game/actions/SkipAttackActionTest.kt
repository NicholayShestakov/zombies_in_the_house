package game.actions

import game.Player
import game.SPINNER_AIM_SEED
import game.SPINNER_BLADES_SEED
import game.SPINNER_ESCAPE_SEED
import game.SPINNER_FANGS_SEED
import game.START
import game.board.Board
import game.cards.Entity
import game.gameControl.GameContext
import game.testBoard
import game.testHuman
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class SkipAttackActionTest {
    private lateinit var entities: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: SkipAttackAction

    @BeforeEach
    fun initialization() {
        entities =
            listOf(
                testHuman(),
                testMonster(),
            )
        players =
            listOf(
                testPlayer("Alice", entities[0]),
                testPlayer("Bob", entities[1]),
            )
        board = testBoard(cards = mapOf(START to entities))
        context = GameContext(board, players, "ru")
        action = SkipAttackAction(context)
    }

    @Test
    fun `player can skip attack if spinner value is BLADES of AIM`() {
        context.spin()
        context.activePlayerStartStepping()
        context.startBattle()
        context.spin(SPINNER_BLADES_SEED)
        context.goInBattleState()
        assertTrue(action.isAvailable)
        context.goToBattleSpinner()
        context.spin(SPINNER_AIM_SEED)
        context.goInBattleState()
        assertTrue(action.isAvailable)
    }

    @Test
    fun `player can not skip attack if spinner value is ESCAPE or FANGS`() {
        context.spin()
        context.activePlayerStartStepping()
        context.startBattle()
        context.spin(SPINNER_FANGS_SEED)
        context.goInBattleState()
        assertTrue(action.isAvailable)
        context.goToBattleSpinner()
        context.spin(SPINNER_ESCAPE_SEED)
        context.goInBattleState()
        assertTrue(action.isAvailable)
    }
}
