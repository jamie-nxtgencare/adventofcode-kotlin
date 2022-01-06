import kotlin.math.abs

class DayTwentythree(file: String) : Project {
    private var rows = 0
    private var cols = 0
    private val grid = mapLettersPerLines(file) {
        val row = it.map { char ->
            Tile(0, cols++, rows, char.toString())
        }
        rows++
        cols = 0
        row
    }

    private val game: AmphipodGame = AmphipodGame(grid)

    override fun part1(): Any {
        val states = HashMap<AmphipodGame, Int>()
        val solve = game.solve(states)
        return solve
    }

    override fun part2(): Any {
        return -1
    }
}

class AmphipodGame(startingGrid: List<List<Tile>>, depth: Int = 0) {
    val amphipodLocations = ArrayList<Tile>()
    val grid = ArrayList<ArrayList<Tile>>()

    init {
        startingGrid.forEach {
            val row = ArrayList<Tile>()
            row.addAll(it.map { tile -> Tile(depth, tile.x, tile.y, tile.toString()) })
            amphipodLocations.addAll(row.filter { it.hasAmphipod() })
            grid.add(row)
        }
    }

    fun solve(solvedAmphipodGames: HashMap<AmphipodGame, Int>): Int {
        if (solvedAmphipodGames.containsKey(this)) {
            return solvedAmphipodGames[this]!!
        }

        if (isDone()) {
            return 0
        }

        val moveableAmphipods = getMoveableAmphipods()

        if (moveableAmphipods.isEmpty()) {
            return -1
        }

        val potentialMoves: List<Pair<AmphipodGame, Int>> = moveableAmphipods.map { getPossibleGameStates(it) }.flatten()

        val solvedMoves = potentialMoves.map {
            val game = it.first
            val cost = it.second
            var result = -1
            if (solvedAmphipodGames.containsKey(game) && solvedAmphipodGames[game]!! > 0) {
                result = solvedAmphipodGames[game]!!
            } else if (!solvedAmphipodGames.containsKey(game)) {
                result = game.solve(solvedAmphipodGames)
            }
            Pair(game, if (result >= 0) result + cost else -1)
        }

        val bestMove = solvedMoves.filter { it.second >= 0 }.sortedBy { it.second }.firstOrNull()

        solvedAmphipodGames[this] = bestMove?.second ?: -1
        return solvedAmphipodGames[this]!!
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

        return validMoves.map {
            val game = clone(grid.first().first().depth)
            val cost = game.move(game.grid[tile.y][tile.x], it.first, it.second)
            Pair(game, cost)
        }
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

    private fun clone(depth: Int): AmphipodGame {
        return AmphipodGame(grid, depth + 1)
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

class Tile(val depth: Int, val x: Int, val y: Int, letter: String) {
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
        return isGoalFor(amphipod)
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

        if (depth != other.depth) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (isHallway != other.isHallway) return false
        if (blocksDoor != other.blocksDoor) return false
        if (isWall != other.isWall) return false
        if (amphipod != other.amphipod) return false

        return true
    }

    override fun hashCode(): Int {
        var result = depth
        result = 31 * result + x
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