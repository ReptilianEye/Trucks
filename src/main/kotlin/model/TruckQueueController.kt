package org.example.model

import java.util.*

class TruckQueueController {
    private val maxSize = 5 //max single queue size
    private val trucks = mutableSetOf<TruckID>()

    private val upperQueue = TruckQueue(maxSize)
    private val bottomQueue = TruckQueue(maxSize)
    private val pending: Queue<Truck> = LinkedList()


    fun step() {
        upperQueue.step()
        bottomQueue.step()

        upperQueue.checkIfFinishedChecking()?.let { trucks.remove(it.id) }
        bottomQueue.checkIfFinishedChecking()?.let { trucks.remove(it.id) }

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

        minFromQueuesAndPending()?.let { minTruck ->
            queue.moveToOtherAllExcept(other, minTruck)

            if (pending.peek() == minTruck) pending.poll()
            else queue.remove(minTruck)

            queue.setCurrentChecking(minTruck)
        }
    }


    fun waitingTime(truckId: TruckID) = when {
        !trucks.contains(truckId) -> println("Truck with id=$truckId does not exist")

        pending.any { it.id == truckId } -> println("Truck with id=$truckId is in pending. Cannot calculate waiting time")

        else -> {
            val allQues = listOf(upperQueue, bottomQueue)
            allQues.firstOrNull { it.currentlyChecking?.id == truckId }?.let {
                println("Truck with id=$truckId is currently being checked. Time left: ${it.timeToFinishChecking}")
            } ?: checkTimeForTruckInQue(allQues, truckId)
        }
    }

    private fun checkTimeForTruckInQue(
        allQues: List<TruckQueue>,
        truckId: TruckID
    ) {
        val allTrucks = (allQues.flatMap { it.getNotNullQueue() } + pending.peek()).filterNotNull()
            .toCollection(PriorityQueue())
        val waitingTimeForStation = allQues.map { it.timeToFinishChecking }.toCollection(PriorityQueue())
        while (allTrucks.isNotEmpty()) {
            val truck = allTrucks.poll()
            val currentWaitingTime = waitingTimeForStation.poll()
            if (truck.id == truckId) {
                println("Approximate time for truck with id=$truckId to start check-in: $currentWaitingTime")
                return
            }
            waitingTimeForStation.add(currentWaitingTime + truck.weight)
        }
        throw IllegalStateException("Truck with id $truckId not found")
    }

    //fills queue that is not empty from pending
    private fun fillOneOfQueues() {
        val toFill = if (!upperQueue.isEmpty()) upperQueue else bottomQueue
        if (toFill.isFull()) return

        while (!toFill.isFull() && pending.isNotEmpty()) {
            toFill.put(pending.poll())
        }

    }

    fun addToPending(newTruck: Truck) {
        pending.add(newTruck)
        trucks.add(newTruck.id)
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

    fun getState() = QueuesState(pending, listOf(upperQueue.getQueueToPrint(), bottomQueue.getQueueToPrint()))

    private fun TruckQueue.other() = if (this == upperQueue) bottomQueue else upperQueue


}
