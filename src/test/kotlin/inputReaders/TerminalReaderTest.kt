package inputReaders

import org.example.inputReaders.TerminalReader
import org.example.model.Order
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test

class TerminalReaderTest {
    private lateinit var terminalReader: TerminalReader
    private lateinit var input: ByteArrayInputStream
    private lateinit var output: ByteArrayOutputStream

    @BeforeEach
    fun setUp() {
        terminalReader = TerminalReader
        output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
    }


    @Test
    fun `test nextOrder() with complex input`() {
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
        input = ByteArrayInputStream(commands.joinToString("\n").toByteArray())
        System.setIn(input)

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

        expectedCommands.forEach { expectedCommand ->
            val actualCommand = terminalReader.nextOrder()
            assertEquals(expectedCommand, actualCommand)
        }

    }

    @Test
    fun `test nextOrder() with parse error`() {
        input = ByteArrayInputStream("a abc\nstop\n".toByteArray())
        System.setIn(input)
        val expectedError = "\"abc\" is invalid weight"
        assertEquals(Order.Stop, terminalReader.nextOrder())
        assertEquals(expectedError, output.toString().trim())
    }

    @Test
    fun `test nextOrder() with no "stop" instruction`() {
        input = ByteArrayInputStream("a abc\n".toByteArray())
        System.setIn(input)
        assertThrows(Exception::class.java) { // ReadAfterEOFException
            terminalReader.nextOrder()
        }

    }


}