package inputReaders

import org.example.inputReaders.FileReader
import org.example.model.Order
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import java.io.File
import kotlin.test.Test

class FileReaderTest {
    private val fileName = "test_input.txt"
    private lateinit var file: File

    @BeforeEach
    fun setUp() {
        file = File(fileName)
    }

    @AfterEach
    fun tearDown() {
        file.delete()
    }

    @Test
    fun `test nextOrder() with complex input`() {
        // arrange
        val commands = listOf(
            "a 10",
            "a 5",
            "a -1",
            "step",
            "st",
            "w 10",
            "status",
            "step",
            "w 10",
            "a 10",
            "stop"
        )
        val fileContent = commands.joinToString("\n")
        file.writeText(fileContent)

        val fileReader = FileReader(fileName)

        // act
        val expectedCommands = listOf(
            Order.Arrived(10),
            Order.Arrived(5),
            Order.Step,
            Order.WaitingTime(10),
            Order.Status,
            Order.Step,
            Order.WaitingTime(10),
            Order.Arrived(10),
            Order.Stop
        )

        // assert
        expectedCommands.forEach { expectedCommand ->
            val actualCommand = fileReader.nextOrder()
            assertEquals(expectedCommand, actualCommand)
        }

    }

    @Test
    fun `test nextOrder() with empty file`() {
        file.writeText("")

        val fileReader = FileReader(fileName)

        assertEquals(Order.Stop, fileReader.nextOrder())

    }

    @Test
    fun `test nextOrder() with invalid file`() {
        val fileName = "nonexistent_file.txt"

        assertThrows(IllegalArgumentException::class.java) {
            FileReader(fileName)
        }
    }
}