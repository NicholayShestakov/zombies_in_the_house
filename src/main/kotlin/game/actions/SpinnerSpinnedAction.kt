package game.actions

import game.SpinnerValue
import game.gameControl.GameContext
import game.gameControl.GameState

class SpinnerSpinnedAction(
    private val context: GameContext,
) : Action(
        listOf("actions.spinner_spinned.name"),
        listOf("actions.spinner_spinned.description"),
        context,
    ) {
    override val isAvailable: Boolean
        get() {
            return context.gameState == GameState.STEP_SPINNER_SPINNED ||
                context.gameState == GameState.ESCAPE_SPINNER_SPINNED ||
                context.gameState == GameState.BATTLE_SPINNER_SPINNED
        }

    override fun execute(): Boolean {
        if (isAvailable) {
            when (context.gameState) {
                GameState.STEP_SPINNER_SPINNED -> {
                    context.activePlayerStartStepping()
                }

                GameState.ESCAPE_SPINNER_SPINNED -> {
                    context.activePlayerStartStepping()
                }

                else -> {
                    when (context.spinnerValue) {
                        SpinnerValue.ESCAPE -> {
                            context.endBattle()
                        }
                        SpinnerValue.FANGS -> {
                            context.activePlayerAddHealth(-1)
                            context.goToBattleSpinner()
                        }
                        else -> {
                            context.goInBattleState()
                        }
                    }
                }
            }
            return true
        }
        return false
    }
}
