package org.example.model

import java.util.*

class TruckQueueController() {
    private val maxSize = 5 //max queue size
    private val numOfQueues = 2 //number of
    private val queues = Array(numOfQueues) { TruckQueue(maxSize) }
    private val trucks = mutableSetOf<Truck>()

    //    private val upperQueue = TruckQueue(maxSize)
//    private val bottomQueue = TruckQueue(maxSize)
    private val pending: Queue<Truck> = LinkedList()


    fun step() {
        queues.forEach { it.step().let { t -> if (t != null) trucks.remove(t) } }
//        upperQueue.step()
//        bottomQueue.step()
        queues.filter { it.checkingStationFree() }.forEach {
            fillAllButOneQueue()
            handleEmptyStation(it)
        }
//        if (upperQueue.checkingStationFree()) {
//            fillOneOfQueues()
//            handleEmptyStation(upperQueue)
//        }
//        if (bottomQueue.checkingStationFree()) {
//            fillOneOfQueues()
//            handleEmptyStation(bottomQueue)
//        }
    }

    private fun handleEmptyStation(queue: TruckQueue) {
        val other = otherQueue(queue)
        val minTruck = minFromQueuesAndPending() ?: return

        if (pending.peek() == minTruck) pending.poll()
        else {
            queue.remove(minTruck)
            queue.squeeze()
        }

        queue.moveAllExcept(other, minTruck)
        queue.setCurrentChecking(minTruck)
    }

    fun state() = QueuesState(pending, getQueues())

    fun waitingTime(truckId: TruckID) {
        if (pending.firstOrNull { it.id == truckId } != null) {
            println("Truck is in pending. Cannot calculate waiting time")
            return
        }
        if (queues.any { it.currentlyChecking?.id == truckId }) {
            println("Truck is currently checking. Waiting time is 0")
            return
        }
        val allTrucks = (queues.flatMap { it.getQueue() } + pending.peek()).filterNotNull()
            .toCollection(PriorityQueue { t1, t2 -> t1.weight - t2.weight })
        val waitingTimeForStation = queues.map { it.currentlyChecking?.weight ?: 0 }.toCollection(PriorityQueue())
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

    fun addToPending(newTruck: Truck) {
        pending.add(newTruck)
        trucks.add(newTruck)
    }

    private fun minFromQueuesAndPending() = listOfNotNull(
        queues.fold(null) { acc: Truck?, truckQueue ->
            val queMin = truckQueue.minOrNull()
            if (acc == null) queMin
            else if (queMin == null) acc
            else if (queMin.weight < acc.weight) queMin
            else acc
        }, pending.peek()
    ).minByOrNull { it.weight }


    private fun fillAllButOneQueue() {
        val toFill = getAllNotFullQueues()
        toFill.forEach {                    //TODO probably can be optimized
            while (!it.isFull() && pending.isNotEmpty()) {
                it.put(pending.poll())
            }
        }
    }

    private fun getAllNotFullQueues(): List<TruckQueue> {
        val leaveEmpty = getFirstEmpty()
        return queues.filter { it != leaveEmpty && !it.isFull() }
    }

    private fun getFirstEmpty() = queues.first { it.isEmpty() }

    private fun getQueues() = queues.map { it.getQueue() } //TODO probably can be done better

    private fun otherQueue(queue: TruckQueue) = queues.first { it != queue }


}
