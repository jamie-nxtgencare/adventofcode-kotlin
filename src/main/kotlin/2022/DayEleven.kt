@file:Suppress("PackageName")

package `2022`

import Project
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.floor

class DayEleven(val file: String) : Project() {
    private var monkeys: List<Monkey> = getMonkeys()

    private fun getMonkeys(): List<Monkey> {
        return whitelineSeperatedGrouper(file, { it }, { it }).map { Monkey(it) }
    }

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
        val startingItems = descriptionLines[1].trim().split(": ")[1].split(", ").map { BigInteger.valueOf(it.toLong()) }.toMutableList()
        val operation = Operation(descriptionLines[2].trim().split(": new = ")[1].split(" "))
        val test = Test(descriptionLines[3].trim().split(": ")[1].split(" "))
        val trueTarget = descriptionLines[4].trim().split(": ")[1].split(" ")[3]
        val falseTarget = descriptionLines[5].trim().split(": ")[1].split(" ")[3]
        val holdingItems = startingItems
        var inspectionCount: BigInteger = BigInteger.valueOf(0)
    }

    class Operation(split: List<String>) {
        val arg1 = split[0]
        val arg2 = split[2]
        val operator: (BigInteger, BigInteger) -> BigInteger = if (split[1] == "+") { a, b  -> a + b } else { a, b  -> a * b }
    }

    class Test(split: List<String>) {
        val arg1: BigInteger = BigInteger.valueOf(split[2].toLong())
        fun assert(test: BigInteger): Boolean {
            return test.mod(arg1) == BigInteger.valueOf(0)
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
                        if (it.operation.arg1 == "old") item else BigInteger.valueOf(it.operation.arg1.toLong()),
                        if (it.operation.arg2 == "old") item else BigInteger.valueOf(it.operation.arg2.toLong())
                    )
                    println("    Worry level is set to $new.")
                    // Reduce level
                    new = new.toBigDecimal().setScale(2, RoundingMode.FLOOR).divide(BigDecimal.valueOf(3), MathContext.DECIMAL128).toBigInteger()
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
        monkeys = getMonkeys() // reset monkeys

        var mod = BigDecimal.valueOf(1L)

        monkeys.forEach {
            mod = mod.times(BigDecimal.valueOf(it.test.arg1.toLong()))
        }

        for (i in 0 until 10_000) {
            monkeys.forEach {
                it.holdingItems.forEach { item ->
                    // Inspect
                    it.inspectionCount++

                    var new = it.operation.operator.invoke(
                        if (it.operation.arg1 == "old") item else BigInteger.valueOf(it.operation.arg1.toLong()),
                        if (it.operation.arg2 == "old") item else BigInteger.valueOf(it.operation.arg2.toLong())
                    )

                    // Reduce level
                    new = new.toBigDecimal().setScale(2, RoundingMode.FLOOR).divideAndRemainder(mod, MathContext.DECIMAL128).last().toBigInteger()

                    // Test
                    val test = it.test.assert(new)

                    // Throw
                    val target = monkeys.first { other -> other.label == if (test) it.trueTarget else it.falseTarget }
                    target.holdingItems.add(new)

                }
                it.holdingItems.clear()
            }

            if (listOf(1, 20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10_000).contains(i + 1)) {
                println("== After round ${i + 1} ==")
                monkeys.forEach {
                    println("Monkey ${it.label} inspected items ${it.inspectionCount} times.")
                }
            }
        }

        val monkeyBusiness = monkeys.sortedByDescending { it.inspectionCount }.take(2).map { it.inspectionCount }

        return monkeyBusiness[0] * monkeyBusiness[1]
    }

}