package game.actions

import game.gameControl.GameContext
import java.util.MissingResourceException
import java.util.ResourceBundle

private const val LANGUAGE_BUNDLE_PREFIX = "language_"

abstract class Action(
    val nameKey: List<String>,
    val descriptionKey: List<String>,
    private val context: GameContext,
) {
    abstract val isAvailable: Boolean

    abstract fun execute(): Boolean

    @Suppress("SwallowedException")
    open fun getName(): String {
        return try {
            nameKey.joinToString(
                separator = " ",
                transform = { ResourceBundle.getBundle(LANGUAGE_BUNDLE_PREFIX + context.localization).getString(it) },
            )
        } catch (e: MissingResourceException) {
            nameKey.joinToString(separator = " ")
        }
    }

    @Suppress("SwallowedException")
    open fun getDescription(): String {
        return try {
            descriptionKey.joinToString(
                separator = " ",
                transform = { ResourceBundle.getBundle(LANGUAGE_BUNDLE_PREFIX + context.localization).getString(it) },
            )
        } catch (e: MissingResourceException) {
            descriptionKey.joinToString(separator = " ")
        }
    }
}
