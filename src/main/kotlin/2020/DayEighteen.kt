@file:Suppress("PackageName")
package `2020`

import Project

class DayEighteen(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val lines = getLines(file)
    private val brackets = "(\\([^()]*\\))".toRegex()
    private val addition = "((\\d+) \\+ (\\d+))".toRegex()


    override suspend fun part1(): Any {
        return lines.map { solve(it, 1) }.sum()
    }

    override suspend fun part2(): Any {
        return lines.map { solve(it, 2) }.sum()
    }

    private fun solve(it: String, part: Int): Long {
        var resolve = it
        while (resolve.contains("(")) {
            val matchResult: MatchResult? = brackets.find(resolve)
            var value: String? = matchResult?.groups?.get(1)?.value
            if (value != null) {
                resolve = resolve.replace(value, solve(value.substring(1 until value.length -1), part).toString())
            }
        }

        if (part == 2) {
            while (resolve.contains("+")) {
                val matchResult: MatchResult? = addition.find(resolve)
                val value: String? = matchResult?.groups?.get(1)?.value
                val valueA: String? = matchResult?.groups?.get(2)?.value
                val valueB: String? = matchResult?.groups?.get(3)?.value

                if (value != null && valueA != null && valueB != null) {
                    resolve = resolve.replaceFirst(value, (valueA.toLong() + valueB.toLong()).toString())
                }
            }
        }

        var value = 0L
        var func = "+"
        val args = resolve.split(" ")
        args.forEach {
            if (it == "+" || it == "*") {
                func = it
            } else {
                value = if (func == "+") value + it.toLong() else value * it.toLong()
            }
        }

        return value
    }
}