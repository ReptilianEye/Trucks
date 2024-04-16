package org.example

import org.example.model.*

class Simulation(private val orderReader: OrderReader) : ISimulation {

    private val queuesController = TruckQueueController()

    override fun run() {
        when (val order = orderReader.nextOrder()) {
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
        val state = queuesController.state()
        println(state) //TODO nice print for it
        return state
    }

    override fun step() {
        queuesController.step()
    }

    override fun waitingTime(truckId: TruckID) {
        TODO("print waiting time")

    }

}