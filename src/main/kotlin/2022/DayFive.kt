@file:Suppress("PackageName")

package `2022`

import Project
import java.util.*
import kotlin.collections.HashMap

class DayFive(file: String) : Project {
    private val lines = getLines(file)
    private val stackStrings = lines.takeWhile { it.isNotBlank() }
    private val moves = lines.takeLastWhile { it.isNotBlank() }.map { it.split(" ") }
    private val stacks = HashMap<Int, Stack<String>>()

    init {
        stackStrings.subList(0, stackStrings.size - 1).reversed().forEach {
            for ((stackId, i) in (1 until it.length step 4).withIndex()) {
                if ((it[i] ?: "").toString().isNotBlank()) {
                    stacks[stackId] = stacks[stackId] ?: Stack()
                    stacks[stackId]!!.push(it[i].toString())
                }
            }
        }
    }

    override fun part1(): Any {
        moves.forEach {
            val count = it[1].toInt()
            val from = it[3].toInt() - 1
            val to = it[5].toInt() - 1

            for (i in 0 until count) {
                val popped = stacks[from]!!.pop()
                stacks[to]!!.push(popped)
            }
        }
        return stacks.values.map { it.peek() }.joinToString("", "", "") { it }
    }

    override fun part2(): Any {
        return -1
    }

}