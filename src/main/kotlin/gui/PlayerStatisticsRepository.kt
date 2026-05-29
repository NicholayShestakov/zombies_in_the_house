package gui

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.Base64
import java.util.Properties
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

private const val STATISTICS_DIRECTORY_NAME = ".zombies_in_the_house"
private const val STATISTICS_FILE_NAME = "statistics.properties"
private const val PROPERTY_NAME_PREFIX = "name."
private const val PROPERTY_WINS_PREFIX = "wins."
private const val DEFAULT_COUNT = 0

class PlayerStatisticsRepository(
    private val statisticsFile: Path = defaultStatisticsFile(),
) {
    fun loadStatistics(): List<PlayerStatisticsUiModel> {
        val properties = loadProperties()

        return properties.stringPropertyNames()
            .filter { it.startsWith(PROPERTY_NAME_PREFIX) }
            .map { propertyName ->
                val id = propertyName.removePrefix(PROPERTY_NAME_PREFIX)
                PlayerStatisticsUiModel(
                    name = properties.getProperty(propertyName),
                    wins = properties.getCount(PROPERTY_WINS_PREFIX + id),
                )
            }
            .sortedWith(compareByDescending<PlayerStatisticsUiModel> { it.wins }.thenBy { it.name })
    }

    fun recordFinalGame(finalGame: FinalGameUiModel) {
        if (!finalGame.isWin) {
            return
        }

        val properties = loadProperties()
        finalGame.winnerNames.forEach { playerName ->
            properties.incrementPlayerWins(playerName)
        }
        saveProperties(properties)
    }

    private fun Properties.incrementPlayerWins(playerName: String) {
        val id = playerName.toStatisticsId()
        setProperty(PROPERTY_NAME_PREFIX + id, playerName)
        setProperty(PROPERTY_WINS_PREFIX + id, (getCount(PROPERTY_WINS_PREFIX + id) + 1).toString())
    }

    private fun loadProperties(): Properties {
        val properties = Properties()
        if (statisticsFile.exists()) {
            Files.newInputStream(statisticsFile).use(properties::load)
        }
        return properties
    }

    private fun saveProperties(properties: Properties) {
        statisticsFile.parent.createDirectories()
        Files.newOutputStream(statisticsFile).use { output ->
            properties.store(output, null)
        }
    }

    private fun Properties.getCount(propertyName: String): Int {
        return getProperty(propertyName)?.toIntOrNull() ?: DEFAULT_COUNT
    }

    private fun String.toStatisticsId(): String {
        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(toByteArray(StandardCharsets.UTF_8))
    }
}

private fun defaultStatisticsFile(): Path {
    return Path.of(System.getProperty("user.home"), STATISTICS_DIRECTORY_NAME, STATISTICS_FILE_NAME)
}
