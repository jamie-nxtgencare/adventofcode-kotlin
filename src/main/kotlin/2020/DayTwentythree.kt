@file:Suppress("PackageName")

package `2020`

import Project

class DayTwentythree(val file: String): Project {
    val debug = false
    private var cache = HashMap<Int, Cup>()
    private var cup = getCup(getLines(file)[0].split("").filter { it.isNotBlank() }.map { it.toInt()} )
    private var startingCup = cup
    private var pickedUp: Cup? = null
    private var maxNumber = 0

    private fun getCup(map: List<Int>): Cup {
        var first: Cup? = null
        var last: Cup? = null
        map.forEach {
            if (it > maxNumber) {
                maxNumber = it
            }
            val cup = Cup(cache, it)
            cache[it] = cup

            if (last != null) {
                last!!.next = cup
            }
            if (first == null) {
                first = cup
            }
            last = cup
        }
        last?.next = first

        return first!!
    }

    class Cup(val cache: Map<Int, Cup>, val number: Int) {
        fun pickUp(count: Int): Cup {
            val pickedUp = this.next!!

            var next = this
            for (i in 0 until count) {
                next = next.next!!
            }
            this.next = next.next
            next.next = null

            return pickedUp
        }

        private fun getByNumber(searchNumber: Int): Cup? {
            if (number == searchNumber) {
                return this
            }

            var next = this.next
            while (next?.number != searchNumber) {
                if (next == null || next == this) {
                    return null
                }
                next = next.next
            }
            return next
        }

        fun getDestination(pickedUp: Cup, maxNumber: Int): Cup {
            var destNum = number
            var destination: Cup? = this
            while (destination != null) {
                destNum--
                if (destNum == 0) {
                    destNum = maxNumber
                }
                destination = pickedUp.getByNumber(destNum)
            }
            return cache[destNum]!!
        }

        fun insert(pickedUp: Cup) {
            val oldNext = next
            next = pickedUp
            var step = next!!
            while (step.next != null) {
                step = step.next!!
            }
            step.next = oldNext
        }

        override fun toString(): String {
            return number.toString()
        }

        var next: Cup? = null
    }

    override fun part1(): Any {
        go(100)

        while (cup.number != 1) {
            cup = cup.next!!
        }

        var s = ""
        cup = cup.next!!
        while (cup.number != 1) {
            s += cup.number.toString()
            cup = cup.next!!
        }

        return s.toInt()
    }

    override fun part2(): Any {
        cache = HashMap()
        val newSegment = Cup(cache, 10)
        var next: Cup? = newSegment
        cache[next?.number!!] = next

        for (i in 11..1_000_000) {
            next?.next = Cup(cache, i)
            next = next?.next
            cache[next?.number!!] = next!!
        }

        cup = getCup(getLines(file)[0].split("").filter { it.isNotBlank() }.map { it.toInt()} )

        next = cup.next
        while (next?.next != cup) {
            next = next?.next
        }

        next.insert(newSegment)
        maxNumber = 1_000_000
        go(10_000_000)

        return this.cache[1]?.next?.number?.toLong()?.times(this.cache[1]?.next?.next?.number?.toLong()!!)!!
    }

    private fun go(n: Int) {
        for (i in 1..n) {
            if (debug) {
                println("-- move $i --")
                println(getCupString(startingCup))
            }
            pickedUp = cup.pickUp(3)
            if (debug) {
                println(getCupString(pickedUp!!, true))
            }
            val destination = cup.getDestination(pickedUp!!, maxNumber)
            val destinationNumber = destination.number
            if (debug) {
                println("destination: $destinationNumber")
            }
            destination.insert(pickedUp!!)
            if (debug) {
                println()
            }
            cup = cup.next!!
        }
    }

    fun getCupString(curr: Cup, commaSeparate: Boolean = false): String {
        val number = curr.number
        var cupString = if (!commaSeparate) "($number)" else "$number"

        var next: Cup? = curr.next
        while(next != curr && next != null) {
            val nextNumber = next.number
            cupString += if (commaSeparate) "," else ""
            cupString += " $nextNumber"
            next = next.next
        }

        return "cups: $cupString"
    }
}