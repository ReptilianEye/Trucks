package org.example.model

import kotlin.math.max

/**
 * TruckQueue is a queue of trucks that are waiting to be checked.
 * It has a maxSize, which is the size of the queue + 1.
 *
 * @property maxSize the actual size of the queue + 1 (one truck is being checked)
 */
class TruckQueue(private val maxSize: Int) {
    private val queue: Array<Truck?> = arrayOfNulls(maxSize - 1)
    var currentlyChecking: Truck? = null
    var timeToFinishChecking: Int = 0
        private set


    fun put(value: Truck) {
        queue[queue.indexOfFirst { it == null }] = value
    }

    fun squeeze() {
        val squeezed = queue.filterNotNull()
        queue.indices.forEach { i ->
            queue[i] = squeezed.getOrNull(i)
        }
    }

    fun moveToOtherAllExcept(other: TruckQueue, exceptTruck: Truck) {
        // move all trucks to second queue except the one that is currently checking
        queue.forEachIndexed { i, v ->
            if (v != null && v != exceptTruck) swap(other, i)
        }
        // if the truck was in the second queue, we need to bring it to the first queue
        other.queue.indexOfFirst {
            it == exceptTruck
        }.let {
            if (it != -1) swap(other, it)
        }
        other.squeeze()
        this.squeeze()
    }

    fun swap(other: TruckQueue, index: Int) {
        this[index] = other[index].also { other[index] = this[index] }
    }

    fun remove(truck: Truck) {
        queue[queue.indexOf(truck)] = null
        squeeze()
    }

    fun isEmpty() = queue.all { it == null }
    fun isFull() = queue.all { it != null }

    fun minOrNull() = queue.filterNotNull().minByOrNull { it.weight }

    fun setCurrentChecking(truck: Truck) {
        currentlyChecking = truck
        timeToFinishChecking = truck.weight
    }

    fun checkingStationFree() = timeToFinishChecking == 0

    fun checkIfFinishedChecking() =
        if (timeToFinishChecking == 0) currentlyChecking?.also { currentlyChecking = null } else null

    fun step() {
        timeToFinishChecking = max(0, timeToFinishChecking - 1)
    }


    operator fun get(i: Int) = queue[i]
    operator fun set(i: Int, value: Truck?) {
        queue[i] = value
    }

    fun getNotNullQueue() = queue.toList().filterNotNull()
    fun getQueueToPrint() = listOf(currentlyChecking) + queue
}