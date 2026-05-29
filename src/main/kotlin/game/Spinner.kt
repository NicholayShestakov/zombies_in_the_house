package game

import kotlin.random.Random

object Spinner {
    fun getValue(seed: Int? = null): SpinnerValue {
        val random = seed?.let { Random(seed) } ?: Random
        return when (random.nextInt(0, 10)) {
            in 0..2 -> SpinnerValue.ESCAPE
            in 3..5 -> SpinnerValue.FANGS
            in 6..7 -> SpinnerValue.BLADES
            else -> SpinnerValue.AIM
        }
    }
}
