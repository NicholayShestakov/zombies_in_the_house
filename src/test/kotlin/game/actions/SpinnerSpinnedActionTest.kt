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
import game.gameControl.GameState
import game.testBoard
import game.testHuman
import game.testMonster
import game.testPlayer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SpinnerSpinnedActionTest {
    private lateinit var entities: List<Entity>
    private lateinit var players: List<Player>
    private lateinit var board: Board
    private lateinit var context: GameContext
    private lateinit var action: SpinnerSpinnedAction

    @BeforeEach
    fun initialization() {
        entities =
            listOf(
                testHuman(plusSpeed = -1),
                testMonster(),
            )
        players =
            listOf(
                testPlayer("Alice", entities[0]),
                testPlayer("Bob", entities[1]),
            )
        board = testBoard(cards = mapOf(START to entities))
        context = GameContext(board, players, "ru")
        action = SpinnerSpinnedAction(context)
    }

    @Test
    fun `spinner spinned action available in SPINNED states`() {
        context.spin()
        assertEquals(GameState.STEP_SPINNER_SPINNED, context.gameState)
        assertTrue(action.isAvailable)

        context.activePlayerStartStepping()
        context.startBattle()
        context.spin(SPINNER_ESCAPE_SEED)
        assertEquals(GameState.BATTLE_SPINNER_SPINNED, context.gameState)
        assertTrue(action.isAvailable)

        context.endBattle()
        context.spin()
        assertEquals(GameState.ESCAPE_SPINNER_SPINNED, context.gameState)
        assertTrue(action.isAvailable)
    }

    @Test
    fun `spinner spinned action correctly changes game state`() {
        context.spin(SPINNER_AIM_SEED)
        action.execute()
        assertEquals(GameState.STEPPING, context.gameState)

        context.startBattle()
        context.spin(SPINNER_FANGS_SEED)
        action.execute()
        assertEquals(GameState.BATTLE_SPINNER_SPINNING, context.gameState)

        context.spin(SPINNER_BLADES_SEED)
        action.execute()
        assertEquals(GameState.BATTLE, context.gameState)

        context.goToBattleSpinner()
        context.spin(SPINNER_ESCAPE_SEED)
        action.execute()
        assertEquals(GameState.ESCAPE_SPINNER_SPINNING, context.gameState)

        context.spin(SPINNER_ESCAPE_SEED)
        action.execute()
        assertEquals(GameState.TURN_FINISH, context.gameState)
    }
}
