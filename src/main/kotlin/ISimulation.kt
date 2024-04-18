package org.example

import org.example.model.QueuesState
import org.example.model.TruckID

interface ISimulation {
    fun run()

    fun arrive(weight: Int)
    fun status(): QueuesState
    fun step()
    fun waitingTime(truckId: TruckID)
}