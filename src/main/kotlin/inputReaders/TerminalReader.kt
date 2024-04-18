package org.example.inputReaders

import org.example.inputReaders.InstructionParser.parse
import org.example.model.Order


class TerminalReader : InputReader {
    init {
        InstructionParser.printInstructions()
    }

    override fun nextOrder(): Order {
        while (true) {
            val (instr, arg) = (readln() + " ").split(' ')
            when (val res = parse(instr, arg)) {
                is Success -> return res.value
                is ParseError -> println(res.error)
                null -> InstructionParser.printInstructions()
//                else -> {throw Error("Unknown Result type")}
            }
        }
    }

//    private fun printInstructions() {
//        println("Available instructions:")
//        println("\ta <weight>\t\t To add truck with weight")
//        println("\tstatus\t\t\t To get current status")
//        println("\tstep\t\t\t To take next step")
//        println("\tw <truckId> \t To get calculated waiting time of truck with id")
//        println("\th \t\t\t\t To get instructions")
//        println("\tstop\t\t\t To stop simulation (it need to me last command)")
//    }

}

