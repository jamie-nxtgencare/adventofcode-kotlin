class DayTwentyone(file: String) : Project {
    val lines = getLines(file)
    val ip1 = lines[0].last().toString().toInt() - 1
    val ip2 = lines[1].last().toString().toInt() - 1
    var countUniverses = 0

    override fun part1(): Any {
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

    override fun part2(): Any {
        val turn = true
        val p1Score = 0
        val p2Score = 0
        val p1 = ip1
        val p2 = ip2

        val results = sum(getUniverses(p1Score, p2Score, p1, p2, turn))

        return if (results.first > results.second) results.first else results.second
    }

    private fun getUniverses(p1Score: Int, p2Score: Int, p1: Int, p2: Int, turn: Boolean): List<Pair<Long, Long>> {
        val universes = ArrayList<Pair<Long, Long>>()
        for (d1 in 1..3) {
            for (d2 in 1..3) {
                for (d3 in 1..3) {
                    universes.add(play(p1Score, p2Score, p1, p2, turn, d1+d2+d3))
                }
            }
        }
        return universes
    }

    private fun sum(play: List<Pair<Long, Long>>): Pair<Long, Long> {
        return Pair(play.map { it.first }.sum(), play.map { it.second }.sum() )
    }

    private fun play(ip1Score: Int, ip2Score: Int, ip1: Int, ip2: Int, iturn: Boolean, roll: Int): Pair<Long, Long> {
        println(countUniverses++)

        var p1Score = ip1Score
        var p2Score = ip2Score
        var p1 = ip1
        var p2 = ip2
        var turn = iturn

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

        return sum(getUniverses(p1Score, p2Score, p1, p2, turn))
    }

}