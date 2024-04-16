package org.example.model

import java.util.*

data class QueuesState(val pending: Queue<Truck>, val queues: List<List<Truck?>>)
