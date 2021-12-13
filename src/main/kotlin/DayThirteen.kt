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
        line = lines[i++]
        while (i < lines.size) {
            val pair = line.split("fold along ").last().split("=")
            instructions.add(Pair(pair.first(), pair.last().toInt()))
            line = lines[i++]
        }

        val xWidth = coords.maxOf { it.first } + 1
        val yHeight = coords.maxOf { it.second } + 1

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

    private fun fold(passedGrid: List<ArrayList<Boolean>>, instruction: Pair<String, Int>): List<ArrayList<Boolean>> {

        val grid = if (instruction.first == "x") transpose(passedGrid) else passedGrid

        val newGrid = grid.subList(0, instruction.second)
        val folded = grid.subList(instruction.second + 1, grid.size)
        val reversed = folded.reversed()

        var row = folded.size - 1
        for (i in reversed.size - 1 downTo 0) {
            for (j in folded[row].indices) {
                newGrid[row][j] = if (reversed[i][j]) true else newGrid[row][j]
            }
            row--
        }

        return if (instruction.first == "x") transpose(newGrid) else newGrid
    }

    private fun transpose(passedGrid: List<ArrayList<Boolean>>): List<ArrayList<Boolean>> {
        val newGrid = ArrayList<ArrayList<Boolean>>()

        for (j in passedGrid.indices) {
            for (i in passedGrid[j].indices) {
                if (newGrid.size < i + 1) newGrid.add(ArrayList())
                if (newGrid[i].size < j + 1) newGrid[i].add(false)
                newGrid[i][j] = passedGrid[j][i]
            }
        }

        return newGrid
    }

    private fun countDots(grid: List<ArrayList<Boolean>>): Int {
        return grid.sumOf { it.map { i: Boolean -> if (i) 1 else 0 }.sum() }
    }

    override fun part2(): Any {
        return -1
    }

}