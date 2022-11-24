@file:Suppress("PackageName")

package `2020`

import Project
import java.math.BigInteger

class DayNine(file: String) : Project {
    private val window = if (file.contains("sample")) 5 else 25
    private val nums = mapFileLines(file) { it.toBigInteger() }
    private val numWithoutSum = getNumWithoutSum()

    override fun part1(): BigInteger {
        return numWithoutSum
    }

    override fun part2(): BigInteger {
        val expected = part1()

        for (i in nums.indices) {
            val set = getContiguousSet(i, expected)

            if (set.size > 1) {
                return set.minOrNull()?.add(set.maxOrNull()) ?: BigInteger.valueOf(-1)
            }
        }

        return BigInteger.valueOf(-1)
    }

    private fun getNumWithoutSum(): BigInteger {
        for (i in window+1 until nums.size) {
            if (!hasSum(i, window)) {
                return nums[i]
            }
        }
        return BigInteger.valueOf(-1)
    }

    private fun getContiguousSet(start: Int, expected: BigInteger): Set<BigInteger> {
        val out = HashSet<BigInteger>()
        var currentSet = HashSet<BigInteger>()
        var sum = BigInteger.ZERO

        for (i in start until nums.size) {
            val curr = nums[i]
            currentSet.add(curr)
            sum = sum.add(curr)
            if (sum > expected) {
                return out
            } else if (sum == expected) {
                return currentSet
            }
        }

        return out
    }

    private fun hasSum(i: Int, window: Int): Boolean {
        return getSums(i-1-window until i).contains(nums[i])
    }

    private fun getSums(range: IntRange): Set<BigInteger> {
        val out = HashSet<BigInteger>()

        range.forEach {
            i ->
            range.forEach {
                j ->
                run {
                    if (j != i) {
                        out.add(nums[j].add(nums[i]))
                    }
                }
            }
        }

        return out
    }
}