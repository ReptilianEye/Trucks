package model

import org.example.model.Truck
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TruckTest {
    @Test
    fun `test truck comparison`() {
        val truck1 = Truck(500)
        val truck2 = Truck(1000)
        val truck3 = Truck(750)
        val truck4 = Truck(200)

        assertEquals(
            listOf(truck4, truck1, truck3, truck2),
            listOf(truck1, truck2, truck3, truck4).sorted()
        )
    }
}