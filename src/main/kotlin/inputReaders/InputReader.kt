package org.example.inputReaders

import org.example.model.Order
import org.example.model.TruckID


sealed class Result<out A, out B>
data class Success<out A>(val value: A) : Result<A, Nothing>()
data class ParseError<out B>(val error: B) : Result<Nothing, B>()

typealias ErrorMsg = String

abstract class InputReader {
    abstract fun nextOrder(): Order

    protected fun parse(instr: String, arg: String): Result<Order, ErrorMsg>? {
        when (instr.trim().lowercase()) {
            "a", "arrive" -> {
                val weight = arg.toIntOrNull() ?: return ParseError("\"$arg\" is invalid weight")

                if (weight <= 0) return ParseError("weight should be greater than 0")

                return Success(Order.Arrived(weight))
            }

            "status" -> return Success(Order.Status)
            "step" -> return Success(Order.Step)
            "w", "wait", "waitingtime" -> {
                val truckId: TruckID = arg.toLongOrNull() ?: return ParseError("\"$arg\" is invalid truck id")
                return Success(Order.WaitingTime(truckId))
            }

            "h", "help" -> return null
            "stop", "x" -> return Success(Order.Stop)

            else -> return ParseError("$instr is unknown command")
        }
    }

    protected fun printInstructions() {
        println(
            """
            |Available instructions:
            |  a <weight> - To add truck with weight
            |  status - To get current status
            |  step - To take next step
            |  w <truckId> - To get calculated waiting time of truck with id
            |  h - To get instructions
            |  stop - To stop simulation (it need to me last command)
            """.trimMargin()
        )
    }
}
