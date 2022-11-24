@file:Suppress("PackageName")
package `2021`
import Project
import java.util.*

class DayTwentythree(file: String) : Project {
    private var rows = 0
    private var cols = 0
    private val grid = mapLettersPerLines(file) {
        val row = it.map { char ->
            Tile(cols++, rows, char.toString())
        }
        rows++
        cols = 0
        row
    }

    private val game: AmphipodGame = AmphipodGame(grid)

    override fun part1(): Any {
        val solve = game.solve()
        return solve
    }

    override fun part2(): Any {
        val grid2 = ArrayList<List<Tile>>()

        grid.forEach {
            val row = ArrayList<Tile>()
            row.addAll(it.map { tile -> Tile(tile.x, if(tile.y <= 2) tile.y else tile.y + 2, tile.toString()) })
            grid2.add(row)

            if (grid2.size == 3) {
                var cols = 0
                grid2.add(" #D#C#B#A#".split("").map { char -> Tile(cols++, 3, char) })
                cols = 0
                grid2.add(" #D#B#A#C#".split("").map { char -> Tile(cols++, 4, char) })
            }
        }

        val part2Game = AmphipodGame(grid2)
        val solve = part2Game.solve()
        return solve
    }
}

class AmphipodGame(startingGrid: List<List<Tile>>) {
    val amphipodLocations = ArrayList<Tile>()
    val grid = ArrayList<ArrayList<Tile>>()

    init {
        startingGrid.forEach {
            val row = ArrayList<Tile>()
            row.addAll(it.map { tile -> Tile(tile.x, tile.y, tile.toString()) })
            amphipodLocations.addAll(row.filter { it.hasAmphipod() })
            grid.add(row)
        }
    }

    fun solve(): Int {
        val moveableAmphipods = getMoveableAmphipods()
        val initialStates = moveableAmphipods.map { getPossibleGameStates(it) }
        val seenStates = HashSet<Int>()
        val nextPotentialMoves: PriorityQueue<Pair<AmphipodGame, Int>> = PriorityQueue(compareBy { it.second })
        nextPotentialMoves.addAll(initialStates.flatten())

        while (nextPotentialMoves.isNotEmpty()) {
            val it = nextPotentialMoves.poll()

            val game = it.first
            val cost = it.second

            if (game.isDone()) {
                return cost
            } else {
                val nextMoveableAmphipods = game.getMoveableAmphipods()
                val states = nextMoveableAmphipods.map { a -> game.getPossibleGameStates(a) }.flatten().map { s -> Pair(s.first, cost + s.second) }.filter { !seenStates.contains(it.hashCode()) }
                seenStates.addAll(states.map { it.hashCode() })
                nextPotentialMoves.addAll(states)
            }
        }

        return -1
    }

    fun getMoveableAmphipods(): List<Tile> {
        return amphipodLocations.filter {
            !isDone(it) && openNeighbours(Pair(it.x, it.y), 1).isNotEmpty()
        }
    }

    fun openNeighbours(coords: Pair<Int, Int>, distance: Int): List<Pair<Pair<Int, Int>, Int>> {
        return listOf(
            Pair(Pair(coords.first - 1, coords.second), distance),
            Pair(Pair(coords.first + 1, coords.second), distance),
            Pair(Pair(coords.first, coords.second - 1), distance),
            Pair(Pair(coords.first, coords.second + 1), distance)
        ).filter { it.first.second >= 0 && it.first.second < grid.size && it.first.first >= 0 && it.first.first < grid[it.first.second].size && grid[it.first.second][it.first.first].isOpen()}
    }

