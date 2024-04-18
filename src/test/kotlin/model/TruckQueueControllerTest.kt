package model

import org.example.model.Truck
import org.example.model.TruckQueueController
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TruckQueueControllerTest {
    private lateinit var controller: TruckQueueController

    @BeforeEach
    fun setUp() {
        controller = TruckQueueController()
    }

    @Test
    fun step() {
        val truck1 = Truck(1)
        val truck2 = Truck(2)
        val truck3 = Truck(3)
        val truck4 = Truck(4)
        val truck5 = Truck(5)

        controller.addToPending(truck5)
        controller.addToPending(truck2)
        controller.addToPending(truck3)
        controller.addToPending(truck1)
        controller.addToPending(truck4)

        controller.step()

        val state = controller.getState()

        assertEquals(0, state.pending.size)
        assertEquals(listOf(truck1, truck5, truck3, truck4, null), state.queues[0]) //upper queue
        assertEquals(listOf(truck2, null, null, null, null), state.queues[1])
    }

    @Test
    fun waitingTime() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val truck1 = Truck(1)
        val truck2 = Truck(2)
        val truck3 = Truck(3)
        val truck4 = Truck(4)
        val truck5 = Truck(5)

        controller.addToPending(truck5)
        controller.addToPending(truck2)
        controller.addToPending(truck3)
        controller.addToPending(truck1)
        controller.addToPending(truck4)

        controller.step()

        val expectedOutput =
            listOf(
                "Truck with id=${truck1.id} is currently being checked. Time left: 1",
                "Truck with id=${truck2.id} is currently being checked. Time left: 2",
                "Approximate time for truck with id=${truck3.id} to start check-in: 1",
                "Approximate time for truck with id=${truck4.id} to start check-in: 2",
                "Approximate time for truck with id=${truck5.id} to start check-in: 4"
            )

        controller.waitingTime(truck1.id)
        controller.waitingTime(truck2.id)
        controller.waitingTime(truck3.id)
        controller.waitingTime(truck4.id)
        controller.waitingTime(truck5.id)

        output.toString().split("\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }.forEachIndexed { index, line ->
                assertEquals(expectedOutput[index], line)
            }
    }

    @Test
    fun addToPending() {
        val truck1 = Truck(1)
        val truck2 = Truck(2)
        val truck3 = Truck(3)
        val truck4 = Truck(4)
        val truck5 = Truck(5)

        controller.addToPending(truck1)
        controller.addToPending(truck2)
        controller.addToPending(truck3)
        controller.addToPending(truck4)
        controller.addToPending(truck5)

        val state = controller.getState()
        assertEquals(5, state.pending.size)
        assertEquals(listOf(truck1, truck2, truck3, truck4, truck5), state.pending)

    }

}