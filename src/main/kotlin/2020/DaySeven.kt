@file:Suppress("PackageName")

package `2020`

import Project

class DaySeven(file: String) : Project() {
    private val bagStrMap = mapFileLines(file) { it.replace("bags?".toRegex(), "").split("contain") }
    private val bags : Map<String, List<BagCount>> = getBags()

    private fun getBags(): Map<String, List<BagCount>> {
        return bagStrMap
            .map {
                it2 -> it2[0].trim() to
                    it2[1].split(",").map {
                        it3 ->
                            if (it3.trim(' ', ',', '.') == "no other") BagCount(0, "no other")
                            else BagCount("(^\\d+) (.*)".toRegex().findAll(it3.trim(' ', ',', '.')).iterator().next())
                    }
            }
            .toMap()
    }

    private fun getBagCost(bag: String): Int {
        val contents = bags[bag]
        var cost = 1
        if (contents?.size == 1 && contents[0].bag == "no other") {
            return cost
        }

        contents?.forEach {
            cost += getBagCost(it.bag) * it.count
        }

        return cost
    }

    private fun contains(haystackBag: String, needleBag: String): Boolean {
        val contents = bags[haystackBag]
        if (contents?.size == 1 && contents[0].bag == "no other bags") {
            return false
        }

        if (contents?.contains(BagCount(0, needleBag)) == true) {
            return true
        }

        return contents?.any { contains(it.bag, needleBag) } == true
    }

    override fun part1(): Int {
       return bags.keys.filter { it != "shiny gold" && contains(it, "shiny gold") }.size
    }

    override fun part2(): Int {
        return getBagCost("shiny gold") - 1 // -1 because don't include yourself
    }

    class BagCount(val count: Int, val bag: String) {
        constructor(result: MatchResult) : this(result.groupValues[1].toInt(), result.groupValues[2])

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is BagCount) return false

            if (bag != other.bag) return false

            return true
        }

        override fun hashCode(): Int {
            return bag.hashCode()
        }

    }
}
