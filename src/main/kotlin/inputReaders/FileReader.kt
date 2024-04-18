package org.example.inputReaders

import org.example.model.Order
import java.io.File

class FileReader(fileName: String) : InputReader() {
    private val linesIterator: Iterator<String>

    init {
        val file = File(fileName)
        if (!file.exists()) throw IllegalArgumentException("File $file does not exist")

        linesIterator = file.bufferedReader().lineSequence().iterator()
    }

    override fun nextOrder(): Order {
        while (linesIterator.hasNext()) {
            val (instr, arg) = (linesIterator.next() + " ").split(' ')
            when (val res = parse(instr, arg)) {
                is Success -> return res.value
                is ParseError -> println(res.error)
                else -> {}
            }
        }
        return Order.Stop
    }
}