package org.example.model

interface ISimulation {
    fun run()

    fun arrive(weight: Int)
    fun status(): QueuesState
    fun step()
    fun waitingTime(truckId: TruckID)

}