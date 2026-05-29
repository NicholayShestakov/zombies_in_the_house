package game.gameControl.contextManagers

import game.Spinner
import game.SpinnerValue

class SpinnerManager {
    var spinnerValue = SpinnerValue.ESCAPE
        private set

    fun spin(seed: Int? = null) {
        spinnerValue = Spinner.getValue(seed)
    }

    fun resetForBattle() {
        spinnerValue = SpinnerValue.FANGS // Чтобы при ESCAPE с прошлого раза не случался моментальный побег
    }
}
