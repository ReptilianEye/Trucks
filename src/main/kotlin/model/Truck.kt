package org.example.model

typealias TruckID = Long

data class Truck private constructor(val id: Long, val weight: Int) : Comparable<Truck> {

    constructor(weight: Int) : this(next(), weight)

    companion object {
        private var ID: TruckID = 0
        private fun next() = ID++
    }

    override fun compareTo(other: Truck): Int {
        return this.weight.compareTo(other.weight)
    }
}
