package org.example.model

import java.util.*

class TruckQueueController() {
    private val maxSize = 5 //max queue size
    private val trucks = mutableSetOf<Truck>()

    private val upperQueue = TruckQueue(maxSize)
    private val bottomQueue = TruckQueue(maxSize)
    private val pending: Queue<Truck> = LinkedList()


    fun step() {
        upperQueue.step().let { if (it != null) trucks.remove(it) }
        bottomQueue.step().let { if (it != null) trucks.remove(it) }

        if (upperQueue.checkingStationFree()) {
            fillOneOfQueues()
            handleEmptyStation(upperQueue)
        }
        if (bottomQueue.checkingStationFree()) {
            fillOneOfQueues()
            handleEmptyStation(bottomQueue)
        }
    }


    private fun handleEmptyStation(queue: TruckQueue) {
        val other = queue.other()
        val minTruck = minFromQueuesAndPending() ?: return

        queue.moveAllExcept(other, minTruck)
        queue.setCurrentChecking(minTruck)

        if (pending.peek() == minTruck) pending.poll()
        else {
            queue.remove(minTruck)
            queue.squeeze()
        }
    }


    fun state() = QueuesState(pending, listOf(upperQueue.getQueue(), bottomQueue.getQueue()))

    fun waitingTime(truckId: TruckID) {
        if (pending.firstOrNull { it.id == truckId } != null) {
            println("Truck is in pending. Cannot calculate waiting time")
            return
        }
        val allQues = listOf(upperQueue, bottomQueue)
        if (allQues.any { it.currentlyChecking?.id == truckId }) {
            println("Truck is currently checking. Waiting time is 0")
            return
        }
        val allTrucks = (allQues.flatMap { it.getQueue() } + pending.peek()).filterNotNull()
            .toCollection(PriorityQueue { t1, t2 -> t1.weight - t2.weight })
        val waitingTimeForStation = allQues.map { it.currentlyChecking?.weight ?: 0 }.toCollection(PriorityQueue())
        while (allTrucks.isNotEmpty()) {
            val truck = allTrucks.poll()
            val currentWaitingTime = waitingTimeForStation.poll()
            if (truck.id == truckId) {
                println("Waiting time for truck $truckId is $currentWaitingTime")
                return
            }
            waitingTimeForStation.add(currentWaitingTime + truck.weight)
        }
        println("Truck with id $truckId not found")

    }

    private fun fillOneOfQueues() {
        val toFill = if (!upperQueue.isEmpty()) upperQueue else bottomQueue
        if (toFill.isFull()) return

        while (!toFill.isFull() && pending.isNotEmpty()) {
            toFill.put(pending.poll())
        }

    }

    fun addToPending(newTruck: Truck) {
        pending.add(newTruck)
        trucks.add(newTruck)
    }

    private fun minFromQueuesAndPending() = listOfNotNull(
        listOf(upperQueue, bottomQueue).fold(null) { acc: Truck?, truckQueue ->
            val queMin = truckQueue.minOrNull()
            if (acc == null) queMin
            else if (queMin == null) acc
            else if (queMin.weight < acc.weight) queMin
            else acc
        }, pending.peek()
    ).minByOrNull { it.weight }

    //TODO probably can be done better
    private fun getUpperQueue() = upperQueue
    private fun getBottomQueue() = bottomQueue

    private fun TruckQueue.other() = if (this == upperQueue) bottomQueue else upperQueue
//    private fun otherQueue(queue: TruckQueue) = if (queue == upperQueue) bottomQueue else upperQueue


}
