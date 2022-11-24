@file:Suppress("PackageName")

package `2020`

import Project
import java.math.BigInteger

class DayTen(file: String) : Project {
    private val input = mapFileLines(file) { it.toInt() }.sorted().toMutableList()
    private val differences = input.mapIndexed { i, it -> it - (if (i == 0) 0 else input[i - 1]) }.toMutableList()
    private val memo = HashMap<Int, BigInteger>()
    override fun part1(): Any {
        val m = HashMap<Int, Int>()
        differences.forEach { m[it] = m.computeIfAbsent(it) { 0 } + 1 }
        return m[1]?.times((m[3] ?: -1) + 1) ?: -1
    }
    override fun part2(): Any {
        input.add(0, 0)
        input.add(input[input.size-1]+3)
        return traverse(0)
    }

    private fun traverse(i: Int): BigInteger {
        if (i == input.size - 1) return BigInteger.ONE
        var count = BigInteger.ZERO

        if (memo.containsKey(i)) {
            return memo[i] ?: BigInteger.valueOf(-1)
        }

        if (i+1 < input.size && input[i] + 3 >= input[i+1]) {
            count += traverse(i+1)
        }
        if (i+2 < input.size && input[i] + 3 >= input[i+2]) {
            count += traverse(i+2)
        }
        if (i+3 < input.size && input[i] + 3 >= input[i+3]) {
            count += traverse(i+3)
        }

        memo[i] = count

        return memo[i] ?: BigInteger.valueOf(-1)
    }
}