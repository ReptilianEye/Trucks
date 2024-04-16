package org.example.model

sealed class Order {
    data class Arrived(val truckWeight: Int) : Order()
    data object Status : Order()
    data object Step : Order()
    data class WaitingTime(val truckId: TruckID) : Order()
    data object Stop : Order()
}
