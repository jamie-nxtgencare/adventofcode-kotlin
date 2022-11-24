@file:Suppress("PackageName")
package `2021`

import Project

class DayEleven(file: String) : Project {
    private val grid: List<List<Int>> = mapLettersPerLines(file) { it.map { c -> Character.getNumericValue(c) }}
    private var dumboGrid = ArrayList<ArrayList<Dumbo>>()
    private var dumbos: ArrayList<Dumbo> = ArrayList()
    private var partOneFlashes: Int = 0

    init {
        populateDumbos(grid)
    }

    override fun part1(): Any {
        partOneFlashes = 0
        for (i in 0 until 100) {
            step()
        }
        return partOneFlashes
    }

    override fun part2(): Any {
        var step = 1
        populateDumbos(grid)
        while (!step()) {
            step++
        }
        return step
    }

    private fun step(): Boolean {
        var toIncrement: List<Dumbo> = ArrayList(dumbos)
        val toReset: ArrayList<Dumbo> = ArrayList()
        var stepPopped = 0

        while (toIncrement.isNotEmpty()) {
            val popped = toIncrement.filter { it.increment() }
            toReset.addAll(popped)
            toIncrement = popped.map { it.neighbours(dumboGrid) }.flatten()
            partOneFlashes += popped.size
            stepPopped += popped.size
        }

        toReset.forEach { it.reset() }
        return stepPopped == dumbos.size
    }

    private fun populateDumbos(grid: List<List<Int>>) {
        dumboGrid = ArrayList()
        dumbos = ArrayList()
        for (y in grid.indices) {
            dumboGrid.add(ArrayList())
            for (x in grid[y].indices) {
                val dumbo = Dumbo(x, y, grid[y][x])
                dumbos.add(dumbo)
                dumboGrid[y].add(dumbo)
            }
        }
    }
}

class Dumbo(val x:Int, val y:Int, var power:Int){
    fun neighbours(dumboGrid: List<List<Dumbo>>): List<Dumbo> {
        return listOf(
            safeGet(dumboGrid, x - 1, y - 1),
            safeGet(dumboGrid, x,     y - 1),
            safeGet(dumboGrid, x + 1, y - 1),
            safeGet(dumboGrid, x - 1, y),
            // this
            safeGet(dumboGrid, x + 1, y),
            safeGet(dumboGrid, x - 1, y + 1),
            safeGet(dumboGrid, x,     y + 1),
            safeGet(dumboGrid, x + 1, y + 1),
        ).mapNotNull { it }
    }

    fun increment(): Boolean {
        if (power < 10) {
            power++
            if (power == 10) return true
        }

        return false
    }

    private fun safeGet(dumboGrid: List<List<Dumbo>>, x: Int, y: Int): Dumbo? {
        return if (y >= 0 && y < dumboGrid.size && x >= 0 && x < dumboGrid[y].size) dumboGrid[y][x] else null
    }

    fun reset() {
        power = 0
    }
}
