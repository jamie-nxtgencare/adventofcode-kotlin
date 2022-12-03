@file:Suppress("PackageName")

package `2022`

import Project

fun getPriority(char: Int): Int {
    return if (char.toChar().isLowerCase()) char - 96 else char - 38
}

class DayThree(file: String) : Project {
    private val rucksacks = mapFileLines(file) { RuckSack(it) }

    class RuckSack(stuff: String) {
        val charCount: HashMap<Int, Int>
        private val charCountLeft: HashMap<Int, Int>
        private val charCountRight: HashMap<Int, Int>

        init {
            charCount = getCharCounts(stuff)
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
            return charCountLeft.filter { charCountRight.containsKey(it.key) }.keys.sumOf { getPriority(it) }
        }

    }

    override fun part1(): Any {
        return rucksacks.sumOf { it.getPriority() }
    }

    override fun part2(): Any {
        val badges = ArrayList<Int>()
        for (i in rucksacks.indices step 3) {
            val group = rucksacks.subList(i, i + 3)
            val badge = group[0].charCount.filter {
                group[1].charCount[it.key] != null && group[2].charCount[it.key] != null
            }.firstNotNullOf { it }.key

            val priority = getPriority(badge)

            badges.add(priority)
        }

        return badges.sum()
    }

}