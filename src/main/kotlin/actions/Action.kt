package actions

import Context

interface Action {

    val automatic: Boolean
    val isAvailable: Boolean
}