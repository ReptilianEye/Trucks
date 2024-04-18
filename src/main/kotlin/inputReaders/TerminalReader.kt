package org.example.inputReaders

import org.example.model.Order


object TerminalReader : InputReader() {
    init {
        printInstructions()
    }

    override fun nextOrder(): Order {
        while (true) {
            val (instr, arg) = (readln() + " ").split(' ')
            when (val res = parse(instr, arg)) {
                is Success -> return res.value
                is ParseError -> println(res.error)
                null -> printInstructions()
            }
        }
    }


}