    fun getPossibleGameStates(tile: Tile): List<Pair<AmphipodGame, Int>> {
        val openNeighbours = ArrayList(openNeighbours(tile.coords(), 1))
        var visited = openNeighbours.map { it.first }
        var previousSize = 0
        var step = 2

        while (previousSize != openNeighbours.size) {
            previousSize = openNeighbours.size
            openNeighbours.addAll(openNeighbours.map {
                openNeighbours(it.first, step).filter { n -> !visited.contains(n.first) }
            }.flatten())
            visited = openNeighbours.map { it.first }
            step++
        }

        val validMoves = openNeighbours.filter { isValidMove(tile, it.first) }.sortedBy { i -> i.first.second }

        val states = validMoves.map {
            val game = clone()
            val cost = game.move(game.grid[tile.y][tile.x], it.first, it.second)
            Pair(game, cost)
        }

        return states
    }

    private fun isValidMove(tile: Tile, it: Pair<Int, Int>): Boolean {
        val blocksDoor = grid[it.second][it.first].blocksDoor
        val isSafeGoal = grid[it.second][it.first].isGoalFor(tile.amphipod) && isDone(grid[it.second+1][it.first])

        return isSafeGoal || (!tile.isHallway && !blocksDoor && grid[it.second][it.first].isHallway)
    }

    private fun move(tile: Tile, coords: Pair<Int, Int>, cost: Int): Int {
        val amphipod = tile.amphipod ?: return 0

        val newTile = grid[coords.second][coords.first]
        newTile.amphipod = amphipod
        tile.amphipod = null

        amphipodLocations.clear()
        grid.forEach { row -> amphipodLocations.addAll(row.filter { it.hasAmphipod() }) }

        return amphipod.cost * cost
    }


    fun isDone(tile: Tile): Boolean {
        return tile.isWall || (tile.isDone() && isDone(grid[tile.y+1][tile.x]))
    }

    fun isDone(): Boolean {
        return amphipodLocations.none { !isDone(it) }
    }

    fun countDone(): Int {
        return amphipodLocations.count { isDone(it) }
    }

    private fun clone(): AmphipodGame {
        return AmphipodGame(grid)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AmphipodGame) return false

        if (amphipodLocations != other.amphipodLocations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = amphipodLocations.hashCode()
        result = 31 * result + grid.hashCode()
        return result
    }

    override fun toString(): String {
        var out = ""
        grid.forEach { row ->
            row.forEach {
                out += it
            }
            out += System.lineSeparator()
        }
        return out
    }

}

class Tile(val x: Int, val y: Int, letter: String) {
    val isHallway: Boolean = y == 1
    val blocksDoor = isHallway && (x == 3 || x == 5 || x == 7 || x == 9)
    val isWall: Boolean = letter == "#"
    var amphipod: Amphipod? = if (isWall || letter == "." || letter.isBlank()) null else Amphipod(letter)

    override fun toString(): String {
        return if(isWall) "#" else amphipod?.toString() ?: "."
    }

    fun isOpen(): Boolean {
        return !isWall && amphipod == null
    }

    fun hasAmphipod(): Boolean {
        return amphipod != null
    }

    fun coords(): Pair<Int, Int> {
        return Pair(x, y)
    }

    fun isDone(): Boolean {
        return isGoalFor(amphipod) || isWall
    }

    fun isGoalFor(amphipod: Amphipod?): Boolean {
        return if(amphipod == null) false
            else if (amphipod.label == "A") x == 3
            else if (amphipod.label == "B") x == 5
            else if (amphipod.label == "C") x == 7
            else x == 9
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Tile) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (isHallway != other.isHallway) return false
        if (blocksDoor != other.blocksDoor) return false
        if (isWall != other.isWall) return false
        if (amphipod != other.amphipod) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + isHallway.hashCode()
        result = 31 * result + blocksDoor.hashCode()
        result = 31 * result + isWall.hashCode()
        result = 31 * result + (amphipod?.hashCode() ?: 0)
        return result
    }

}

class Amphipod(val label: String) {
    val cost = if (label == "A") 1 else if (label == "B") 10 else if (label == "C") 100 else 1000

    override fun toString(): String {
        return label
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Amphipod) return false

        if (label != other.label) return false
        if (cost != other.cost) return false

        return true
    }

    override fun hashCode(): Int {
        var result = label.hashCode()
        result = 31 * result + cost
        return result
    }


}