@file:Suppress("PackageName")

package `2022`

import Project
import java.util.*

class DaySix(file: String) : Project {
    private val signal = getLines(file)[0]

    override fun part1(): Any {
        val queue: Queue<String> = LinkedList()
        signal.forEachIndexed { i, it ->
            while (queue.contains(it.toString())) { queue.poll() }
            queue.add(it.toString())
            if (queue.size == 4) {
                return i + 1
            }
        }
        return -1
    }

    override fun part2(): Any {
        return -1
    }

}