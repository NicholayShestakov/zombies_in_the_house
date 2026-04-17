import kotlin.random.Random

object Spinner {

    fun getValue(): SpinnerValue {
        return when (Random.nextInt(0, 10)) {
            in 0..2 -> SpinnerValue.ESCAPE
            in 3..5 -> SpinnerValue.FANGS
            in 6..7 -> SpinnerValue.BLADES
            else -> SpinnerValue.AIM
        }
    }
}