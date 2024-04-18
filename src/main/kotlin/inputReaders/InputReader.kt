package org.example.inputReaders

import org.example.model.Order

interface InputReader {
    fun nextOrder(): Order
}
