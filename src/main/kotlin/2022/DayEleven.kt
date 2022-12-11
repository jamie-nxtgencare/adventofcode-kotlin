@file:Suppress("PackageName")

package `2022`

import Project
import kotlin.math.floor

class DayEleven(file: String) : Project {
    private val monkeys: List<Monkey> = whitelineSeperatedGrouper(file, { it }, { it }).map { Monkey(it) }

    class Monkey(descriptionLines: List<String>) {
        /*
            Monkey 0:
              Starting items: 79, 98
              Operation: new = old * 19
              Test: divisible by 23
                If true: throw to monkey 2
                If false: throw to monkey 3
         */
        val label = descriptionLines[0].trim().split(" ")[1].split(":")[0]
        val startingItems = descriptionLines[1].trim().split(": ")[1].split(", ").map { it.toInt() }.toMutableList()
        val operation = Operation(descriptionLines[2].trim().split(": new = ")[1].split(" "))
        val test = Test(descriptionLines[3].trim().split(": ")[1].split(" "))
        val trueTarget = descriptionLines[4].trim().split(": ")[1].split(" ")[3]
        val falseTarget = descriptionLines[5].trim().split(": ")[1].split(" ")[3]
        val holdingItems = startingItems
        var inspectionCount: Int = 0
    }

    class Operation(split: List<String>) {
        val arg1 = split[0]
        val arg2 = split[2]
        val operator: (Int, Int) -> Int = if (split[1] == "+") { a, b  -> a + b } else { a, b  -> a * b }
    }

    class Test(split: List<String>) {
        val arg1 = split[2].toInt()
        fun assert(test: Int): Boolean {
            return test % arg1 == 0
        }
    }

    override fun part1(): Any {

        for (i in 0 until 20) {
            monkeys.forEach {
                println("Monkey ${it.label}:")

                it.holdingItems.forEach { item ->
                    // Inspect
                    it.inspectionCount++
                    println("  Monkey inspects an item with a worry level of ${item}.")
                    var new = it.operation.operator.invoke(
                        if (it.operation.arg1 == "old") item else it.operation.arg1.toInt(),
                        if (it.operation.arg2 == "old") item else it.operation.arg2.toInt()
                    )
                    println("    Worry level is set to $new.")
                    // Reduce level
                    new = floor(new.toDouble() / 3).toInt()
                    println("    Monkey gets bored with item. Worry level is divided by 3 to $new.")
                    // Test
                    val test = it.test.assert(new)
                    println("    Current worry level is${if (!test) " not" else ""} divisible by ${it.test.arg1}")
                    // Throw
                    val target = monkeys.first { other -> other.label == if (test) it.trueTarget else it.falseTarget }
                    target.holdingItems.add(new)
                    println("    Item with worry level $new is thrown to monkey ${target.label}.")
                }
                it.holdingItems.clear()
            }
        }

        val monkeyBusiness = monkeys.sortedByDescending { it.inspectionCount }.take(2).map { it.inspectionCount }

        return monkeyBusiness[0] * monkeyBusiness[1]
    }

    override fun part2(): Any {
        return -1
    }

}