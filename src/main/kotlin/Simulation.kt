package org.example

import org.example.inputReaders.InputReader
import org.example.model.*

class Simulation(private val inputReader: InputReader) : ISimulation {

    private val queuesController = TruckQueueController()

    override tailrec fun run() {
        when (val order = inputReader.nextOrder()) {
            is Order.Arrived -> {
                arrive(order.truckWeight)
                println("Successfully added truck with weight ${order.truckWeight}")
            }

            Order.Status -> status()
            Order.Step -> {
                step()
                println("Step taken")
            }

            is Order.WaitingTime -> waitingTime(order.truckId)
            Order.Stop -> {
                println("Stopping...")
                return
            }
        }
        run()
    }

    override fun arrive(weight: Int) {
        val newTruck = Truck(weight)
        queuesController.addToPending(newTruck)
    }

    override fun status(): QueuesState {
        val state = queuesController.getState()
        printState(state)
        return state
    }

    override fun step() {
        queuesController.step()
    }

    override fun waitingTime(truckId: TruckID) = queuesController.waitingTime(truckId)

    private fun printState(state: QueuesState) {
        val emptySlotSign = "_"

        state.queues.forEachIndexed { index, queue ->
            println("Queue $index: -> ${queue.reversed().map { it?.toString() ?: emptySlotSign }} ->")
        }

        val pending = state.pending.reversed().map { it?.toString() ?: emptySlotSign }
        println("Pending: -> $pending ->")
    }
}

