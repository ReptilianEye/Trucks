package org.example

import org.example.inputReaders.FileReader
import org.example.inputReaders.TerminalReader

fun main() {
//    val reader = TerminalReader
    val reader = FileReader("src/main/resources/dane.in")
    val simulation = Simulation(reader)
    simulation.run()
}