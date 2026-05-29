package actions

interface Action {

    val isAvailable: Boolean
    fun action(): Boolean
}