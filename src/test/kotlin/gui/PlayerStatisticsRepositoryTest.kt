package gui

import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlayerStatisticsRepositoryTest {
    @Test
    fun `loadStatistics returns empty list when file does not exist`() {
        val statisticsFile = Files.createTempDirectory("zombies-statistics-empty").resolve("statistics.properties")
        val repository = PlayerStatisticsRepository(statisticsFile)

        assertEquals(emptyList(), repository.loadStatistics())
    }

    @Test
    fun `recordFinalGame stores only win winners and sorts statistics by wins then name`() {
        val statisticsFile = Files.createTempDirectory("zombies-statistics-wins").resolve("statistics.properties")
        val repository = PlayerStatisticsRepository(statisticsFile)

        repository.recordFinalGame(FinalGameUiModel(isWin = true, winnerNames = listOf("Bob", "Alice")))
        repository.recordFinalGame(FinalGameUiModel(isWin = true, winnerNames = listOf("Bob")))
        repository.recordFinalGame(FinalGameUiModel(isWin = false, winnerNames = listOf("Alice", "Carol")))
        val reloadedRepository = PlayerStatisticsRepository(statisticsFile)

        assertEquals(
            listOf(
                PlayerStatisticsUiModel(name = "Bob", wins = 2),
                PlayerStatisticsUiModel(name = "Alice", wins = 1),
            ),
            reloadedRepository.loadStatistics(),
        )
    }

    @Test
    fun `recordFinalGame preserves player names with spaces and non latin characters`() {
        val statisticsFile = Files.createTempDirectory("zombies-statistics-names").resolve("statistics.properties")
        val repository = PlayerStatisticsRepository(statisticsFile)

        repository.recordFinalGame(FinalGameUiModel(isWin = true, winnerNames = listOf("Анна Мария")))

        assertEquals(
            listOf(PlayerStatisticsUiModel(name = "Анна Мария", wins = 1)),
            repository.loadStatistics(),
        )
        assertTrue(Files.exists(statisticsFile))
    }

    @Test
    fun `recordFinalGame does not create file for lose without winners`() {
        val statisticsFile = Files.createTempDirectory("zombies-statistics-lose").resolve("statistics.properties")
        val repository = PlayerStatisticsRepository(statisticsFile)

        repository.recordFinalGame(FinalGameUiModel(isWin = false, winnerNames = listOf("Alice")))

        assertEquals(emptyList(), repository.loadStatistics())
        assertFalse(Files.exists(statisticsFile))
    }

    @Test
    fun `loadStatistics sorts equal wins by player name`() {
        val statisticsFile = Files.createTempDirectory("zombies-statistics-sort").resolve("statistics.properties")
        val repository = PlayerStatisticsRepository(statisticsFile)

        repository.recordFinalGame(FinalGameUiModel(isWin = true, winnerNames = listOf("Charlie", "Alice", "Bob")))

        assertEquals(
            listOf(
                PlayerStatisticsUiModel(name = "Alice", wins = 1),
                PlayerStatisticsUiModel(name = "Bob", wins = 1),
                PlayerStatisticsUiModel(name = "Charlie", wins = 1),
            ),
            repository.loadStatistics(),
        )
    }
}
