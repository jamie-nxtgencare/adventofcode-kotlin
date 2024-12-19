@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.parseInt

class DayFifteen(file: String, isTest: Boolean = false) : Project(file, isTest) {
    val input = getLines(file).flatMap { it.split(",").filter { it.isNotBlank() }}
    val hashcodes = input.map { it.split("").filter { it.isNotBlank() }.map { it[0].code.toDouble() }.toMutableList()}.toMutableList()
    val operations = input.map { if (it.contains("=")) SetOperation(it, parseInt(it.split("=")[1])) else DeleteOperation(it) }

    class SetOperation(it: String, val focalLength: Int) : Operation(it.split("=")[0])
    class DeleteOperation(it: String) : Operation(it.replace("-", ""))
    open class Operation(val label: String)

    override suspend fun part1(): Any {
        return hashcodes.sumOf { hashDoubles(it) }
    }

    private fun hash(it: String): Double {
        return hashDoubles(it.split("").filter { it.isNotBlank() }.map { it[0].code.toDouble()}.toMutableList())
    }

    private fun hashDoubles(it: MutableList<Double>): Double {
        it[0] = it[0] * 17.0 % 256.0
        return it.reduce { a,b -> (a + b) * 17.0 % 256.0 }
    }

    override suspend fun part2(): Any {
        val boxes = mutableMapOf<Double, MutableList<Pair<String, Int>>>()

        for (operation in operations) {
            val hashCode = hash(operation.label)
            val box = boxes[hashCode] ?: mutableListOf()

            if (operation is DeleteOperation) {
                box.removeAll { it.first == operation.label }
            } else if (operation is SetOperation) {
                val labelledLens = Pair(operation.label, operation.focalLength)
                if (box.any { it.first == operation.label }) {
                    for (i in box.indices) {
                        if (box[i].first == operation.label) {
                            box[i] = labelledLens
                        }
                    }
                } else {
                    box.add(labelledLens)
                }
            }

            boxes[hashCode] = box
        }

        var sum = 0.0
        boxes.forEach {
            val boxValue = it.key + 1
            it.value.forEachIndexed { i, it ->
                val slotValue = i + 1
                sum += boxValue * slotValue * it.second
            }
        }

        return sum
    }
}