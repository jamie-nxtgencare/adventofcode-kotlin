import java.util.*
import kotlin.collections.ArrayList

class DayThirteen(file: String) : Project {
    private val lines = getLines(file)
    private var grid: List<ArrayList<Boolean>>
    private val instructions = ArrayList<Pair<String, Int>>()

    init {
        var line = lines.first()
        var i = 1
        val coords = ArrayList<Pair<Int, Int>>()

        while (line.isNotBlank()) {
            val pair = line.split(",")
            coords.add(Pair(pair.first().toInt(), pair.last().toInt()))
            line = lines[i++]
        }
        while (i < lines.size) {
            line = lines[i++]
            val pair = line.split("fold along ").last().split("=")
            instructions.add(Pair(pair.first(), pair.last().toInt()))
        }

        val xWidth = instructions.first { it.first == "x" }.second * 2 + 1
        val yHeight = instructions.first { it.first == "y" }.second * 2 + 1

        val templateRow = ArrayList(Collections.nCopies(xWidth, false))
        val tempGrid = ArrayList<ArrayList<Boolean>>(Collections.nCopies(yHeight, ArrayList()))
        grid = tempGrid.map { ArrayList(templateRow) } // Make sure we aren't using the list reference

        coords.forEach { coord ->
            grid[coord.second][coord.first] = true
        }
    }

    override fun part1(): Any {
        return countDots(fold(grid, instructions.first()))
    }

    override fun part2(): Any {
        var workingGrid = grid
        instructions.forEach {
            workingGrid = fold(workingGrid, it)
        }
        printGrid(workingGrid)
        return countDots(workingGrid)
    }

    private fun fold(passedGrid: List<ArrayList<Boolean>>, instruction: Pair<String, Int>): List<ArrayList<Boolean>> {
        return if (instruction.first == "x") foldX(passedGrid, instruction.second) else foldY(passedGrid, instruction.second)
    }

    private fun foldX(grid: List<java.util.ArrayList<Boolean>>, x: Int): List<java.util.ArrayList<Boolean>> {
        val newGrid = grid.map { ArrayList(it.subList(0, x)) }
        val folded = grid.map { ArrayList(it.subList(x + 1, it.size).reversed()) }

        for (i in folded.indices) {
            for (j in folded[i].indices) {
                newGrid[i][j] = folded[i][j] || newGrid[i][j]
            }
        }
        return newGrid
    }

    private fun foldY(grid: List<java.util.ArrayList<Boolean>>, y: Int): List<java.util.ArrayList<Boolean>> {
        val newGrid = ArrayList(grid.subList(0, y))
        val folded = ArrayList(grid.subList(y + 1, grid.size)).reversed()

        for (i in folded.indices) {
            for (j in folded[i].indices) {
                newGrid[i][j] = folded[i][j] || newGrid[i][j]
            }
        }

        return newGrid
    }

    private fun countDots(grid: List<ArrayList<Boolean>>): Int {
        return grid.sumOf { it.map { i: Boolean -> if (i) 1 else 0 }.sum() }
    }

    companion object {
        private fun printGrid(workingGrid: List<java.util.ArrayList<Boolean>>) {
            workingGrid.forEach { row ->
                row.forEach { column ->
                    print(if (column) "█" else " ")
                }
                println()
            }
            println()
        }
    }
}



