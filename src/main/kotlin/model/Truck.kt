package org.example.model

typealias TruckID = Long

data class Truck private constructor(val id: Long, val weight: Int) : Comparable<Truck> {

    //TODO probably change to UUID
    companion object {
        private var ID: TruckID = 0L
        private fun next() = ID++

        operator fun invoke(weight: Int) = Truck(next(), weight)
    }

    override fun compareTo(other: Truck): Int {
        return this.weight.compareTo(other.weight)
    }
}

