@file:Suppress("PackageName")

package `2023`

import Project
import kotlin.math.max

val numberRegex = Regex("^\\d+$")


class DayThirteen(file: String) : Project() {
    val signalPairs = whitelineSeperatedGrouper(file, { SignalPair(listOf(Signal(it[0]), Signal(it[1]))) }, { it })

    class SignalPair(val signals: List<Signal>) {
        fun outOfOrder(): Boolean {
            return outOfOrder(signals[0], signals[1])!!
        }

        fun outOfOrder(sig1: Signal, sig2: Signal): Boolean? {

            if (!sig1.isList() && !sig2.isList()) {
                println("Compare ${sig1.value} vs ${sig2.value}")
                // If both values are integers, the lower integer should come first.
                return if (sig1.value == sig2.value) null else sig1.value > sig2.value
            }

            sig1.convertToListIfNecessary()
            sig2.convertToListIfNecessary()

            for (i in 0 until max(sig1.packets.size, sig2.packets.size)) {
                if (i == sig2.packets.size) {
                    return true
                }
                if (i < sig1.packets.size) {
                    val outOfOrder = outOfOrder(sig1.packets[i], sig2.packets[i])
                    if (outOfOrder != null) {
                        return outOfOrder
                    }
                } else {
                    return false
                }
            }

            return null
        }
    }

    class Signal(var value: Int, var packets: ArrayList<Signal>) {
        constructor(s: String): this(-1, ArrayList()) {
            val tokens = s.replace("[",",[,").replace("]",",],").split(",").toMutableList()
            tokens.removeIf { it.isBlank() }

            while (tokens.size > 0) {
                val token = tokens.removeFirst()
                if (token.matches(numberRegex)) {
                    packets.add(Signal(token.toInt(), ArrayList()))
                } else {
                    var depthCount = 1
                    val subTokens = ArrayList<String>()
                    while (depthCount > 0) {
                        val subToken = tokens.removeFirst()
                        if (subToken == "[") {
                            depthCount++
                        } else if (subToken == "]") {
                            depthCount--
                        }

                        if (depthCount > 0) {
                            subTokens.add(subToken)
                        }
                    }
                    packets.add(Signal(subTokens.joinToString(",","","")))
                }
            }
        }

        fun isList(): Boolean {
            return value == -1
        }

        fun convertToListIfNecessary() {
            if (!isList()) {
                packets.add(Signal(value, ArrayList()))
                value = -1
            }
        }
    }


    override fun part1(): Any {
        var sum = 0
        signalPairs.forEachIndexed { i, it ->
            println("== Pair ${i + 1} ==")
            if (!it.outOfOrder()) {
                println("    - Left side is smaller, so inputs are in the right order")
                sum += i + 1
            } else {
                println("    - Right side is smaller, so inputs are not in the right order")
            }
        }

        return sum
    }

    override fun part2(): Any {
        val allSignals = ArrayList<Signal>()

        signalPairs.forEach { allSignals.addAll(it.signals) }

        val divider1 = Signal("[[2]]")
        val divider2 = Signal("[[6]]")

        allSignals.add(divider1)
        allSignals.add(divider2)

        allSignals.sortWith { a, b ->
            if (SignalPair(listOf(a, b)).outOfOrder()) 1 else -1
        }

        var divider1Index = -1
        var divider2Index = -1

        allSignals.forEachIndexed { i, it ->
            if (it == divider1) {
                divider1Index = i + 1
            } else if (it == divider2) {
                divider2Index = i + 1
            }
        }

        return divider1Index * divider2Index
    }

}