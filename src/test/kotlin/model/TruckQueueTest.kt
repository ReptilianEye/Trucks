package model

import org.example.model.Truck
import org.example.model.TruckQueue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test


class TruckQueueTests {
    private val queueSize: Int = 5
    private lateinit var queue: TruckQueue

    @BeforeEach
    fun setUp() {
        queue = TruckQueue(queueSize)
    }

    @Test
    fun testPut() {
        val truck1 = Truck(10)
        val truck2 = Truck(15)
        val truck3 = Truck(20)
        val truck4 = Truck(25)
        val truck5 = Truck(30)

        queue.put(truck1)
        queue.put(truck2)
        queue.put(truck3)
        queue.put(truck4)

        assertEquals(truck1, queue[0])
        assertEquals(truck2, queue[1])
        assertEquals(truck3, queue[2])
        assertEquals(truck4, queue[3])

        assertThrows(IndexOutOfBoundsException::class.java) {
            queue.put(truck5)
        }

    }

    @Test
    fun testSqueeze() {
        val truck1 = Truck(10)
        val truck2 = Truck(15)
        val truck3 = Truck(7)
        queue.put(truck1)
        queue.put(truck2)
        queue.put(truck3)
        queue[1] = null

        queue.squeeze()

        assertEquals(truck1, queue[0])
        assertEquals(truck3, queue[1])
        assertNull(queue[2])
        assertNull(queue[3])
    }

    @Test
    fun testMoveAllExcept() {
        val otherQueue = TruckQueue(5)
        val truck1 = Truck(10)
        val truck2 = Truck(15)
        val truck3 = Truck(20)
        queue.put(truck1)
        queue.put(truck2)
        queue.put(truck3)
        queue.moveToOtherAllExcept(otherQueue, truck2)

        assertEquals(truck1, otherQueue[0])
        assertEquals(truck3, otherQueue[1])
        assertNull(otherQueue[2])
        assertEquals(truck2, queue[0])
        assertNull(queue[1])
        assertNull(queue[2])
    }

    @Test
    fun testSwap() {
        val otherQueue = TruckQueue(5)
        val truck1 = Truck(10)
        val truck2 = Truck(15)
        queue.put(truck1)
        otherQueue.put(truck2)
        queue.swap(otherQueue, 0)

        assertEquals(truck2, queue[0])
        assertEquals(truck1, otherQueue[0])
    }

    @Test
    fun testRemove() {
        val queue = TruckQueue(5)
        val truck1 = Truck(10)
        val truck2 = Truck(15)
        queue.put(truck1)
        queue.put(truck2)
        queue.remove(truck1)
        assertEquals(truck2, queue[0])
        assertNull(queue[1])
    }

    @Test
    fun testIsEmpty() {
        assertTrue(queue.isEmpty())
        val truck = Truck(10)
        queue.put(truck)
        assertFalse(queue.isEmpty())
        queue.remove(truck)
        assertTrue(queue.isEmpty())
    }

    @Test
    fun testIsFull() {
        val queue = TruckQueue(3)
        assertFalse(queue.isFull())
        val truck1 = Truck(10)
        val truck2 = Truck(15)
        queue.put(truck1)
        queue.put(truck2)
        assertTrue(queue.isFull())
        queue.remove(truck1)
        assertFalse(queue.isFull())
    }

    @Test
    fun testMinOrNull() {
        val truck1 = Truck(10)
        val truck2 = Truck(15)
        val truck3 = Truck(5)
        assertNull(queue.minOrNull())
        queue.put(truck1)
        queue.put(truck2)
        queue.put(truck3)
        assertEquals(truck3, queue.minOrNull())
    }

    @Test
    fun testSetCurrentChecking() {
        val truck = Truck(10)
        assertNull(queue.currentlyChecking)
        assertEquals(0, queue.timeToFinishChecking)

        queue.setCurrentChecking(truck)

        assertEquals(truck, queue.currentlyChecking)
        assertEquals(truck.weight, queue.timeToFinishChecking)
    }

    @Test
    fun testCheckingStationFree() {
        assertTrue(queue.checkingStationFree())
        val truck = Truck(10)
        queue.setCurrentChecking(truck)
        assertFalse(queue.checkingStationFree())
    }

    @Test
    fun testCheckIfFinishedChecking() {
        val queue = TruckQueue(5)
        assertNull(queue.checkIfFinishedChecking())
        val truck = Truck(1)
        queue.setCurrentChecking(truck)
        queue.step()
        assertEquals(truck, queue.checkIfFinishedChecking())
        assertNull(queue.currentlyChecking)
        assertEquals(0, queue.timeToFinishChecking)
    }

    @Test
    fun testStep() {
        val truck = Truck(3)
        queue.setCurrentChecking(truck)
        queue.step()
        assertEquals(truck.weight - 1, queue.timeToFinishChecking)
        queue.step()
        assertEquals(truck.weight - 2, queue.timeToFinishChecking)
        queue.step()
        queue.step()
        queue.step()
        queue.step()
        queue.step()
        queue.checkIfFinishedChecking()
        assertEquals(0, queue.timeToFinishChecking)
        assertNull(queue.currentlyChecking)
    }

    @Test
    fun testGetNotNullQueue() {
        val truck1 = Truck(10)
        val truck2 = Truck(15)
        queue.put(truck1)
        queue.put(truck2)
        val notNullQueue = queue.getNotNullQueue()
        assertEquals(2, notNullQueue.size)
        assertTrue(notNullQueue.contains(truck1))
        assertTrue(notNullQueue.contains(truck2))
    }

    @Test
    fun testGetQueueToPrint() {
        val truck1 = Truck(10)
        val truck2 = Truck(15)
        val truck3 = Truck(20)
        queue.put(truck1)
        queue.put(truck2)
        queue.setCurrentChecking(truck3)
        val queueToPrint = queue.getQueueToPrint()
        assertEquals(queueSize, queueToPrint.size)
        assertEquals(queue.currentlyChecking, queueToPrint[0])
        assertEquals(truck1, queueToPrint[1])
        assertEquals(truck2, queueToPrint[2])
    }
}