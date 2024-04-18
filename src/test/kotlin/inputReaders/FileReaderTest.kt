package inputReaders

import org.example.inputReaders.FileReader
import org.example.model.Order
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileReaderTest {
    @Test
    fun `test nextOrder() with complex input`() {
        // arrange
        val fileName = "complex.txt"
        val file = File(fileName)
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
        file.delete()

    }

    @Test
    fun `test nextOrder() with empty file`() {
        val fileName = "empty.txt"
        val file = File(fileName)

        file.writeText("")

        val fileReader = FileReader(fileName)

        assertEquals(Order.Stop, fileReader.nextOrder())

        file.delete()
    }

    @Test
    fun `test nextOrder() with invalid file`() {
        val fileName = "nonexistent_file.txt"

        assertThrows(IllegalArgumentException::class.java) {
            FileReader(fileName)
        }
    }
}