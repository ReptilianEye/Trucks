package org.example.model

import kotlin.math.max

class TruckQueue(maxSize: Int = 5) {
    private val queue: Array<Truck?> = arrayOfNulls(maxSize - 1)
    var currentlyChecking: Truck? = null
    private var timeToFinishChecking: Int = 0

    fun put(value: Truck?) {
        squeeze()
        queue[queue.indexOfFirst { it == null }] = value
    }

    fun squeeze() {
        var curr = 0
        for (i in queue.indices) {
            if (queue[i] != null) {
                queue[curr++] = queue[i]
                queue[i] = null
            }
        }
    }

    fun moveAllExcept(other: TruckQueue, exceptTruck: Truck) {
        queue.forEachIndexed { i, _ ->
            if (queue[i] != exceptTruck) swap(other, i)
        }
    }

    fun swap(other: TruckQueue, index: Int) {
        this[index] = other[index].also { other[index] = this[index] }
    }

    fun remove(truck: Truck) {
        queue[queue.indexOf(truck)] = null
    }

    fun isEmpty() = queue.all { it == null }
    fun isFull() = queue.all { it != null }

    fun minOrNull() = queue.filterNotNull().minByOrNull { it.weight }

    fun setCurrentChecking(truck: Truck) {
        currentlyChecking = truck
        this.timeToFinishChecking = truck.weight
    }

    fun checkingStationFree() = timeToFinishChecking == 0

    fun step(): Truck? {
        timeToFinishChecking = max(0, timeToFinishChecking - 1)
        if (timeToFinishChecking == 0) {
//            currentlyChecking = null
            return currentlyChecking.also { currentlyChecking = null }
        }
        return null
    }

    operator fun get(i: Int) = queue[i]
    operator fun set(i: Int, value: Truck?) {
        queue[i] = value
    }

    fun getQueue() = listOf(currentlyChecking) + queue
}