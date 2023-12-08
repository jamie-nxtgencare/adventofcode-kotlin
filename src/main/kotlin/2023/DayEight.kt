@file:Suppress("PackageName")

package `2023`

import Project

class DayEight(file: String) : Project() {
    private val lines = getLines(file)
    private val instructions = lines[0].split("").filter { it.isNotBlank() }

    private val graph: MutableMap<String, Pair<String, String>> = HashMap()

    init {
        for (i in 2 until lines.size) {
            val path = lines[i].split(" = (")
            val dirs = path[1].replace(")","").split(", ")
            graph[path[0]] = Pair(dirs[0], dirs[1])
        }
    }

    override fun part1(): Any {
        var steps = 0
        var location = "AAA"

        while(location != "ZZZ") {
            val paths = graph[location]!!

            if (instructions[steps % instructions.size] == "L") {
                location = paths.first
            } else {
                location = paths.second
            }
            steps++
        }


        return steps
    }

    override fun part2(): Any {
        return -1
    }
}