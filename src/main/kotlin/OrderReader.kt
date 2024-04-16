package org.example

import org.example.model.Order

interface OrderReader {
    fun nextOrder(): Order
}
