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
        return getSteps(graph, instructions, "AAA") { location: String -> location != "ZZZ" }
    }

    private fun getSteps(graph: MutableMap<String, Pair<String, String>>, instructions: List<String>, start: String, endCondition: (String) -> Boolean): Long {
        var steps = 0L
        var location = start

        while(endCondition.invoke(location)) {
            val paths = graph[location]!!
            val i = (steps % instructions.size).toInt()

            location = if (instructions[i] == "L") {
                paths.first
            } else {
                paths.second
            }
            steps++
        }


        return steps
    }

    override fun part2(): Any {
        /*val lines = listOf(
            "LR",
            "",
            "11A = (11B, XXX)",
            "11B = (XXX, 11Z)",
            "11Z = (11B, XXX)",
            "22A = (22B, XXX)",
            "22B = (22C, 22C)",
            "22C = (22Z, 22Z)",
            "22Z = (22B, 22B)",
            "XXX = (XXX, XXX)"
        )
        val instructions = lines[0].split("").filter { it.isNotBlank() }
        val graph: MutableMap<String, Pair<String, String>> = HashMap()
        for (i in 2 until lines.size) {
            val path = lines[i].split(" = (")
            val dirs = path[1].replace(")","").split(", ")
            graph[path[0]] = Pair(dirs[0], dirs[1])
        }*/

        val rings = graph.keys.filter { it.endsWith("A") }.map { getSteps(graph, instructions, it) { location: String -> !location.endsWith("Z") } }
        return lcm(rings.toLongArray())
    }

    private fun gcd(aStart: Long, bStart: Long): Long {
        var a = aStart
        var b = bStart
        while (b > 0) {
            val temp = b
            b = a % b
            a = temp
        }
        return a
    }

    private fun gcd(input: LongArray): Long {
        var result = input[0]
        for (i in 1 until input.size) result = gcd(result, input[i])
        return result
    }

    private fun lcm(a: Long, b: Long): Long {
        return a * (b / gcd(a, b))
    }

    private fun lcm(input: LongArray): Long {
        var result = input[0]
        for (i in 1 until input.size) result = lcm(result, input[i])
        return result
    }
}