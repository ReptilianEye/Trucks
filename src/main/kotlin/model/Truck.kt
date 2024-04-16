package org.example.model

typealias TruckID = Long

data class Truck(val weight: Int) {
    val id = next()

    override fun equals(other: Any?) = other is Truck && other.id == id

    companion object {
        private var ID: TruckID = 0L
        fun next() = ID++
    }
}