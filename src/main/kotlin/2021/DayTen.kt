@file:Suppress("PackageName")
package `2021`
import Project
import java.util.*
import kotlin.math.floor

class DayTen(file: String) : Project() {
    private val lines = getLines(file)
    private val pairs = mapOf("(" to ")", "[" to "]", "<" to ">", "{" to "}")
    private val points = mapOf(")" to 3, "]" to 57, "}" to 1197, ">" to 25137)
    private val points2 = mapOf(")" to 1, "]" to 2, "}" to 3, ">" to 4)

    private val incomplete = ArrayList<Stack<String>>()

    override fun part1(): Any {
        val illegalChars = ArrayList<String>()

        lines.forEach {
            val stack = Stack<String>()
            val chars = it.split("").filter { char -> char.isNotBlank() }
            var invalid = false

            for (char in chars) {
                if (stack.isNotEmpty() && pairs[stack.peek()] == char) {
                    stack.pop()
                    continue
                }
                if (pairs.containsKey(char)) {
                    stack.push(char)
                    continue
                }

                illegalChars.add(char)
                invalid = true
                break
            }

            if (!invalid) {
                incomplete.add(stack)
            }
        }


        return illegalChars.map { points[it] }.sumOf { it ?: 0 }
    }

    override fun part2(): Any {
        return incomplete.map {
            var score = 0L
            while (it.isNotEmpty()) {
                score = score * 5 + points2[pairs[it.pop()]]!!
            }
            score
        }.sorted()[floor(incomplete.size / 2.0).toInt()]
    }

}