@file:Suppress("PackageName")

package `2022`

import Project

class DayThree(file: String) : Project {
    private val rucksacks = mapFileLines(file) { RuckSack(it) }

    class RuckSack(val stuff: String) {
        private val charCountLeft: HashMap<Int, Int>
        private val charCountRight: HashMap<Int, Int>

        init {
            charCountLeft = getCharCounts(stuff.subSequence(0, stuff.length/2).toString())
            charCountRight = getCharCounts(stuff.subSequence(stuff.length/2, stuff.length).toString())
        }

        private fun getCharCounts(rucksack: String): HashMap<Int, Int> {
            val charCount : HashMap<Int, Int> = HashMap()
            rucksack.chars().forEach {
                charCount[it] = (charCount[it] ?: 0) + 1
            }
            return charCount
        }

        fun getPriority(): Int {
            return charCountLeft.filter { charCountRight.containsKey(it.key) }
                .keys.sumOf { if (it.toChar().isLowerCase()) it - 96 else it - 38 }
        }
    }

    override fun part1(): Any {
        return rucksacks.sumOf { it.getPriority() }
    }

    override fun part2(): Any {
        return -1
    }

}