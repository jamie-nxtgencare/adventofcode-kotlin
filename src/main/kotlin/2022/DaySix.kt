@file:Suppress("PackageName")

package `2022`

import Project
import java.util.*

class DaySix(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val signal = getLines(file)[0]

    private fun getStart(signal: String, size: Int): Any {
        val queue: Queue<String> = LinkedList()
        signal.forEachIndexed { i, it ->
            while (queue.contains(it.toString())) { queue.poll() }
            queue.add(it.toString())
            if (queue.size == size) {
                return i + 1
            }
        }
        return -1
    }

    override suspend fun part1(): Any {
        return getStart(signal, 4)
    }

    override suspend fun part2(): Any {
        return getStart(signal, 14)
    }

}