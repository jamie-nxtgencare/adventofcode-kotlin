@file:Suppress("PackageName")
package `2021`
import Project
import kotlin.math.roundToInt

class DayThree(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val input: List<List<Int>> = mapLettersPerLines(file) { it.map { it2 -> (it2 + "").toInt() }}
    private var mostCommonBits: List<Int> = ArrayList()
    private var leastCommonBits: List<Int> = ArrayList()

    override suspend fun part1(): Any {
        val gammaSums: MutableList<Double> = ArrayList()

        for (row in input) {
            for (i in row.indices) {
                if (gammaSums.size < i + 1) gammaSums.add(0.0)
                gammaSums[i] = gammaSums[i] + row[i]
            }
        }

        for(i in gammaSums.indices) {
            gammaSums[i] = (gammaSums[i] / input.size.toDouble()).roundToInt().toDouble()
        }

        this.mostCommonBits = gammaSums.map { it.toInt() }
        this.leastCommonBits = gammaSums.map { flip(it, false) }

        val gamma = mostCommonBits.joinToString("").toInt(2)
        val epsilon = leastCommonBits.joinToString("").toInt(2)
        return gamma * epsilon
    }

    private fun flip(it: Double, mostCommon: Boolean) = if (mostCommon) it.toInt() else if (it.toInt() == 1) 0 else 1

    override suspend fun part2(): Any {
        val oxygen = getCandidates(input, true).first().joinToString("").toInt(2)
        val co2 = getCandidates(input, false).first().joinToString("").toInt(2)
        return oxygen * co2
    }

    private fun getCandidates(input: List<List<Int>>, mostCommon: Boolean): MutableList<List<Int>> {
        val candidates = ArrayList(input)
        var targetSubstr = ""
        var subStringSize = 1;

        while (candidates.size > 1) {
            val toRemove = ArrayList<List<Int>>()

            var sum = 0.0
            for (row in candidates) {
                sum += row[subStringSize - 1]
            }

            targetSubstr += flip((sum / candidates.size.toDouble()).roundToInt().toDouble(), mostCommon)

            for (candidate in candidates) {
                val candidateSubstr = candidate.joinToString("").substring(0, subStringSize)
                if (sum == 0.5 && !candidateSubstr.endsWith(if (mostCommon) "1" else "0")) {
                    toRemove.add(candidate)
                } else if (targetSubstr != candidateSubstr) {
                    toRemove.add(candidate)
                }
            }
            candidates.removeAll(toRemove.toSet())
            subStringSize++

        }

        return candidates;
    }

}