@file:Suppress("PackageName")
package `2021`

import Project

class DayTwentyone(file: String, isTest: Boolean = false) : Project(file, isTest) {
    val lines = getLines(file)
    val ip1 = lines[0].last().toString().toInt() - 1
    val ip2 = lines[1].last().toString().toInt() - 1
    var countUniverses = 0
    val v2StateCache = HashMap<UniverseState, Pair<Long, Long>>()

    override suspend fun part1(): Any {
        var roll = 1
        var p1Score = 0
        var p2Score = 0
        var p1 = ip1
        var p2 = ip2

        while (p1Score < 1000 && p2Score < 1000) {
            if (roll % 2 == 1) {
                p1 = (p1 + (roll * 3) + 3) % 10
                p1Score += p1 + 1
            } else {
                p2 = (p2 + (roll * 3) + 3) % 10
                p2Score += p2 + 1
            }
            roll += 3
        }

        val loser = if (p1Score > p2Score) p2Score else p1Score
        return loser * (roll - 1)
    }

    override suspend fun part2(): Any {
        val turn = true
        val p1Score = 0
        val p2Score = 0
        val p1 = ip1
        val p2 = ip2

        val universes = getUniverses(p1Score, p2Score, p1, p2, turn)
        var results = Pair(0L,0L)
        for (state in universes) {
            results = sum(listOf(results, play(state)))
        }

        return if (results.first > results.second) results.first else results.second
    }

    private fun getUniverses(p1Score: Int, p2Score: Int, p1: Int, p2: Int, turn: Boolean): List<UniverseState> {
        val universes = ArrayList<UniverseState>()
        for (d1 in 1..3) {
            for (d2 in 1..3) {
                for (d3 in 1..3) {
                    universes.add(UniverseState(p1Score, p2Score, p1, p2, turn, d1+d2+d3))
                }
            }
        }
        return universes
    }

    private fun sum(play: List<Pair<Long, Long>>): Pair<Long, Long> {
        return Pair(play.map { it.first }.sum(), play.map { it.second }.sum() )
    }

    private fun play(state: UniverseState): Pair<Long, Long> {
        if (v2StateCache.containsKey(state)) {
            return v2StateCache[state]!!
        }

        var p1Score = state.p1Score
        var p2Score = state.p2Score
        var p1 = state.p1
        var p2 = state.p2
        var turn = state.turn
        val roll = state.roll

        if (turn) {
            p1 = (p1 + roll) % 10
            p1Score += p1 + 1
        } else {
            p2 = (p2 + roll) % 10
            p2Score += p2 + 1
        }

        turn = !turn

        if (p1Score >= 21) {
            return Pair(1,0)
        } else if (p2Score >= 21) {
            return Pair(0,1)
        }

        val universes = getUniverses(p1Score, p2Score, p1, p2, turn)
        var results = Pair(0L,0L)
        for (state2 in universes) {
            results = sum(listOf(results, play(state2)))
        }

        v2StateCache[state] = results

        return results
    }

}

class UniverseState(val p1Score: Int, val p2Score: Int, val p1: Int, val p2: Int, val turn: Boolean, val roll: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UniverseState) return false

        if (p1Score != other.p1Score) return false
        if (p2Score != other.p2Score) return false
        if (p1 != other.p1) return false
        if (p2 != other.p2) return false
        if (turn != other.turn) return false
        if (roll != other.roll) return false

        return true
    }

    override fun hashCode(): Int {
        var result = p1Score
        result = 31 * result + p2Score
        result = 31 * result + p1
        result = 31 * result + p2
        result = 31 * result + turn.hashCode()
        result = 31 * result + roll
        return result
    }
}
