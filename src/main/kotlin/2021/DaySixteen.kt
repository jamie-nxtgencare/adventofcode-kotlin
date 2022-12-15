@file:Suppress("PackageName")
package `2021`

import Project

class DaySixteen(file: String) : Project() {
    private val packet = Packet(mapLettersPerLines(file) { it.map { c -> String.format("%04d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(c.toString(), 16)))) }}.flatten().joinToString(""))

    override fun part1(): Any {
        //println(packet.toString())
        return packet.getVersionSum()
    }

    override fun part2(): Any {
        return packet.getResult()
    }
}

class Packet(val input: String) {
    private val SUM = "0"
    private val PRODUCT = "1"
    private val MIN = "2"
    private val MAX = "3"
    private val GREATER = "5"
    private val LESS = "6"
    private val EQUAL = "7"

    private val LITERAL = "100"

    private var i = 0
    private var version = -1
    private var type = ""
    private var lengthTypeId = ""
    private var literal = -1L
    private val subpackets = ArrayList<Packet>()

    init {
        version = read(3).toInt(2)
        type = read(3)

        if (type == LITERAL) {
            var cont = read(1)
            var rest = read(4)
            while(cont == "1") {
                cont = read(1)
                rest += read(4)
            }
            literal = rest.toLong(2)
        } else {
            lengthTypeId = read(1)

            if (lengthTypeId == "0") {
                val subPacketBits = read(15).toInt(2)
                var rest = read(subPacketBits)

                while (rest.isNotBlank()) {
                    val packet = Packet(rest)
                    subpackets.add(packet)
                    rest = packet.rest()
                }
            } else {
                var remainingPackets = read(11).toInt(2)
                var rest = rest()
                while (remainingPackets-- > 0) {
                    val packet = Packet(rest)
                    subpackets.add(packet)
                    rest = packet.rest()
                }
                reset(rest)
            }
        }
    }

    private fun reset(rest: String) {
        i = input.length - rest.length
    }

    private fun rest(): String {
        val rest = input.substring(i, input.length)
        i = input.length
        return rest
    }

    private fun read(count: Int): String {
        val out = input.substring(i, i + count)
        i += count
        return out
    }

    fun getVersionSum(): Int {
        return version + subpackets.sumOf { it.getVersionSum() }
    }

    override fun toString(): String {
        return toString(0)
    }

    private fun toString(depth: Int): String {
        val s = String.format("%sVersion: %s (Value: %s)", "\t".repeat(depth), version, if (type == LITERAL) literal.toString() else "operator")
        val subStr = subpackets.map { it.toString(depth+1) }.joinToString("\n")
        return "$s\n$subStr"
    }

    fun getResult(): Long {
        if (type == LITERAL) {
            return literal
        }

        val subs = subpackets.map { it.getResult() }

        return when (type.toInt(2).toString()) {
            SUM -> subs.sum()
            PRODUCT -> subs.reduce {a, b -> a * b}
            MIN -> subs.minOrNull()!!
            MAX -> subs.maxOrNull()!!
            GREATER -> if (subs[0] > subs[1]) 1L else 0L
            LESS -> if (subs[0] < subs[1]) 1L else 0L
            EQUAL -> if (subs[0] == subs[1]) 1L else 0L
            else -> { throw Exception("Unrecognized type")}
        }
    }
}
